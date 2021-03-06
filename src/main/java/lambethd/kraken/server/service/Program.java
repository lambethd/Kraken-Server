package lambethd.kraken.server.service;

import domain.orchestration.Job;
import domain.orchestration.JobStatus;
import domain.orchestration.JobType;
import lambethd.kraken.data.mongo.repository.IJobRepository;
import lambethd.kraken.resource.interfaces.IInfoApi;
import lambethd.kraken.server.configuration.RepositorySetup;
import lambethd.kraken.server.interfaces.IJobDetailService;
import lambethd.kraken.server.job.JobCentralController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
public class Program {

    @Autowired
    private RepositorySetup repositorySetup;
    @Autowired
    private IJobRepository jobRepository;
    @Autowired
    private JobCentralController jobCentralController;
    @Autowired
    private IJobDetailService jobDetailService;
    @Autowired
    private IInfoApi infoApi;

    private void setup() {
        repositorySetup.setup();
    }

    @PostConstruct
    public void run() throws IOException {
        setup();
        jobCentralController.init();
        jobCentralController.failedPreviouslyStartedJobs();

        Job job = new Job();
        job.setStatus(JobStatus.Pending);
        job.setJobType(JobType.DailyGraphLoader);
        job.setUsername("System");
        job.setRuneDay(infoApi.getInfo().lastConfigUpdateRuneday);
        jobRepository.save(job);

        jobCentralController.begin();
    }
}
