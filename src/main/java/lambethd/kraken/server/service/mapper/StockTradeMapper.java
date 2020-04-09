package lambethd.kraken.server.service.mapper;

import lambethd.kraken.server.service.dto.TradeFields;
import org.springframework.stereotype.Service;

@Service
public class StockTradeMapper implements ITradeMapper {
    @Override
    public Trade map(String tradeString) {
        StockTrade trade = new StockTrade();
        String[] split = tradeString.split(",");
        trade.setId(split[TradeFields.id]);
        trade.setTradeType(TradeType.valueOf(split[TradeFields.tradeType]));
        trade.setSymbol(split[TradeFields.stockId]);
        trade.setBuyPrice(Double.parseDouble(split[TradeFields.buyPrice]));
        return trade;
    }

    @Override
    public boolean canMap(TradeType tradeType) {
        return tradeType == TradeType.Stock;
    }
}
