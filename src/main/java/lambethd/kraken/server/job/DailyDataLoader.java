package lambethd.kraken.server.job;

import domain.orchestration.JobType;
import lambethd.kraken.data.mongo.repository.IItemRepository;
import lambethd.kraken.resource.interfaces.IItemApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import runescape.Item;
import runescape.ItemCategory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service("DailyDataLoader")
@Scope("prototype")
public class DailyDataLoader extends JobProcessorBase {
    @Autowired
    private ProgressReporter progressReporter;
    @Autowired
    private IItemApi itemApi;
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
        return JobType.DailyDataLoader;
    }

    @Override
    public Boolean callInternal() throws IOException {
        AtomicInteger count = new AtomicInteger();
        List<Item> items = new ArrayList<>();
        itemApi.getItems().forEach(i -> {
            items.add(i);
            count.getAndIncrement();
            if (count.get() % batchSize == 0) {
                itemRepository.saveAll(items);
                items.clear();
                progressReporter.reportProgress(getJob(), count.get(), 5877);
            }
        });
        itemRepository.saveAll(items);
        return true;
    }
}
