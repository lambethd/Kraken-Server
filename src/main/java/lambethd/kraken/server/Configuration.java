package lambethd.kraken.server;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@org.springframework.context.annotation.Configuration
@ComponentScan({"lambethd.kraken.server", "lambethd.kraken."})
@EnableScheduling
public class Configuration {

}
