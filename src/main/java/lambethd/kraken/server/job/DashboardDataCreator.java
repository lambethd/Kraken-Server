package lambethd.kraken.server.job;

import domain.DashboardDto;
import domain.Index;
import domain.orchestration.JobType;
import dto.ItemMovementDto;
import lambethd.kraken.data.mongo.repository.IGraphRepository;
import lambethd.kraken.data.mongo.repository.IItemRepository;
import lambethd.kraken.server.util.IGraphUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import runescape.Graph;
import runescape.Item;
import runescape.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service("DashboardDataCreator")
@Scope("prototype")
public class DashboardDataCreator extends JobProcessorBase {
    @Autowired
    private ProgressReporter progressReporter;
    @Autowired
    private IGraphRepository graphRepository;
    @Autowired
    private IItemRepository itemRepository;

    @Autowired
    private IGraphUtility graphUtility;

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
        //topIncreases by value
        List<ItemMovementDto> topIncreasesByDailyValue = new ArrayList<>();
        List<ItemMovementDto> topIncreasesByMonthlyValue = new ArrayList<>();
        //topIncreases by %
        List<ItemMovementDto> topIncreasesByDailyPercentage = new ArrayList<>();
        List<ItemMovementDto> topIncreasesByMonthlyPercentage = new ArrayList<>();
        //topDecreases by value
        List<ItemMovementDto> topDecreasesByDailyValue = new ArrayList<>();
        List<ItemMovementDto> topDecreasesByMonthlyValue = new ArrayList<>();
        //topDecreases by %
        List<ItemMovementDto> topDecreasesByDailyPercentage = new ArrayList<>();
        List<ItemMovementDto> topDecreasesByMonthlyPercentage = new ArrayList<>();
        //Indexes
        List<Index> indexes = new ArrayList<>();
        //ItemsToWatch
        List<Integer> itemsToWatch = new ArrayList<>();


        List<Item> items = itemRepository.findAll();
        List<ItemMovementDto> itemMovements = items.stream().map(i->{
            ItemMovementDto dailyMovement = new ItemMovementDto();
            Graph graph = graphRepository.getGraphById(i.id);
            dailyMovement.setValueChange();
            graphUtility.getValueByOffset(0);


            dailyMovement.setItemId(i.id);

            return dailyMovement;
        }).collect(Collectors.toList());


        dashboard.setTopIncreasesByDailyValue(topIncreasesByDailyValue);
        dashboard.setTopIncreasesByDailyPercentage(topIncreasesByDailyPercentage);

        dashboard.setTopIncreasesByMonthlyValue(topIncreasesByMonthlyValue);
        dashboard.setTopIncreasesByMonthlyPercentage(topIncreasesByMonthlyPercentage);


        dashboard.setTopDecreasesByDailyValue(topDecreasesByDailyValue);
        dashboard.setTopDecreasesByDailyPercentage(topDecreasesByDailyPercentage);

        dashboard.setTopDecreasesByMonthlyValue(topDecreasesByMonthlyValue);
        dashboard.setTopDecreasesByMonthlyPercentage(topDecreasesByMonthlyPercentage);
        dashboard.setIndexList(indexes);

        dashboard.setItemsToWatch(itemsToWatch);
        return true;
    }
}
