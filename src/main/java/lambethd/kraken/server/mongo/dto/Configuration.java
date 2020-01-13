package lambethd.kraken.server.mongo.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Configuration {
    @Id
    private String id;

    private String key;

    private String value;

    public Configuration() {
    }

    public Configuration(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public Configuration(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }
}
