package lambethd.kraken.server.service.configuration;

import dto.ConfigurationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class ConfigurationService {
    @Autowired
    private IConfigRepository configRepository;

    public ConfigurationDto getConfigItem(String configKey) {
        Optional<ConfigurationDto> option = configRepository.findOne(Example.of(new ConfigurationDto(configKey)));
        return option.orElse(null);
    }
}
