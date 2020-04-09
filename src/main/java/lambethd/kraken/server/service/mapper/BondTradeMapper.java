package lambethd.kraken.server.service.mapper;

import domain.TradeType;
import dto.BondTradeDto;
import dto.TradeDto;
import lambethd.kraken.server.service.dto.TradeFields;
import lambethd.kraken.server.util.DateFormatter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BondTradeMapper implements ITradeMapper {

    @Override
    public TradeDto map(String tradeString) {
        BondTradeDto trade = new BondTradeDto();
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
