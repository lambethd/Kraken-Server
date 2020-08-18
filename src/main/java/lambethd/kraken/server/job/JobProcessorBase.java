package lambethd.kraken.server.job;

import domain.orchestration.IJob;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class JobProcessorBase implements IJobProcessor {
    @Autowired
    private ProgressReporter progressReporter;

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
            return false;
        }
    }

    public abstract Boolean callInternal() throws Exception;
}
