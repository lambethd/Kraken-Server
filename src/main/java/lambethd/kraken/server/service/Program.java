package lambethd.kraken.server.service;

import lambethd.kraken.data.mongo.repository.IGraphRepository;
import lambethd.kraken.data.mongo.repository.IItemRepository;
import lambethd.kraken.resource.interfaces.IGraphApi;
import lambethd.kraken.resource.interfaces.IInfoApi;
import lambethd.kraken.resource.interfaces.IItemApi;
import lambethd.kraken.server.configuration.RepositorySetup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import runescape.Graph;
import runescape.Runeday;
import runescape.Item;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.Instant;
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
    private IItemRepository itemRepository;
    @Autowired
    private IGraphRepository graphRepository;


    private void setup() {
        repositorySetup.setup();
    }

    @PostConstruct
    public void run() throws IOException {
        setup();
        Runeday info = infoApi.getInfo();
        System.out.println(info.lastConfigUpdateRuneday);

//        Date start = Date.from(Instant.now());
//        List<Item> items = itemApi.getItems();
//        Date end = Date.from(Instant.now());
//
//        System.out.println(end.getTime() - start.getTime());
//        itemRepository.saveAll(items);
        Date start = Date.from(Instant.now());
        List<Item> items = itemRepository.findAll();
        for (Item item : items) {
            Graph graph = graphApi.getGraph(item.id);
            graphRepository.save(graph);
        }
        Date end = Date.from(Instant.now());
        System.out.println(end.getTime() - start.getTime());
    }
}
