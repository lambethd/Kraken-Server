package lambethd.kraken.server.configuration;


import lambethd.kraken.data.mongo.repository.IConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RepositorySetup {

    @Autowired
    private IConfigRepository configRepository;

    public void setup(){
        configRepository.deleteAll();
    }
}
