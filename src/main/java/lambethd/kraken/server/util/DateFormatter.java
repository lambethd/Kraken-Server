package lambethd.kraken.server.util;

import java.time.format.DateTimeFormatter;

public class DateFormatter {
    public static DateTimeFormatter dateFormatter() {
        return DateTimeFormatter.ofPattern("yyyy-mm-dd");
    }
}
