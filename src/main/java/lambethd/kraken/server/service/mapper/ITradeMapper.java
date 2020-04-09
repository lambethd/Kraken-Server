package lambethd.kraken.server.service.mapper;

import domain.TradeType;
import dto.TradeDto;

public interface ITradeMapper {
    TradeDto map(String tradeString);
    boolean canMap(TradeType tradeType);
}
