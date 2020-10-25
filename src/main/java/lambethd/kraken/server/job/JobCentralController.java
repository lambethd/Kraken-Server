package lambethd.kraken.server.job;

import domain.orchestration.Job;
import domain.orchestration.JobDependency;
import domain.orchestration.JobStatus;
import lambethd.kraken.data.repository.JobRepository;
import lambethd.kraken.resource.interfaces.IInfoApi;
import lambethd.kraken.server.interfaces.IJobDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

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
    private JobRepository jobRepository;
    @Autowired
    private JobProcessorFactory jobProcessorFactory;
    @Autowired
    private IJobDetailService jobDetailService;
    @Autowired
    private IInfoApi infoApi;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


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
        logger.info("Initialised JobCentralController");
    }

    public void begin() {
        running.set(true);
        jobCreationFuture = repeatingExecutor.scheduleAtFixedRate(jobCreation(), 0, controllerTimeoutMinutes, TimeUnit.MINUTES);
        logger.info("Scheduled job creation service");
        jobCompletingFuture = repeatingExecutor.scheduleAtFixedRate(jobCompleting(), 2, controllerTimeoutMinutes, TimeUnit.MINUTES);
        logger.info("Scheduled job completion service");
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
//            logger.info("Looking for jobs to create");
            List<Job> jobsToProcess = jobRepository.findJobByStatus(JobStatus.Pending);
            jobsToProcess.forEach(job -> {
                if (canRunJob(job)) {
                    if (!jobFutures.containsKey(job.getId())) {
                        job.setStartTime(LocalDateTime.now());
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
                }
            });
        };
    }

    private boolean canRunJob(Job job) {
        List<JobDependency> dependencies = jobDetailService.findJobDependencies(job);
        List<JobDependency> missingDependencies = dependencies.stream()
                .filter(jobDependency -> !jobRepository.existsByJobTypeAndStatusAndRuneDay(jobDependency.getJobType(), JobStatus.Completed, job.getRuneDay()))
                .collect(Collectors.toList());
        if (missingDependencies.isEmpty()) {
            return true;
        } else {
            logger.info("Dependencies are missing for job: " + job.getJobType() + ". Missing dependencies are: " + missingDependencies.stream().map(jd -> jd.getJobType().toString()).collect(Collectors.joining(",")));
            return false;
        }
    }

    private void setJobStatus(Job job, JobStatus status) {
        job.setStatus(status);
        logger.info("#############################################################################################################");
        logger.info("Job (" + job.getId() + ") of JobType: " + job.getJobType() + " set to status: " + job.getStatus());
        logger.info("#############################################################################################################");
        jobRepository.save(job);
    }

    private Runnable jobCompleting() {
        return () -> {
//            logger.info("Finding jobs that have completed");
            jobFutures.forEachKey(maximumThreads, key -> {
                if (jobFutures.get(key).isDone()) {
                    try {
                        Boolean result = jobFutures.get(key).get();
                        Job job = jobRepository.findOne(key);
                        jobFutures.remove(key);
                        if (result) {
                            setJobStatus(job, JobStatus.Completed);
                        } else {
                            setJobStatus(job, JobStatus.Failed);
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            });
        };
    }

    public void failedPreviouslyStartedJobs() {
        List<Job> startedJobs = jobRepository.findJobByStatus(JobStatus.Started);
        startedJobs.forEach(j -> {
            logger.info("Setting Started job(" + j.getId() + ") to failed.");
            setJobStatus(j, JobStatus.Failed);
        });
        List<Job> blockedJobs = jobRepository.findJobByStatus(JobStatus.Blocked);
        blockedJobs.forEach(j -> {
            logger.info("Setting Blocked job(" + j.getId() + ") to failed.");
            setJobStatus(j, JobStatus.Failed);
        });
    }
}
