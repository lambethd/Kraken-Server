package lambethd.kraken.server.job;

import domain.DashboardDto;
import domain.Index;
import domain.orchestration.JobType;
import dto.ItemMovementDto;
import lambethd.kraken.data.mongo.repository.DashboardDtoRepository;
import lambethd.kraken.data.mongo.repository.IGraphRepository;
import lambethd.kraken.server.interfaces.IOutlierService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import portfolio.RangeType;
import runescape.Graph;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Service("DashboardDataCreator")
@Scope("prototype")
public class DashboardDataCreator extends JobProcessorBase {
    @Autowired
    private ProgressReporter progressReporter;
    @Autowired
    private IGraphRepository graphRepository;
    @Autowired
    private IOutlierService outlierService;
    @Autowired
    private DashboardDtoRepository dashboardRepository;

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
        return JobType.DashboardDataCreator;
    }

    @Override
    public Boolean callInternal() {
        DashboardDto dashboard = new DashboardDto();
        List<Graph> graphs = graphRepository.findAll();
        List<ItemMovementDto> itemMovementDtos = outlierService.calculateItemMovements(graphs);
        int n = 10;

        //topIncreases by value
        List<ItemMovementDto> topIncreasesByDailyValue = outlierService.getTopNByValue(itemMovementDtos, n, RangeType.DAY);
        progressReporter.reportProgress(this.getJob(), 1, 10);
        List<ItemMovementDto> topIncreasesByMonthlyValue = outlierService.getTopNByValue(itemMovementDtos, n, RangeType.MONTH);
        progressReporter.reportProgress(this.getJob(), 2, 10);
        //topIncreases by %
        List<ItemMovementDto> topIncreasesByDailyPercentage = outlierService.getTopNByPercentage(itemMovementDtos, n, RangeType.DAY);
        progressReporter.reportProgress(this.getJob(), 3, 10);
        List<ItemMovementDto> topIncreasesByMonthlyPercentage = outlierService.getTopNByPercentage(itemMovementDtos, n, RangeType.MONTH);
        progressReporter.reportProgress(this.getJob(), 4, 10);
        //topDecreases by value
        List<ItemMovementDto> topDecreasesByDailyValue = outlierService.getBottomNByValue(itemMovementDtos, n, RangeType.DAY);
        progressReporter.reportProgress(this.getJob(), 5, 10);
        List<ItemMovementDto> topDecreasesByMonthlyValue = outlierService.getBottomNByValue(itemMovementDtos, n, RangeType.MONTH);
        progressReporter.reportProgress(this.getJob(), 6, 10);
        //topDecreases by %
        List<ItemMovementDto> topDecreasesByDailyPercentage = outlierService.getBottomNByPercentage(itemMovementDtos, n, RangeType.DAY);
        progressReporter.reportProgress(this.getJob(), 7, 10);
        List<ItemMovementDto> topDecreasesByMonthlyPercentage = outlierService.getBottomNByPercentage(itemMovementDtos, n, RangeType.MONTH);
        progressReporter.reportProgress(this.getJob(), 8, 10);
        //Indexes
        List<Index> indexes = new ArrayList<>();
        progressReporter.reportProgress(this.getJob(), 9, 10);
        //ItemsToWatch
        List<Integer> itemsToWatch = new ArrayList<>();


        dashboard.setTopIncreasesByDailyValue(topIncreasesByDailyValue);
        dashboard.setTopIncreasesByDailyPercentage(topIncreasesByDailyPercentage);

        dashboard.setTopIncreasesByMonthlyValue(topIncreasesByMonthlyValue);
        dashboard.setTopIncreasesByMonthlyPercentage(topIncreasesByMonthlyPercentage);


        dashboard.setTopDecreasesByDailyValue(topDecreasesByDailyValue);
        dashboard.setTopDecreasesByDailyPercentage(topDecreasesByDailyPercentage);

        dashboard.setTopDecreasesByMonthlyValue(topDecreasesByMonthlyValue);
        dashboard.setTopDecreasesByMonthlyPercentage(topDecreasesByMonthlyPercentage);
        //TODO: work out indexes
        dashboard.setIndexList(indexes);
        //TODO: work out items to watch (should this be called 5%ers?)
        dashboard.setItemsToWatch(itemsToWatch);

        dashboard.setCreated(LocalDateTime.now(ZoneOffset.UTC));

        dashboardRepository.save(dashboard);
        return true;
    }
}
