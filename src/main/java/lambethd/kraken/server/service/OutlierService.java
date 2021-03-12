package lambethd.kraken.server.service;

import dto.ItemMovementDto;
import lambethd.kraken.server.interfaces.IOutlierService;
import lambethd.kraken.server.util.IGraphUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import portfolio.RangeType;
import runescape.Graph;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OutlierService implements IOutlierService {
    @Autowired
    private IGraphUtility graphUtility;

    private List<ItemMovementDto> getN(List<ItemMovementDto> dailyItemMovementDtos, int n, RangeType rangeType, boolean isTop, boolean byValue) {
        Comparator<ItemMovementDto> f = null;
        if (rangeType == RangeType.DAY) {
            if (byValue) {
                f = Comparator.comparing(ItemMovementDto::getDailyValueChange);
            } else {
                f = Comparator.comparing(ItemMovementDto::getDailyPercentageChange);
            }
        } else if (rangeType == RangeType.MONTH) {
            if (byValue) {
                f = Comparator.comparing(ItemMovementDto::getMonthlyValueChange);
            } else {
                f = Comparator.comparing(ItemMovementDto::getMonthlyPercentageChange);
            }
        }
        if (isTop) {
            f = f.reversed();
        }

        List<ItemMovementDto> collection = dailyItemMovementDtos.stream().filter(d ->
                d != null
                        && ((rangeType == RangeType.DAY && d.getDailyValueChange() != null && d.getDailyPercentageChange() != null)
                        || (rangeType == RangeType.MONTH && d.getMonthlyValueChange() != null && d.getMonthlyPercentageChange() != null)))
                .sorted(f).limit(n).collect(Collectors.toList());

        return collection;
    }

    @Override
    public List<ItemMovementDto> calculateItemMovements(List<Graph> graphs) {
        return graphs.stream().map(graph -> {
            ItemMovementDto itemMovementDto = new ItemMovementDto();

            Float today = graphUtility.getValueByDate(graph.daily, LocalDate.now().atStartOfDay());
            Float yesterday = graphUtility.getValueByDate(graph.daily, LocalDate.now().atStartOfDay().minusDays(1));
            Float lastMonth = graphUtility.getValueByDate(graph.daily, LocalDate.now().atStartOfDay().minusMonths(1));
            Float lastQuarter = graphUtility.getValueByDate(graph.daily, LocalDate.now().atStartOfDay().minusMonths(3));
            Float lastYear = graphUtility.getValueByDate(graph.daily, LocalDate.now().atStartOfDay().minusYears(1));

            if (today == null || lastMonth == null || yesterday == null || lastMonth == 0 || yesterday == 0) {
                return null;
            }
            itemMovementDto.setItemId(graph.id);
            itemMovementDto.setDailyValueChange((long) (today - yesterday));
            itemMovementDto.setDailyPercentageChange((today - yesterday) / yesterday * 100);
            itemMovementDto.setMonthlyValueChange((long) (today - lastMonth));
            itemMovementDto.setMonthlyPercentageChange((today - lastMonth) / lastMonth * 100);

            return itemMovementDto;
        })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemMovementDto> getTopNByValue(List<ItemMovementDto> itemMovementDtos, int n, RangeType rangeType) {
        return getN(itemMovementDtos, n, rangeType, true, true);
    }

    @Override
    public List<ItemMovementDto> getBottomNByValue(List<ItemMovementDto> itemMovementDtos, int n, RangeType rangeType) {
        return getN(itemMovementDtos, n, rangeType, false, true);
    }

    @Override
    public List<ItemMovementDto> getTopNByPercentage(List<ItemMovementDto> itemMovementDtos, int n, RangeType rangeType) {
        return getN(itemMovementDtos, n, rangeType, true, false);
    }

    @Override
    public List<ItemMovementDto> getBottomNByPercentage(List<ItemMovementDto> itemMovementDtos, int n, RangeType rangeType) {
        return getN(itemMovementDtos, n, rangeType, false, false);
    }
}
