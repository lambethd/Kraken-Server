package lambethd.kraken.server.service.enrich;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class TradeEnricherService {
    @Autowired
    private IEnricher[] enrichers;

    public Trade enrich(Trade trade) {
        IEnricher enricher = Arrays.stream(enrichers).filter(m -> m.canEnrich(trade)).findFirst().orElse(null);
        return enricher == null ? trade : enricher.enrich(trade);
    }
}
