package lambethd.kraken.server.service.enrich;

import lambethd.kraken.server.mongo.repository.IRiskFactorRepository;
import lambethd.kraken.server.mongo.repository.IStockMarketDataRepository;
import lambethd.kraken.server.mongo.repository.IStockTradeRepository;
import lambethd.kraken.server.service.domain.RiskFactorType;
import lambethd.kraken.server.service.dto.*;
import lambethd.kraken.server.service.marketdata.IMarketDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockTradeEnricher implements IEnricher {
    @Autowired
    private IStockTradeRepository stockTradeRepository;
    @Autowired
    private IMarketDataService marketDataService;
    @Autowired
    private IStockMarketDataRepository stockMarketDataRepository;
    @Autowired
    private IRiskFactorRepository riskFactorRepository;

    @Override
    public Trade enrich(Trade trade) {
        StockTrade stock = (StockTrade) trade;
        StockMarketData marketData = marketDataService.getDailyStockMarketData(stock.getSymbol());
        stockMarketDataRepository.insert(marketData);
        RiskFactor superSimple = new RiskFactor();
        superSimple.riskFactorType = RiskFactorType.SUPER_SIMPLE;
        superSimple.tradeType = TradeType.Stock;
        superSimple.tradeIdentifier = stock.getId();
        superSimple.value = stock.getBuyPrice() - Double.parseDouble(marketData.getMostRecentTimeSerie().getDetails().get("Open"));
        riskFactorRepository.insert(superSimple);
        //TODO: some enrichment here based on the market data?

        return stock;
    }

    @Override
    public Boolean canEnrich(Trade trade) {
        return trade.getTradeType() == TradeType.Stock;
    }
}
