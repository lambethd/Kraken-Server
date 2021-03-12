package lambethd.kraken.server.service;

import dto.ItemMovementDto;
import lambethd.kraken.server.util.GraphUtility;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import portfolio.RangeType;
import runescape.Graph;
import runescape.Pair;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OutlierServiceTest {

    @Mock
    private GraphUtility graphUtilityMock;
    @InjectMocks
    private OutlierService undertest;

    @Before
    public void setUp() throws Exception {
        when(graphUtilityMock.getValueByDate(any(), any())).thenCallRealMethod();
    }

    @Test
    public void getTopNByValue() {
        Graph graph1 = createGraph(1);
        Graph graph2 = createGraph(2);
        Graph graph3 = createGraph(3);

        List<Graph> graphs = Arrays.asList(graph1, graph2, graph3);
        List<ItemMovementDto> itemMovements = undertest.calculateItemMovements(graphs);

        List<ItemMovementDto> result = undertest.getTopNByValue(itemMovements, 1, RangeType.DAY);
        assertNotNull(result);
        assertEquals("ID should be 3", Integer.valueOf(3), result.get(0).getItemId());
    }

    @Test
    public void getBottomNByValue() {
        Graph graph1 = createGraph(1);
        Graph graph2 = createGraph(2);
        Graph graph3 = createGraph(3);

        List<Graph> graphs = Arrays.asList(graph1, graph2, graph3);
        List<ItemMovementDto> itemMovements = undertest.calculateItemMovements(graphs);

        List<ItemMovementDto> result = undertest.getBottomNByValue(itemMovements, 1, RangeType.DAY);
        assertNotNull(result);
        assertEquals("ID should be 1", Integer.valueOf(1), result.get(0).getItemId());
    }

    @Test
    public void getTopNByPercentage() {
        Graph graph1 = createGraph(1);
        Graph graph2 = createGraph(2);
        Graph graph3 = createGraph(3);

        graph3.daily.get(0).setValue(graph3.daily.get(0).getValue() * 10);

        List<Graph> graphs = Arrays.asList(graph1, graph2, graph3);
        List<ItemMovementDto> itemMovements = undertest.calculateItemMovements(graphs);

        List<ItemMovementDto> result = undertest.getTopNByPercentage(itemMovements, 1, RangeType.DAY);
        assertNotNull(result);
        assertEquals("ID should be 3", Integer.valueOf(3), result.get(0).getItemId());
    }

    @Test
    public void getBottomNByPercentage() {
        Graph graph1 = createGraph(1);
        Graph graph2 = createGraph(2);

        graph1.daily.get(0).setValue(graph1.daily.get(0).getValue() * 10);

        List<Graph> graphs = Arrays.asList(graph1, graph2);
        List<ItemMovementDto> itemMovements = undertest.calculateItemMovements(graphs);

        List<ItemMovementDto> result = undertest.getBottomNByPercentage(itemMovements, 1, RangeType.DAY);
        assertNotNull(result);
        assertEquals("ID should be 2", Integer.valueOf(2), result.get(0).getItemId());
    }

    private Graph createGraph(int valueMultiplier) {
        Graph graph = new Graph();
        graph.id = valueMultiplier;
        graph.daily = new ArrayList<>();
        graph.daily.add(new Pair<>(LocalDate.now().atStartOfDay(), 10f * valueMultiplier));
        graph.daily.add(new Pair<>(LocalDate.now().atStartOfDay().minusDays(1), 8f * valueMultiplier));
        graph.daily.add(new Pair<>(LocalDate.now().atStartOfDay().minusMonths(1), 2f * valueMultiplier));
        graph.daily.add(new Pair<>(LocalDate.now().atStartOfDay().minusYears(1), 1f * valueMultiplier));
        return graph;
    }
}