package lambethd.kraken.server.job;

import domain.orchestration.Job;
import lambethd.kraken.server.util.BeanUtil;
import org.springframework.stereotype.Service;

@Service
public class JobProcessorFactory {


    public IJobProcessor getJobProcessor(Job job) throws Exception {
        Class<?> processorClassName = Class.forName("lambethd.kraken.server.job." + job.getJobType());
        IJobProcessor jobProcessor = (IJobProcessor) BeanUtil.getBean(processorClassName);
        jobProcessor.setJob(job);
        return jobProcessor;
    }
}
