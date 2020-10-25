package lambethd.kraken.server.service;

import domain.orchestration.Job;
import dto.Configuration;
import lambethd.kraken.data.repository.ConfigRepository;
import lambethd.kraken.data.repository.JobRepository;
import lambethd.kraken.server.configuration.RepositorySetup;
import lambethd.kraken.server.interfaces.IJobDetailService;
import lambethd.kraken.server.job.JobCentralController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class Program {
    @Autowired
    private JobCentralController jobCentralController;
    @Autowired
    private IJobDetailService jobDetailService;
    @Autowired
    private RepositorySetup repositorySetup;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    ConfigRepository configRepository;


    private void setup() {
        repositorySetup.setup();
    }

    @PostConstruct
    public void run() {
        setup();
        configRepository.save(new Configuration());
        Iterable<Configuration> dtos = configRepository.findAll();


        jobRepository.save(new Job());
        Iterable<Job> jobs = jobRepository.findAll();


        jobCentralController.init();
        jobCentralController.failedPreviouslyStartedJobs();
        jobCentralController.begin();

//        job = new Job();
//        job.setStatus(JobStatus.Pending);
//        job.setJobType(JobType.DailyDataLoader);
//        job.setUsername("David");
//        jobRepository.save(job);
    }
}
