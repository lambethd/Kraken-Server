package lambethd.kraken.server.mongo.repository;

import lambethd.kraken.server.mongo.dto.Configuration;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IConfigRepository extends MongoRepository<Configuration, String> {
}
