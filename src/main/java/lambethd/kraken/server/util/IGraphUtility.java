package lambethd.kraken.server.util;

import runescape.Pair;

import java.time.LocalDateTime;
import java.util.List;

public interface IGraphUtility {
    Float getValueByDate(List<Pair<LocalDateTime, Float>> graph, LocalDateTime date);

    Float getValueByOffset(List<Pair<LocalDateTime, Float>> graph, int offset);
}
