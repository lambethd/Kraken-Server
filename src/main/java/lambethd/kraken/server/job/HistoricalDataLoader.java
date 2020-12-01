package lambethd.kraken.server.job;

import domain.orchestration.JobType;
import lambethd.kraken.data.mongo.repository.IGraphRepository;
import lambethd.kraken.data.mongo.repository.IItemRepository;
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
    private IItemRepository itemRepository;
    @Autowired
    private IInfoApi infoApi;
    @Autowired
    private IGraphRepository graphRepository;
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
        List<Item> items = itemRepository.findAll();
        int totalCount = items.size();
        AtomicInteger count = new AtomicInteger();
        List<Graph> batch = new LinkedList<>();
        Runeday currentRuneDay = null;
        try {
            currentRuneDay = infoApi.getInfo();
        } catch (IOException e) {
            logger.debug("Some other error: ", e);
            return false;
        }

        Runeday finalCurrentRuneDay = currentRuneDay;
        items.forEach(item -> {
            try {
                Date start1 = Date.from(Instant.now());
                String itemName = item.wikiName == null ? item.name : item.wikiName;
                List<HistoricalData> historicalData = historicalDataApi.getHistoricalData(itemName);
                Graph graph = new Graph();
                graph.id = item.id;
                graph.lastUpdatedRuneDay = finalCurrentRuneDay.lastConfigUpdateRuneday;
                graph.daily = new ArrayList<>();
                for (HistoricalData historicalDatum : historicalData) {
                    graph.daily.add(new Pair<>(historicalDatum.getDate(), historicalDatum.getPrice()));
                }
                batch.add(graph);

                Date end1 = Date.from(Instant.now());
                count.getAndIncrement();

                if (count.get() % batchSize == 0 || count.get() == totalCount) {
                    graphRepository.saveAll(batch);
                    progressReporter.reportProgress(getJob(), count.get(), totalCount);
                    batch.clear();
                }
                logger.debug("Time taken for " + graph.daily.size() + " points for " + item.name + ": " + (end1.getTime() - start1.getTime()) + "ms");
            } catch (NullPointerException e) {
                logger.debug("NPE: Failed on " + item.name + ", attempted to use " + item.name.replace(" ", "_"), e);
            } catch (RuntimeException e) {
                logger.debug("RE: Failed on " + item.name + ", attempted to use " + item.name.replace(" ", "_"), e);
            } catch (Exception e) {
                logger.debug("Some other error: ", e);
            }
        });

        return true;
    }
}
