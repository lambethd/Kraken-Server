package lambethd.kraken.server.job;

import domain.orchestration.IJob;
import org.springframework.stereotype.Service;

@Service
public class JobProcessorFactory {

    public IJobProcessor getJobProcessor(IJob job) throws Exception {
        switch (job.getJobType()) {
            case HistoricalDataLoader:
                return new HistoricalDataLoader(job);
            default:
                throw new Exception("Could not find processor for JobType: " + job.getJobType());
        }
    }
}
