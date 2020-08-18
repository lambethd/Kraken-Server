package lambethd.kraken.server.service;

import domain.orchestration.IJob;
import domain.orchestration.JobDependency;
import domain.orchestration.JobDetail;
import lambethd.kraken.data.mongo.repository.IJobDetailRepository;
import lambethd.kraken.server.interfaces.IJobDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobDetailService implements IJobDetailService {

    @Autowired
    private IJobDetailRepository jobDetailRepository;

    @Override
    public List<JobDetail> findJobsDependingOn(IJob job) {
        return jobDetailRepository.findAll().stream().filter(j -> j.getJobDependencies().stream().anyMatch(inner -> inner.getJobType() == job.getJobType())).collect(Collectors.toList());
    }

    @Override
    public List<JobDependency> findJobDependencies(IJob job) {
        JobDetail jobDetail = jobDetailRepository.findAll().stream().filter(j -> j.getJobType() == job.getJobType()).findFirst().orElse(null);
        return jobDetail == null ? new ArrayList<>() : jobDetail.getJobDependencies();
    }
}
