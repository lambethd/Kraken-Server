package lambethd.kraken.server.service;

import domain.orchestration.IJob;
import domain.orchestration.Job;
import domain.orchestration.JobStatus;
import domain.orchestration.JobType;
import lambethd.kraken.data.mongo.repository.IJobRepository;
import lambethd.kraken.server.configuration.RepositorySetup;
import lambethd.kraken.server.job.JobCentralController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class Program {

    @Autowired
    private RepositorySetup repositorySetup;
    @Autowired
    private IJobRepository jobRepository;
    @Autowired
    private JobCentralController jobCentralController;

    private void setup() {
        repositorySetup.setup();
    }

    @PostConstruct
    public void run() {
        setup();
        jobCentralController.init();
        jobCentralController.failedPreviouslyStartedJobs();
        jobCentralController.begin();
        IJob job = new Job();
        job.setStatus(JobStatus.Pending);
        job.setJobType(JobType.HistoricalDataLoader);
        job.setUsername("David");
        jobRepository.save(job);
        job = new Job();
        job.setStatus(JobStatus.Pending);
        job.setJobType(JobType.DailyDataLoader);
        job.setUsername("David");
        jobRepository.save(job);
    }
}
