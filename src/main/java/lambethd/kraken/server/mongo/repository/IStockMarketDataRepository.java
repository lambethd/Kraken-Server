package lambethd.kraken.server.mongo.repository;

import lambethd.kraken.server.service.dto.StockMarketData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IStockMarketDataRepository extends MongoRepository<StockMarketData, String> {
}
