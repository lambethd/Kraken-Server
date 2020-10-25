package lambethd.kraken.server.job;

import domain.orchestration.Job;
import lambethd.kraken.data.repository.JobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProgressReporter {
    @Autowired
    private JobRepository jobRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void reportProgress(Job job, int progress, int total) {
        float progressFloat = progress;
        progressFloat = total == 0 ? 0 : progressFloat * 100 / total;
        if (progressFloat > 100) {
            progressFloat = 100;
        } else if (progressFloat < 0) {
            progressFloat = 0;
        }

        int progressInt = (int) progressFloat;
        logger.info("Saving progress of " + job.getJobType() + " at " + progressInt);
        jobRepository.findOne(job.getId());
        job.setProgress(progressInt);
        jobRepository.save(job);
    }
}
