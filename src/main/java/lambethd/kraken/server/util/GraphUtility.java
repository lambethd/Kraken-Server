package lambethd.kraken.server.util;

import org.springframework.stereotype.Service;
import runescape.Pair;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class GraphUtility implements IGraphUtility {
    @Override
    public Float getValueByDate(List<Pair<LocalDateTime, Float>> graph, LocalDateTime date) {
        graph.sort(Comparator.comparing(Pair::getKey));
        Optional<Pair<LocalDateTime, Float>> result = graph.stream().filter(i -> i.getKey() == date).findFirst();
        return result.map(Pair::getValue).orElse(null);
    }

    @Override
    public Float getValueByOffset(List<Pair<LocalDateTime, Float>> graph, int offset) {
        graph.sort(Comparator.comparing(Pair::getKey));
        return graph.get(offset).getValue();
    }
}

