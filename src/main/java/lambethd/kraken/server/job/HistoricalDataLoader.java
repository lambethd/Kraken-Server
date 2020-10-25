package lambethd.kraken.server.job;

import domain.orchestration.JobType;
import lambethd.kraken.resource.interfaces.IHistoricalDataApi;
import lambethd.kraken.resource.interfaces.IInfoApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import runescape.*;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service("HistoricalDataLoader")
@Scope("prototype")
public class HistoricalDataLoader extends JobProcessorBase {
    @Autowired
    private ProgressReporter progressReporter;
    @Autowired
    private IInfoApi infoApi;
    @Autowired
    private IHistoricalDataApi historicalDataApi;

    @Value("${job.batch_size}")
    private int batchSize;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean validate() {
        //TODO: validate based on the IJob input
        return true;
    }

    @Override
    public JobType getJobType() {
        return JobType.HistoricalDataLoader;
    }

    @Override
    public Boolean callInternal() {
        return true;
    }
}
