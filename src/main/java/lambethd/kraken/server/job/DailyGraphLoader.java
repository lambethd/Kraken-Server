package lambethd.kraken.server.job;

import domain.orchestration.JobType;
import lambethd.kraken.data.mongo.repository.IGraphRepository;
import lambethd.kraken.data.mongo.repository.IItemRepository;
import lambethd.kraken.resource.interfaces.IItemApi;
import lambethd.kraken.resource.interfaces.ILatestDataApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import runescape.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Service("DailyGraphLoader")
@Scope("prototype")
public class DailyGraphLoader extends JobProcessorBase {
    @Autowired
    private ProgressReporter progressReporter;
    @Autowired
    private ILatestDataApi latestDataApi;
    @Autowired
    private IItemRepository itemRepository;
    @Autowired
    private IGraphRepository graphRepository;

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
        List<Item> items = itemRepository.findAll();
        AtomicInteger count = new AtomicInteger();
        int totalItems = items.size();

        ConcurrentLinkedQueue<Graph> graphs = new ConcurrentLinkedQueue<>();
        items.parallelStream().forEach(item -> {
            try {
                HistoricalData latestData = latestDataApi.getLatestData(item.id);
                if(latestData != null) {
                    Graph graph = graphRepository.getGraphById(item.id);
                    if(graph != null && graph.daily != null) {
                        if (graph.daily.stream().noneMatch(i -> i.getKey().isEqual(latestData.getDate()))) {
                            graph.daily.add(new Pair<>(latestData.getDate(), latestData.getPrice()));
                            graphs.add(graph);
                        }
                    }
                }

                count.getAndIncrement();
                if(graphs.size() % batchSize == 0){
                    synchronized (this) {
                        graphRepository.saveAll(graphs);
                        graphs.clear();
                    }
                }
                if(count.get() % batchSize == 0){
                    progressReporter.reportProgress(getJob(), count.get(), totalItems);
                }
            } catch (IOException e) {
                logger.warn("Failed to add current graph data for itemId: " + item.id, e);
            }
        });
        graphRepository.saveAll(graphs);
        return true;
    }
}
