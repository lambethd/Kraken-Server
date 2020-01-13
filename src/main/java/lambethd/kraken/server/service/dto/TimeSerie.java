package lambethd.kraken.server.service.dto;

import java.util.Date;
import java.util.HashMap;

public class TimeSerie {
    private Date timeSerie;
    private HashMap<String, String> details;

    public TimeSerie() {
        details = new HashMap<>();
    }

    public HashMap<String, String> getDetails() {
        return details;
    }

    public void setDetails(HashMap<String, String> details) {
        this.details = details;
    }

    public Date getTimeSerie() {
        return timeSerie;
    }

    public void setTimeSerie(Date timeSerie) {
        this.timeSerie = timeSerie;
    }
}
