package lambethd.kraken.server.service.dto;

import lambethd.kraken.server.service.domain.RiskFactorType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Date;

@Document
public class RiskFactor {
    @Id
    public String id;
    public RiskFactorType riskFactorType;
    public TradeType tradeType;
    public String tradeIdentifier;
    public double value;
    public Date calculationDate;

    public RiskFactor() {
        calculationDate = Date.from(Instant.now());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RiskFactorType getRiskFactorType() {
        return riskFactorType;
    }

    public void setRiskFactorType(RiskFactorType riskFactorType) {
        this.riskFactorType = riskFactorType;
    }

    public TradeType getTradeType() {
        return tradeType;
    }

    public void setTradeType(TradeType tradeType) {
        this.tradeType = tradeType;
    }

    public String getTradeIdentifier() {
        return tradeIdentifier;
    }

    public void setTradeIdentifier(String tradeIdentifier) {
        this.tradeIdentifier = tradeIdentifier;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Date getCalculationDate() {
        return calculationDate;
    }

    public void setCalculationDate(Date calculationDate) {
        this.calculationDate = calculationDate;
    }
}
