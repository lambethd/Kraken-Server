package lambethd.kraken.server.service.configuration;

import lambethd.kraken.server.mongo.dto.Configuration;
import lambethd.kraken.server.mongo.repository.IConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class ConfigurationService {
    @Autowired
    private IConfigRepository configRepository;

    public Configuration getConfigItem(String configKey) {
        Optional<Configuration> option = configRepository.findOne(Example.of(new Configuration(configKey)));
        return option.orElse(null);
    }
}
