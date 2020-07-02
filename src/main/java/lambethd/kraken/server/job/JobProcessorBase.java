package lambethd.kraken.server.job;

import domain.orchestration.IJob;

public abstract class JobProcessorBase implements IJobProcessor {
    protected IJob job;

    public IJob getJob() {
        return job;
    }

    @Override
    public void setJob(IJob job) {
        this.job = job;
    }
}
