package lambethd.kraken.server.service;

import domain.orchestration.IJob;
import domain.orchestration.Job;
import domain.orchestration.JobStatus;
import domain.orchestration.JobType;
import lambethd.kraken.data.mongo.repository.IGraphRepository;
import lambethd.kraken.data.mongo.repository.IItemRepository;
import lambethd.kraken.data.mongo.repository.IJobRepository;
import lambethd.kraken.resource.interfaces.IGraphApi;
import lambethd.kraken.resource.interfaces.IHistoricalDataApi;
import lambethd.kraken.resource.interfaces.IInfoApi;
import lambethd.kraken.resource.interfaces.IItemApi;
import lambethd.kraken.server.configuration.RepositorySetup;
import lambethd.kraken.server.job.JobCentralController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
public class Program {

    @Autowired
    private RepositorySetup repositorySetup;
    @Autowired
    private IInfoApi infoApi;
    @Autowired
    private IItemApi itemApi;
    @Autowired
    private IGraphApi graphApi;
    @Autowired
    private IHistoricalDataApi historicalDataApi;
    @Autowired
    private IItemRepository itemRepository;
    @Autowired
    private IGraphRepository graphRepository;
    @Autowired
    private IJobRepository jobRepository;
    @Autowired
    private JobCentralController jobCentralController;


    private void setup() {
        repositorySetup.setup();
    }

    @PostConstruct
    public void run() throws IOException {
        jobCentralController.init();
        jobCentralController.failedPreviouslyStartedJobs();
        jobCentralController.begin();
        IJob job = new Job();
        job.setStatus(JobStatus.Pending);
        job.setJobType(JobType.HistoricalDataLoader);
        job.setUsername("David");
        jobRepository.save(job);


//below here is historical load code
//        System.out.println("Beginning Load");
//        setup();
//        Date totalStart = Date.from(Instant.now());
//        infoApi.getInfo();
//
//        Date start = Date.from(Instant.now());
////        List<Item> items = itemApi.getItems();
//        List<Item> items = itemRepository.findAll();
//        Date end = Date.from(Instant.now());
//
//        System.out.println("Taken " + (end.getTime() - start.getTime()) + " to load items from RS");
////        itemRepository.saveAll(items);
//
//        items.parallelStream().forEach(item -> {
//            try {
//                Runeday currentRuneday = infoApi.getInfo();
//                Graph previousGraph = graphRepository.getGraphById(item.id);
//                if (previousGraph == null || previousGraph.lastUpdatedRuneDay != currentRuneday.lastConfigUpdateRuneday) {
//                    Date start1 = Date.from(Instant.now());
//                    String itemName = item.wikiName == null ? item.name : item.wikiName;
//                    List<HistoricalData> historicalData = historicalDataApi.getHistoricalData(itemName);
//                    Graph graph = new Graph();
//                    graph.id = item.id;
//                    graph.lastUpdatedRuneDay = currentRuneday.lastConfigUpdateRuneday;
//                    graph.daily = new ArrayList<>();
//                    for (HistoricalData historicalDatum : historicalData) {
//                        graph.daily.add(new Pair<>(historicalDatum.getDate(), historicalDatum.getPrice()));
//                    }
//                    graphRepository.save(graph);
//
//                    Date end1 = Date.from(Instant.now());
//
//                    System.out.println("Time taken for " + graph.daily.size() + " points for " + item.name + ": " + (end1.getTime() - start1.getTime()) + "ms");
//                }
//
//            } catch (NullPointerException npe) {
//                System.out.println("NPE: Failed on " + item.name + ", attempted to use " + item.name.replace(" ", "_"));
//                npe.printStackTrace();
//            } catch (RuntimeException e) {
//                System.out.println("RE: Failed on " + item.name + ", attempted to use " + item.name.replace(" ", "_"));
//                e.printStackTrace();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//        Date totalEnd = Date.from(Instant.now());
//        System.out.println("Time taken for everything: " + (totalEnd.getTime() - totalStart.getTime()));
//above here is historical load code


//        Runeday info = infoApi.getInfo();
//        System.out.println(info.lastConfigUpdateRuneday);
//
//        Date start = Date.from(Instant.now());
//        List<Item> items = itemApi.getItems();
//        Date end = Date.from(Instant.now());
//
//        System.out.println(end.getTime() - start.getTime());
//        itemRepository.saveAll(items);
//        Date start = Date.from(Instant.now());
//        List<Item> items = itemRepository.findAll();
//        for (Item item : items) {
//            Graph graph = graphApi.getGraph(item.id);
//            graphRepository.save(graph);
//        }
//        Date end = Date.from(Instant.now());
//        System.out.println(end.getTime() - start.getTime());
    }
}
