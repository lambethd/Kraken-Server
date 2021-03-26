package lambethd.kraken.server.job;

import domain.orchestration.IJob;
import lambethd.kraken.data.mongo.repository.IJobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class JobProcessorBase implements IJobProcessor {
    @Autowired
    private ProgressReporter progressReporter;
    @Autowired
    private IJobRepository jobRepository;

    Logger logger = LoggerFactory.getLogger(this.getClass());

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
            logger.error("There was an error in job " + job.getId(), e);
            job.setError(e.getMessage());
            jobRepository.save(job);
            return false;
        }
    }

    public abstract Boolean callInternal() throws Exception;
}
