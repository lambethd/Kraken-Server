package lambethd.kraken.server.service.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class TradeMapperService {

    @Autowired
    private SimpleTradeMapper simpleTradeMapper;

    @Autowired
    private ITradeMapper[] mappers;

    public Trade map(String tradeString) {
        Trade simpleMapped = simpleTradeMapper.map(tradeString);
        ITradeMapper mapper = Arrays.stream(mappers).filter(m -> m.canMap(simpleMapped.getTradeType())).findFirst().orElse(simpleTradeMapper);
        return mapper.map(tradeString);
    }
}
