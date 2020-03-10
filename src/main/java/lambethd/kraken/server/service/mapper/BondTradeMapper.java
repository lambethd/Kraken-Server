package lambethd.kraken.server.service.mapper;

import lambethd.kraken.server.service.dto.TradeFields;
import lambethd.kraken.server.service.dto.BondTrade;
import lambethd.kraken.server.service.dto.Trade;
import lambethd.kraken.server.service.dto.TradeType;
import lambethd.kraken.server.util.DateFormatter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BondTradeMapper implements ITradeMapper {

    @Override
    public Trade map(String tradeString) {
        BondTrade trade = new BondTrade();
        String[] splitString = tradeString.split(",");
        trade.setBondId(splitString[TradeFields.bondId]);
        trade.setMaturityDate(LocalDateTime.parse(splitString[TradeFields.maturityDate], DateFormatter.dateFormatter()));
        trade.setCoupon(Double.parseDouble(splitString[TradeFields.coupon]));
        trade.setNominal(Double.parseDouble(splitString[TradeFields.nominal]));
        return trade;
    }

    @Override
    public boolean canMap(TradeType tradeType) {
        return tradeType == TradeType.Bond;
    }
}
