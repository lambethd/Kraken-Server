package lambethd.kraken.server.service.processor.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class MessageProcessorService {
    @Autowired
    private IMessageProcessor[] messageProcessors;

    public void processMessage(String message, MessageType messageType){
        Arrays.stream(messageProcessors).filter(processor->processor.CanProcess(messageType)).findFirst().get().ProcessMessage(message);
    }
}
