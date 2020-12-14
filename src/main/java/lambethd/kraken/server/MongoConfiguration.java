package lambethd.kraken.server;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoClientURI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "lambethd.kraken.data.mongo.repository")
public class MongoConfiguration extends AbstractMongoConfiguration {

    @Value("${spring.data.mongodb.uri}")
    public String uri;

    @Override
    protected String getDatabaseName() {
        return "kraken";
    }

    @Override
    public MongoClient mongoClient() {
        return new MongoClient(new MongoClientURI(uri));
    }

    @Override
    protected String getMappingBasePackage() {
        return "org.baeldung";
    }
}