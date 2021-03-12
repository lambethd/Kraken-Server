package lambethd.kraken.server.interfaces;

import dto.ItemMovementDto;
import portfolio.RangeType;
import runescape.Graph;

import java.util.List;

public interface IOutlierService {
    List<ItemMovementDto> calculateItemMovements(List<Graph> graphs);
    List<ItemMovementDto> getTopNByValue(List<ItemMovementDto> itemMovementDtos, int n, RangeType rangeType);

    List<ItemMovementDto> getBottomNByValue(List<ItemMovementDto> itemMovementDtos, int n, RangeType rangeType);

    List<ItemMovementDto> getTopNByPercentage(List<ItemMovementDto> itemMovementDtos, int n, RangeType rangeType);

    List<ItemMovementDto> getBottomNByPercentage(List<ItemMovementDto> itemMovementDtos, int n, RangeType rangeType);
}
