package lambethd.kraken.server.interfaces;

import domain.orchestration.IJob;
import domain.orchestration.JobDependency;
import domain.orchestration.JobDetail;

import java.util.List;

public interface IJobDetailService {
    List<JobDetail> findJobsDependingOn(IJob job);

    List<JobDependency> findJobDependencies(IJob job);
}
