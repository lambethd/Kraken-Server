package lambethd.kraken.server.service.marketdata;

import java.util.HashMap;

public class AlphaVantageMetadataKeys {
    private static HashMap<String, String> nameMap = new HashMap<String, String>() {{
        put("1. Information", "Information");
        put("2. Symbol", "Symbol");
        put("3. Last Refreshed", "LastRefreshed");
        put("4. Output Size", "OutputSize");
        put("5. Time Zone", "TimeZone");
        put("1. open", "Open");
        put("2. high", "High");
        put("3. low", "Low");
        put("4. close", "Close");
        put("5. volume", "Volume");
    }};

    public static String getCleanName(String dirtyName) {
        return nameMap.get(dirtyName);
    }
}
