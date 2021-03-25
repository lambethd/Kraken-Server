package lambethd.kraken.server.scheduling;

import domain.orchestration.IJob;
import domain.orchestration.Job;
import domain.orchestration.JobStatus;
import domain.orchestration.JobType;
import lambethd.kraken.data.mongo.repository.IJobRepository;
import lambethd.kraken.resource.interfaces.IInfoApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class DailyBatchScheduler {

    @Autowired
    private IJobRepository jobRepository;
    @Autowired
    private IInfoApi infoApi;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Scheduled(cron = "0 15 0 * * ?")
    public void scheduleDailyJobs(){
        int runeDay = -1;
        try{
            runeDay = infoApi.getInfo().lastConfigUpdateRuneday;
        }catch(IOException e){
            e.printStackTrace();
        }
        logger.info("Starting daily jobs");
        for (JobType jobType : dailyJobs()) {
            IJob job = new Job();
            job.setStatus(JobStatus.Pending);
            job.setJobType(jobType);
            job.setUsername("System");
            job.setRuneDay(runeDay);
            jobRepository.save(job);
        }
    }

    public JobType[] dailyJobs() {
        return new JobType[]{
                JobType.DailyDataLoader,
                JobType.DailyGraphLoader,
                JobType.BuyingLimitLoader,
                JobType.DashboardDataCreator
        };
    }
}
