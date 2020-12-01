package lambethd.kraken.server.configuration;


import com.fasterxml.jackson.databind.ObjectMapper;
import domain.orchestration.JobDetail;
import lambethd.kraken.data.mongo.repository.IJobDetailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

@Service
public class RepositorySetup {
    @Autowired
    private IJobDetailRepository jobDetailRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void setup() {
        setupJobDetail();
    }

    private void setupJobDetail() {
        logger.info("Setting up JobDetail");
        Resource resource = new ClassPathResource("JobDetail.json");
        try {
            String jobDetail = String.join("\n", Files.readAllLines(resource.getFile().toPath()));
            jobDetailRepository.deleteAll();
            JobDetail[] jobDetails = new ObjectMapper().readValue(jobDetail, JobDetail[].class);
            jobDetailRepository.saveAll(Arrays.asList(jobDetails));
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("Finished setting up JobDetail");
    }
}
