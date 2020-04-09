package lambethd.kraken.server.service.marketdata;

import dto.StockMarketDataDto;

public interface IMarketDataService {
    StockMarketDataDto getDailyStockMarketData(String symbol);
}
