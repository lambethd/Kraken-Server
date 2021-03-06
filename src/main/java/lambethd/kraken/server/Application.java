package lambethd.kraken.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "lambethd.kraken")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
