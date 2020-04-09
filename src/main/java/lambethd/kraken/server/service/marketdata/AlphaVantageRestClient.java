package lambethd.kraken.server.service.marketdata;

import dto.StockMarketDataDto;
import lambethd.kraken.server.configuration.ConfigurationKey;
import lambethd.kraken.server.service.configuration.ConfigurationService;
import lambethd.kraken.server.service.mapper.AlphaVantageStockTimeSeriesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.ws.http.HTTPException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class AlphaVantageRestClient {

    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private AlphaVantageStockTimeSeriesMapper mapper;

    private Logger logger = LoggerFactory.getLogger(AlphaVantageRestClient.class);

    public StockMarketDataDto getMarketDataString(String symbol, String function) {
        String urlString = configurationService.getConfigItem(ConfigurationKey.ALPHA_VANTAGE_URL_KEY).getValue();
        String apiKey = configurationService.getConfigItem(ConfigurationKey.ALPHA_VANTAGE_API_KEY_KEY).getValue();
        urlString = urlString.replace("{function}", function);
        urlString = urlString.replace("{symbol}", symbol);
        urlString = urlString.replace("{apiKey}", apiKey);
        URL url;
        StringBuffer content = null;
        try {
            url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");

            int status = con.getResponseCode();
            if (status != 200) {
                logger.error("Could not get market data from AlphaVantage with status: " + status);
                throw new HTTPException(status);
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();
        } catch (
                IOException e) {
            e.printStackTrace();
        }
        return mapper.map(content != null ? content.toString() : null);
    }
}
