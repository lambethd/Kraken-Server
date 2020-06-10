package lambethd.kraken.server.service;

import lambethd.kraken.data.mongo.repository.IGraphRepository;
import lambethd.kraken.data.mongo.repository.IItemRepository;
import lambethd.kraken.resource.interfaces.IGraphApi;
import lambethd.kraken.resource.interfaces.IHistoricalDataApi;
import lambethd.kraken.resource.interfaces.IInfoApi;
import lambethd.kraken.resource.interfaces.IItemApi;
import lambethd.kraken.server.configuration.RepositorySetup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import runescape.*;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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


    private void setup() {
        repositorySetup.setup();
    }

    @PostConstruct
    public void run() throws IOException {
        setup();
        Date totalStart = Date.from(Instant.now());
        infoApi.getInfo();

        List<Item> items = itemRepository.findAll();
        items.parallelStream().forEach(item -> {
            try {
                Runeday currentRuneday = infoApi.getInfo();
                if (graphRepository.getGraphById(item.id).lastUpdatedRuneDay != currentRuneday.lastConfigUpdateRuneday) {
                    Date start = Date.from(Instant.now());
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

                    Date end = Date.from(Instant.now());

                    System.out.println("Time taken for " + graph.daily.size() + " points for " + item.name + ": " + (end.getTime() - start.getTime()) + "ms");
                }

            } catch (NullPointerException npe) {
                System.out.println("NPE: Failed on " + item.name + ", attempted to use " + item.name.replace(" ", "_"));
            } catch (RuntimeException e) {
                System.out.println("RE: Failed on " + item.name + ", attempted to use " + item.name.replace(" ", "_"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        Date totalEnd = Date.from(Instant.now());
        System.out.println("Time taken for everything: " + (totalEnd.getTime() - totalStart.getTime()));

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
