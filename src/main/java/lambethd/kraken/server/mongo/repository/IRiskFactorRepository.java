package lambethd.kraken.server.mongo.repository;

import lambethd.kraken.server.service.dto.RiskFactor;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IRiskFactorRepository extends MongoRepository<RiskFactor, String> {
}
