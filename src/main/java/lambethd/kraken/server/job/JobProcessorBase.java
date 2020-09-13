package lambethd.kraken.server.job;

import domain.orchestration.IJob;
import lambethd.kraken.data.mongo.repository.IJobRepository;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class JobProcessorBase implements IJobProcessor {
    @Autowired
    private ProgressReporter progressReporter;
    @Autowired
    private IJobRepository jobRepository;

    protected IJob job;

    public IJob getJob() {
        return job;
    }

    @Override
    public void setJob(IJob job) {
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
