package lambethd.kraken.server.service.processor;

import lambethd.kraken.server.mongo.repository.ITradeRepository;
import lambethd.kraken.server.service.dto.Trade;
import lambethd.kraken.server.service.enrich.TradeEnricherService;
import lambethd.kraken.server.service.mapper.TradeMapperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TradeProcessor implements IProcessor {

    @Autowired
    private TradeMapperService tradeMapperService;

    @Autowired
    private ITradeRepository tradeRepository;

    @Autowired
    private TradeEnricherService tradeEnricherService;

    private Logger logger = LoggerFactory.getLogger(TradeProcessor.class);

    public void process(String tradeString) {
        Trade trade;
        try {
            trade = tradeMapperService.map(tradeString);
        } catch (Exception e) {
            logger.error("Could not map trade '" + tradeString + "'", e);
            return;
        }
        tradeRepository.insert(trade);
        tradeEnricherService.enrich(trade);
    }
}
