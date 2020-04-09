package lambethd.kraken.server.service.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import domain.TimeSerie;
import dto.StockMarketDataDto;
import kafka.utils.Json;
import kafka.utils.json.JsonValue;
import lambethd.kraken.server.service.marketdata.AlphaVantageMetadataKeys;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class AlphaVantageStockTimeSeriesMapper {

    public StockMarketDataDto map(String marketDataString) {
        JsonValue value = Json.parseFull(marketDataString).get();
        JsonNode node = value.node();
        JsonNode metadataNode = node.get("Meta Data");
        JsonNode timeSeriesNode = node.get("Time Series (Daily)");

        StockMarketDataDto stockMarketData = new StockMarketDataDto();
        stockMarketData.setMetaData(getMetaData(metadataNode));
        stockMarketData.setTimeSeries(getTimeSeries(timeSeriesNode));
        return stockMarketData;
    }

    private HashMap<String, String> getMetaData(JsonNode metadataNode) {
        HashMap<String, String> metadata = new HashMap<>();
        metadataNode.fieldNames().forEachRemaining((String fieldName) -> {
            metadata.put(AlphaVantageMetadataKeys.getCleanName(fieldName), metadataNode.get(fieldName).textValue());
        });
        return metadata;
    }

    private List<TimeSerie> getTimeSeries(JsonNode timeSeriesNode) {
        List<TimeSerie> timeSeries = new ArrayList<>();
        timeSeriesNode.fieldNames().forEachRemaining((String fieldName) -> {
            TimeSerie timeSerie = new TimeSerie();
            try {
                timeSerie.setTimeSerie(new SimpleDateFormat("yyyy-MM-dd").parse(fieldName));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            JsonNode single = timeSeriesNode.get(fieldName);
            single.fieldNames().forEachRemaining((String singleField) ->
            {
                timeSerie.getDetails().put(AlphaVantageMetadataKeys.getCleanName(singleField), single.get(singleField).textValue());
            });
            timeSeries.add(timeSerie);
        });
        return timeSeries;
    }
}
