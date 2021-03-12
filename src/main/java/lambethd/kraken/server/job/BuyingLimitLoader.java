package lambethd.kraken.server.job;

import domain.orchestration.JobType;
import lambethd.kraken.data.mongo.repository.IItemRepository;
import lambethd.kraken.resource.interfaces.IBuyingLimitApi;
import lambethd.kraken.resource.interfaces.IItemApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import runescape.BuyingLimit;
import runescape.Item;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service("BuyingLimitLoader")
@Scope("prototype")
public class BuyingLimitLoader extends JobProcessorBase {
    @Autowired
    private ProgressReporter progressReporter;
    @Autowired
    private IBuyingLimitApi buyingLimitApi;
    @Autowired
    private IItemRepository itemRepository;

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
        return JobType.BuyingLimitLoader;
    }

    @Override
    public Boolean callInternal() throws IOException {
        AtomicInteger count = new AtomicInteger();
        List<BuyingLimit> buyingLimits = buyingLimitApi.getBuyingLimits();
        List<Item> items = new LinkedList<>();
        buyingLimits.forEach(i -> {
            List<Item> itemMatches = itemRepository.getItemsByName(i.getItemName(), Sort.unsorted());
            if(itemMatches == null || itemMatches.isEmpty()) {
                return;
            }
            Item item = itemMatches.get(0);
            item.buyingLimit = i.getLimit();
            items.add(item);
            count.getAndIncrement();
            if (count.get() % batchSize == 0) {
                itemRepository.saveAll(items);
                items.clear();
                progressReporter.reportProgress(getJob(), count.get(), buyingLimits.size());
            }
        });
        itemRepository.saveAll(items);
        return true;
    }
}
