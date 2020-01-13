package lambethd.kraken.server.service.builder;

import lambethd.kraken.server.service.dto.Trade;
import lambethd.kraken.server.service.dto.TradeType;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TradeBuilder {
    private String id;
    private TradeType tradeType;
    private Date purchaseDate;

    public TradeBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public TradeBuilder withTradeType(TradeType tradeType) {
        this.tradeType = tradeType;
        return this;
    }

    public TradeBuilder withPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
        return this;
    }

    public Trade build() {
        Trade trade = new Trade();
        trade.setId(id);
        trade.setTradeType(tradeType);
        trade.setPurchaseDate(purchaseDate);
        return trade;
    }
}
