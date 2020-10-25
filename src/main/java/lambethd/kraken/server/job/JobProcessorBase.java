package lambethd.kraken.server.job;

import domain.orchestration.Job;
import lambethd.kraken.data.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class JobProcessorBase implements IJobProcessor {
    @Autowired
    private ProgressReporter progressReporter;
    @Autowired
    private JobRepository jobRepository;

    protected Job job;

    public Job getJob() {
        return job;
    }

    @Override
    public void setJob(Job job) {
        this.job = job;
    }

    @Override
    public Boolean call() {
        try {
            Boolean result = callInternal();
            progressReporter.reportProgress(job, 1, 1);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            job.setError(e.getMessage());
            jobRepository.save(job);
            return false;
        }
    }

    public abstract Boolean callInternal() throws Exception;
}
