package lambethd.kraken.server.service.marketdata;

import lambethd.kraken.server.service.dto.StockMarketData;

public interface IMarketDataService {
    StockMarketData getDailyStockMarketData(String symbol);
}
