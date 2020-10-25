package lambethd.kraken.server.scheduling;

import domain.orchestration.Job;
import domain.orchestration.JobStatus;
import domain.orchestration.JobType;
import lambethd.kraken.data.repository.JobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class DailyBatchScheduler {

    @Autowired
    private JobRepository jobRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Scheduled(cron="0 15 0 * * ?")
    public void scheduleDailyDataLoader(){
        logger.info("Starting daily load batch jobs");
        Job job = new Job();
        job.setStatus(JobStatus.Pending);
        job.setJobType(JobType.DailyDataLoader);
        job.setUsername("System");
        jobRepository.save(job);
    }

    @Scheduled(cron="0 15 4 * * ?")
    public void scheduleHistoricalDataLoader(){
        logger.info("Starting historical load batch jobs");
        Job job = new Job();
        job.setStatus(JobStatus.Pending);
        job.setJobType(JobType.HistoricalDataLoader);
        job.setUsername("System");
        jobRepository.save(job);
    }
}
