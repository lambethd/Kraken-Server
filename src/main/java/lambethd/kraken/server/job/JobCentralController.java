package lambethd.kraken.server.job;

import domain.orchestration.IJob;
import domain.orchestration.JobStatus;
import lambethd.kraken.data.mongo.repository.IJobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class JobCentralController {

    @Value("${job.thread_pool.core_threads}")
    private int coreThreads;
    @Value("${job.thread_pool.maximum_threads}")
    private int maximumThreads;
    @Value("${job.thread_pool.keep_alive_time_seconds}")
    private int keepAliveSeconds;
    @Value("${job.controller.timeout_minutes}")
    private int controllerTimeoutMinutes;
    @Autowired
    private IJobRepository jobRepository;
    @Autowired
    private JobProcessorFactory jobProcessorFactory;

    AtomicBoolean running = new AtomicBoolean(false);
    Boolean isInitialised = false;
    ScheduledFuture<?> jobCreationFuture;
    ScheduledFuture<?> jobCompletingFuture;
    private ConcurrentHashMap<String, Future<Boolean>> jobFutures;

    ScheduledExecutorService repeatingExecutor;
    ExecutorService jobExecutor;

    public void init() {
        jobFutures = new ConcurrentHashMap<>();
        repeatingExecutor = Executors.newScheduledThreadPool(coreThreads);
        jobExecutor = Executors.newFixedThreadPool(maximumThreads);
        isInitialised = true;
        System.out.println("Initialised JobCentralController");
    }

    public void begin() {
        running.set(true);
        jobCreationFuture = repeatingExecutor.scheduleAtFixedRate(jobCreation(), 0, 10, TimeUnit.SECONDS);
        System.out.println("Scheduled job creation service");
        jobCompletingFuture = repeatingExecutor.scheduleAtFixedRate(jobCompleting(), 5, 10, TimeUnit.SECONDS);
        System.out.println("Scheduled job completion service");
    }

    public Boolean requestJobCancellation(String jobId) {
        jobFutures.get(jobId).cancel(true);
        try {
            TimeUnit.SECONDS.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return jobFutures.get(jobId).isCancelled();
    }

    public void end() {
        running.set(false);
        jobFutures.forEachKey(maximumThreads, key -> {
            jobFutures.get(key).cancel(true);
        });
        jobCreationFuture.cancel(true);
        jobCompletingFuture.cancel(true);
    }

    private Runnable jobCreation() {
        return () -> {
            System.out.println("Looking for jobs to create!");
            List<IJob> jobsToProcess = jobRepository.findJobByStatus(JobStatus.Pending);
            jobsToProcess.forEach(job -> {
                if (!jobFutures.containsKey(job.getId())) {
                    System.out.println("Found a job to create!");
                    setJobStatus(job, JobStatus.Started);
                    try {
                        IJobProcessor processor = jobProcessorFactory.getJobProcessor(job);
                        if (processor.validate()) {
                            jobFutures.put(job.getId(), jobExecutor.submit(processor));
                        } else {
                            setJobStatus(job, JobStatus.Failed);
                        }
                    } catch (Exception e) {
                        setJobStatus(job, JobStatus.Failed);
                        e.printStackTrace();
                    }
                }
            });
        };
    }

    private void setJobStatus(IJob job, JobStatus status) {
        System.out.println("Setting job(" + job.getId() + ") to " + status);
        job.setStatus(status);
        jobRepository.save(job);
    }

    private Runnable jobCompleting() {
        return () -> {
            System.out.println("Finding jobs that have completed");
            jobFutures.forEachKey(maximumThreads, key -> {
                if (jobFutures.get(key).isDone()) {
                    //TODO: Complete the job here
                    try {
                        Boolean result = jobFutures.get(key).get();
                        System.out.println("Found completed job with status: " + result);
                        IJob job = jobRepository.findById(key);
                        jobFutures.remove(key);
                        setJobStatus(job, JobStatus.Completed);
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            });
        };
    }

    public void failedPreviouslyStartedJobs() {
        List<IJob> startedJobs = jobRepository.findJobByStatus(JobStatus.Started);
        startedJobs.forEach(j -> {
            System.out.println("Setting Started job(" + j.getId() + ") to failed.");
            setJobStatus(j, JobStatus.Failed);
        });
        List<IJob> blockedJobs = jobRepository.findJobByStatus(JobStatus.Blocked);
        blockedJobs.forEach(j -> {
            System.out.println("Setting Blocked job(" + j.getId() + ") to failed.");
            setJobStatus(j, JobStatus.Failed);
        });
    }
}
