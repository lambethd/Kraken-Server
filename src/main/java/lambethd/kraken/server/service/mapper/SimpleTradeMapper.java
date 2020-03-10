package lambethd.kraken.server.service.mapper;

import lambethd.kraken.server.service.dto.TradeFields;
import lambethd.kraken.server.service.dto.TradeType;
import lambethd.kraken.server.service.dto.Trade;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;

@Service
public class SimpleTradeMapper implements ITradeMapper {

    public Trade map(String tradeString) {
        String[] tradeFields = tradeString.split(",");
        Trade trade = new Trade();
        try {
            trade.setId(tradeFields[TradeFields.id]);
            trade.setTradeType(TradeType.valueOf(tradeFields[TradeFields.tradeType]));
            trade.setPurchaseDate(new SimpleDateFormat("yyyy-MM-dd").parse(tradeFields[TradeFields.purchaseDate]));
        } catch (Exception e) {
            System.out.println("Could not map simple trade");
            System.out.println(e.getMessage());
        }
        return trade;
    }

    @Override
    public boolean canMap(TradeType tradeType) {
        return false;
    }
}
