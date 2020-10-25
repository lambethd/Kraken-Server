package lambethd.kraken.server.interfaces;

import domain.orchestration.Job;
import domain.orchestration.JobDependency;
import domain.orchestration.JobDetail;

import java.util.List;

public interface IJobDetailService {
    List<JobDetail> findJobsDependingOn(Job job);

    List<JobDependency> findJobDependencies(Job job);
}
