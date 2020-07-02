package lambethd.kraken.server.job;

import domain.orchestration.IJob;

public abstract class JobProcessorBase implements IJobProcessor {
    protected IJob job;

    public JobProcessorBase(IJob job) {
        this.job = job;
    }
}
