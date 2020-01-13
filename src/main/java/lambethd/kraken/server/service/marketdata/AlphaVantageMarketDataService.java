package lambethd.kraken.server.service.marketdata;

import lambethd.kraken.server.service.dto.StockMarketData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlphaVantageMarketDataService implements IMarketDataService {

    @Autowired
    private AlphaVantageRestClient restClient;

    @Override
    public StockMarketData getDailyStockMarketData(String symbol) {
        return restClient.getMarketDataString(symbol, AlphaVantageFunction.TIME_SERIES_DAILY.toString());
    }
}
