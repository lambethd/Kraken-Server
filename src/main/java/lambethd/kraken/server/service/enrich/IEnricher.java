package lambethd.kraken.server.service.enrich;

public interface IEnricher {
    Trade enrich(Trade trade);
    Boolean canEnrich(Trade trade);
}
