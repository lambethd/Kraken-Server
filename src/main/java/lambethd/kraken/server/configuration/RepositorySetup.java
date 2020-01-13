package lambethd.kraken.server.configuration;

import lambethd.kraken.server.mongo.dto.Configuration;
import lambethd.kraken.server.mongo.repository.IConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RepositorySetup {

    @Autowired
    private IConfigRepository configRepository;

    public void setup(){
        configRepository.deleteAll();
        configRepository.insert(new Configuration(ConfigurationKey.ALPHA_VANTAGE_API_KEY_KEY, "0HRXI8NK56CCK3OQ"));
        configRepository.insert(new Configuration(ConfigurationKey.ALPHA_VANTAGE_URL_KEY, "https://www.alphavantage.co/query?function={function}&symbol={symbol}&interval=5min&apikey={apiKey}"));
    }
}
