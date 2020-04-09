package lambethd.kraken.server.service.marketdata;

import dto.StockMarketDataDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlphaVantageMarketDataService implements IMarketDataService {

    @Autowired
    private AlphaVantageRestClient restClient;

    @Override
    public StockMarketDataDto getDailyStockMarketData(String symbol) {
        return restClient.getMarketDataString(symbol, AlphaVantageFunction.TIME_SERIES_DAILY.toString());
    }
}
