package lambethd.kraken.server.mongo.repository;

import lambethd.kraken.server.service.dto.StockTrade;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IStockTradeRepository extends MongoRepository<StockTrade, String> {
}
