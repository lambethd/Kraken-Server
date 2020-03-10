package lambethd.kraken.server.service.processor.message;

import lambethd.kraken.server.service.processor.TradeProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Service
public class TradeMessageProcessor extends MessageProcessorBase {

    @Autowired
    private TradeProcessor tradeProcessor;
    private int NUMBER_OF_THREADS = 10;

    @Override
    public void ProcessMessage(String message) {
        Executor exec = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        exec.execute(() -> tradeProcessor.process(message));
        return tradeProcessor.process;
    }

    @Override
    MessageType GetApplicableMessageType() {
        return MessageType.Trade;
    }
}
