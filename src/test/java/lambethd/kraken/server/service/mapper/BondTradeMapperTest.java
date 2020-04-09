package lambethd.kraken.server.service.mapper;

import domain.TradeType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Instant;
import java.util.Date;

@RunWith(MockitoJUnitRunner.class)
public class BondTradeMapperTest {

    @InjectMocks
    private BondTradeMapper sut;

    @Test
    public void map() {
        //Arrange
        TradeType tradeType = TradeType.Bond;
        String bondId = "BondA";
        double nominal = 10d;
        Date maturityDate = Date.from(Instant.now());
        double coupon = 2d;
        String tradeString = bondId + "," ;
        //Act

        //Assert

    }

    @Test
    public void canMap_GivenBondTradeType_ShouldReturnTrue() {
        //Arrange
        TradeType tradeType = TradeType.Bond;

        //Act
        boolean result = sut.canMap(tradeType);

        //Assert
        Assert.assertTrue(result);
    }

    @Test
    public void canMap_givenAnyNotBondType_ShouldReturnFalse() {
        //Arrange
        TradeType[] tradeTypes = TradeType.values();
        boolean result = false;

        //Act
        for (TradeType tradeType : tradeTypes) {
            if (tradeType == TradeType.Bond)
                continue;
            result = result || sut.canMap(tradeType);
        }

        //Assert
        Assert.assertFalse(result);
    }
}