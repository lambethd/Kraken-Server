package lambethd.kraken.server.service;

import lambethd.kraken.server.configuration.RepositorySetup;
import lambethd.kraken.server.kafka.ConsumerCreator;
import lambethd.kraken.server.kafka.IKafkaConstants;
import lambethd.kraken.server.service.processor.TradeProcessor;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Duration;

@Service
public class Program {

    @Autowired
    private TradeProcessor tradeProcessor;

    @Autowired
    private RepositorySetup repositorySetup;

    private void setup(){
        repositorySetup.setup();
    }

    @PostConstruct
    public void run(){
        setup();
        Consumer<Long, String> consumer = ConsumerCreator.createConsumer();

        int noMessageFound = 0;

        while (true) {
            ConsumerRecords<Long, String> consumerRecords = consumer.poll(Duration.ofSeconds(10));
            if (consumerRecords.count() == 0) {
                noMessageFound++;
                if (noMessageFound > IKafkaConstants.MAX_NO_MESSAGE_FOUND_COUNT) {
                    // If no message found count is reached to threshold exit loop.
                    System.out.println("No messages found, exiting");
                    break;
                } else
                    continue;
            }
            //print each record.
            consumerRecords.forEach(record -> tradeProcessor.process(record.value()));
            // commits the offset of record to broker.
            consumer.commitAsync();
        }
        consumer.close();
    }
}
