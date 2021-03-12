package lambethd.kraken.server.job;

import lambethd.kraken.server.Configuration;
import lambethd.kraken.server.MongoConfiguration;
import org.junit.Test;
import org.junit.internal.runners.JUnit38ClassRunner;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Configuration.class, MongoConfiguration.class})
public class DashboardDataCreatorTest {

    @Autowired
    private DashboardDataCreator underTest;

    @Test
    public void callInternal() {
        Boolean result = underTest.callInternal();

        Assert.isTrue(result, "Returned result should be true (not failed)");
    }
}