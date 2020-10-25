package lambethd.kraken.server.service;

import domain.orchestration.Job;
import domain.orchestration.JobDependency;
import domain.orchestration.JobDetail;
import lambethd.kraken.data.repository.IJobDetailRepository;
import lambethd.kraken.server.interfaces.IJobDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class JobDetailService implements IJobDetailService {

    @Autowired
    private IJobDetailRepository jobDetailRepository;

    @Override
    public List<JobDetail> findJobsDependingOn(Job job) {
        return StreamSupport.stream(jobDetailRepository.findAll().spliterator(), false).filter(j -> j.getJobDependencies().stream().anyMatch(inner -> inner.getJobType() == job.getJobType())).collect(Collectors.toList());
    }

    @Override
    public List<JobDependency> findJobDependencies(Job job) {
        JobDetail jobDetail = StreamSupport.stream(jobDetailRepository.findAll().spliterator(), false).filter(j -> j.getJobType() == job.getJobType()).findFirst().orElse(null);
        return jobDetail == null ? new ArrayList<>() : jobDetail.getJobDependencies();
    }
}
