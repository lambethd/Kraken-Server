package lambethd.kraken.server.integration.orchestration;

import lambethd.kraken.server.Configuration;
import lambethd.kraken.server.service.Program;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Configuration.class})
public class KafkaOrchestrationTests {
    @Autowired
    private Program program;

    @Test
    public void TestKafkaSend() {

    }
}
