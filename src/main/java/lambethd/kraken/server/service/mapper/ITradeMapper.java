package lambethd.kraken.server.service.mapper;

import lambethd.kraken.server.service.dto.Trade;
import lambethd.kraken.server.service.dto.TradeType;

public interface ITradeMapper {
    Trade map(String tradeString);
    boolean canMap(TradeType tradeType);
}
