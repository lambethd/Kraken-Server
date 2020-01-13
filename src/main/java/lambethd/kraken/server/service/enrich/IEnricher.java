package lambethd.kraken.server.service.enrich;

import lambethd.kraken.server.service.dto.Trade;

public interface IEnricher {
    Trade enrich(Trade trade);
    Boolean canEnrich(Trade trade);
}
