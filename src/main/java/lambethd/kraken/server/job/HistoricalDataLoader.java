package lambethd.kraken.server.job;

import domain.orchestration.JobType;
import lambethd.kraken.data.mongo.repository.IGraphRepository;
import lambethd.kraken.data.mongo.repository.IItemRepository;
import lambethd.kraken.resource.interfaces.IHistoricalDataApi;
import lambethd.kraken.resource.interfaces.IInfoApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import runescape.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    public Boolean call() {
        List<Item> items = itemRepository.findAll();

        items.parallelStream().forEach(item -> {
            try {
                Runeday currentRuneday = infoApi.getInfo();
                Graph previousGraph = graphRepository.getGraphById(item.id);
                if (previousGraph == null || previousGraph.lastUpdatedRuneDay != currentRuneday.lastConfigUpdateRuneday) {
                    Date start1 = Date.from(Instant.now());
                    String itemName = item.wikiName == null ? item.name : item.wikiName;
                    List<HistoricalData> historicalData = historicalDataApi.getHistoricalData(itemName);
                    Graph graph = new Graph();
                    graph.id = item.id;
                    graph.lastUpdatedRuneDay = currentRuneday.lastConfigUpdateRuneday;
                    graph.daily = new ArrayList<>();
                    for (HistoricalData historicalDatum : historicalData) {
                        graph.daily.add(new Pair<>(historicalDatum.getDate(), historicalDatum.getPrice()));
                    }
                    graphRepository.save(graph);

                    Date end1 = Date.from(Instant.now());

                    logger.debug("Time taken for " + graph.daily.size() + " points for " + item.name + ": " + (end1.getTime() - start1.getTime()) + "ms");
                }
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
