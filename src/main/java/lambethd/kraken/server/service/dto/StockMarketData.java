package lambethd.kraken.server.service.dto;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class StockMarketData {
    private HashMap MetaData;
    private List<TimeSerie> timeSeries;

    public HashMap getMetaData() {
        return MetaData;
    }

    public void setMetaData(HashMap metaData) {
        MetaData = metaData;
    }

    public List<TimeSerie> getTimeSeries() {
        return timeSeries;
    }

    public void setTimeSeries(List<TimeSerie> timeSeries) {
        this.timeSeries = timeSeries;
    }

    public TimeSerie getMostRecentTimeSerie() {
        timeSeries.sort(Comparator.comparing(TimeSerie::getTimeSerie));
        return timeSeries.get(0);
    }
}
