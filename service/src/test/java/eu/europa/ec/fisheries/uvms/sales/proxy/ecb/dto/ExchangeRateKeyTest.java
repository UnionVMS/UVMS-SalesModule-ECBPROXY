package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.dto;

import org.joda.time.LocalDate;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ExchangeRateKeyTest {

    @Test
    public void testCompareTWhenFirstIsGreaterThanSecond() throws Exception {
        ExchangeRateKey exchangeRateKey1 = new ExchangeRateKey(new LocalDate(2017, 1, 1), "CUR1", "CUR2");
        ExchangeRateKey exchangeRateKey2 = new ExchangeRateKey(new LocalDate(2015, 1, 1), "CUR1", "CUR2");

        assertTrue(exchangeRateKey1.compareTo(exchangeRateKey2) > 0);
    }

    @Test
    public void testCompareTWhenFirstIsSmallerThanSecond() throws Exception {
        ExchangeRateKey exchangeRateKey1 = new ExchangeRateKey(new LocalDate(2017, 1, 1), "CUR1", "CUR2");
        ExchangeRateKey exchangeRateKey2 = new ExchangeRateKey(new LocalDate(2018, 1, 1), "CUR1", "CUR2");

        assertTrue(exchangeRateKey1.compareTo(exchangeRateKey2) < 0);
    }

    @Test
    public void testCompareTWhenFirstIsEqualToSecond() throws Exception {
        ExchangeRateKey exchangeRateKey1 = new ExchangeRateKey(new LocalDate(2017, 1, 1), "CUR1", "CUR2");
        ExchangeRateKey exchangeRateKey2 = new ExchangeRateKey(new LocalDate(2017, 1, 1), "CUR1", "CUR2");

        assertEquals(0, exchangeRateKey1.compareTo(exchangeRateKey2));
    }

}