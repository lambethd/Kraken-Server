package lambethd.kraken.server.mongo.repository;

import lambethd.kraken.server.service.dto.Trade;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ITradeRepository extends MongoRepository<Trade, String> {
}
