package lambethd.kraken.server.job;

import domain.orchestration.IJob;
import domain.orchestration.JobType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

public class HistoricalDataLoader extends JobProcessorBase {
    @Autowired
    private ProgressReporter progressReporter;

    public HistoricalDataLoader(IJob job) {
        super(job);
    }

    @Override
    public boolean validate() {
        //TODO: validate based on the IJob input
        return true;
    }

    @Override
    public JobType getJobType() {
        return JobType.HistoricalDataLoader;
    }

    @Override
    public Boolean call() {
        System.out.println("Beginning work on job: " + job.getId());

        return true;
    }
}
