package lambethd.kraken.server.service.marketdata;

import lambethd.kraken.server.Configuration;
import lambethd.kraken.server.MongoConfiguration;
import lambethd.kraken.server.mongo.repository.IStockMarketDataRepository;
import lambethd.kraken.server.service.dto.StockMarketData;
import lambethd.kraken.server.service.mapper.AlphaVantageStockTimeSeriesMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Configuration.class, MongoConfiguration.class})
public class AlphaVantageMarketDataServiceTest {
    @Autowired
    private AlphaVantageMarketDataService marketDataService;
    @Autowired
    private AlphaVantageStockTimeSeriesMapper mapper;
    @Autowired
    private IStockMarketDataRepository stockMarketDataRepository;

    @Test
    public void getMarketData() {
        StockMarketData response = marketDataService.getDailyStockMarketData("GOOGL");
        stockMarketDataRepository.insert(response);
    }
}