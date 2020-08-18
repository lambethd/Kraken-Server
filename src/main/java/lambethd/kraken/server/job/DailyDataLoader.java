package lambethd.kraken.server.job;

import domain.orchestration.JobType;
import lambethd.kraken.data.mongo.repository.IItemRepository;
import lambethd.kraken.resource.interfaces.IItemApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import runescape.Item;

import java.io.IOException;
import java.util.List;

@Service("DailyDataLoader")
@Scope("prototype")
public class DailyDataLoader extends JobProcessorBase {
    @Autowired
    private ProgressReporter progressReporter;
    @Autowired
    private IItemApi itemApi;
    @Autowired
    private IItemRepository itemRepository;

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
        List<Item> items = itemApi.getItems();
        itemRepository.saveAll(items);
        return true;
    }
}
