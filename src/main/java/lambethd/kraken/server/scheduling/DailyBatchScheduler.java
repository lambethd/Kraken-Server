package lambethd.kraken.server.scheduling;

import domain.orchestration.IJob;
import domain.orchestration.Job;
import domain.orchestration.JobStatus;
import domain.orchestration.JobType;
import lambethd.kraken.data.mongo.repository.IJobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class DailyBatchScheduler {

    @Autowired
    private IJobRepository jobRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Scheduled(cron="0 15 0 * * ?")
    public void scheduleDailyDataLoader(){
        logger.info("Starting daily load batch jobs");
        IJob job = new Job();
        job.setStatus(JobStatus.Pending);
        job.setJobType(JobType.DailyDataLoader);
        job.setUsername("System");
        jobRepository.save(job);
    }

    @Scheduled(cron="0 15 4 * * ?")
    public void scheduleHistoricalDataLoader(){
        logger.info("Starting historical load batch jobs");
        IJob job = new Job();
        job.setStatus(JobStatus.Pending);
        job.setJobType(JobType.DailyGraphLoader);
        job.setUsername("System");
        jobRepository.save(job);
    }
}
