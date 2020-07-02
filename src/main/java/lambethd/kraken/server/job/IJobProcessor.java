package lambethd.kraken.server.job;

import domain.orchestration.JobType;

import java.util.concurrent.Callable;

public interface IJobProcessor extends Callable<Boolean> {
    boolean validate();

    JobType getJobType();
}
