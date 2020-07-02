package lambethd.kraken.server.job;

import domain.orchestration.IJob;
import lambethd.kraken.data.mongo.repository.IJobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProgressReporter {
    @Autowired
    private IJobRepository jobRepository;

    public void reportProgress(IJob job, int progress) {
        if (progress > 100) {
            progress = 100;
        } else if (progress < 0) {
            progress = 0;
        }
        jobRepository.findById(job.getId());
        job.setProgress(progress);
        jobRepository.save(job);
    }
}
