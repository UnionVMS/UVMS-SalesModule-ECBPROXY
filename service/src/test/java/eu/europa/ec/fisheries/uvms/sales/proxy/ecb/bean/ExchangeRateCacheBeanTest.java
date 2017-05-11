package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.bean;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.dto.ExchangeRate;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.dto.ExchangeRateKey;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.HashMap;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ExchangeRateCacheBeanTest {

    @InjectMocks
    private ExchangeRateCacheBean exchangeRateCacheBean;

    @Test
    public void testConvertCurrencyWhenNothingFound() throws Exception {
        //data set
        ExchangeRateKey exchangeRateKey1 = new ExchangeRateKey(new LocalDate(2017, 1, 1), "EUR", "USD");
        BigDecimal rate1 = BigDecimal.valueOf(1.02546);

        LocalDate date = new LocalDate(2017, 2, 2);
        ExchangeRateKey exchangeRateKey2 = new ExchangeRateKey(date, "USD", "EUR");
        BigDecimal rate2 = BigDecimal.valueOf(0.9889);

        exchangeRateCacheBean.rates = new HashMap<>();
        exchangeRateCacheBean.rates.put(exchangeRateKey1, rate1);
        exchangeRateCacheBean.rates.put(exchangeRateKey2, rate2);

        //execute
        Optional<BigDecimal> actualRate = exchangeRateCacheBean.findRate("JAP", "EUR", date);

        //assert
        assertFalse(actualRate.isPresent());
    }

    @Test
    public void testConvertCurrencyWhenSomethingFound() throws Exception {
        //data set
        ExchangeRateKey exchangeRateKey1 = new ExchangeRateKey(new LocalDate(2017, 1, 1), "EUR", "USD");
        BigDecimal rate1 = BigDecimal.valueOf(1.02546);

        LocalDate date = new LocalDate(2017, 2, 2);
        ExchangeRateKey exchangeRateKey2 = new ExchangeRateKey(date, "USD", "EUR");
        BigDecimal rate2 = BigDecimal.valueOf(0.9889);

        exchangeRateCacheBean.rates = new HashMap<>();
        exchangeRateCacheBean.rates.put(exchangeRateKey1, rate1);
        exchangeRateCacheBean.rates.put(exchangeRateKey2, rate2);

        //execute
        Optional<BigDecimal> actualRate = exchangeRateCacheBean.findRate("USD", "EUR", date);

        //assert
        assertTrue(actualRate.isPresent());
        assertEquals(rate2, actualRate.get());
    }

    @Test
    public void testDateOfLastKnownRateWhenSomethingFound() throws Exception {
        //data set
        LocalDate first = new LocalDate(2017, 1, 1);
        LocalDate second = new LocalDate(2017, 2, 2);
        LocalDate last = new LocalDate(2017, 3, 3);

        ExchangeRateKey exchangeRateKey1 = new ExchangeRateKey(second, "EUR", "USD");
        BigDecimal rate1 = BigDecimal.valueOf(1.02546);


        ExchangeRateKey exchangeRateKey2 = new ExchangeRateKey(last, "USD", "EUR");
        BigDecimal rate2 = BigDecimal.valueOf(0.9889);


        ExchangeRateKey exchangeRateKey3 = new ExchangeRateKey(first, "BEL", "EUR");
        BigDecimal rate3 = BigDecimal.valueOf(40.564548);

        exchangeRateCacheBean.rates = new HashMap<>();
        exchangeRateCacheBean.rates.put(exchangeRateKey1, rate1);
        exchangeRateCacheBean.rates.put(exchangeRateKey2, rate2);
        exchangeRateCacheBean.rates.put(exchangeRateKey3, rate3);

        //execute
        Optional<LocalDate> latest = exchangeRateCacheBean.getDateOfLastKnownRate();

        assertTrue(latest.isPresent());
        assertEquals(last, latest.get());
    }

    @Test
    public void testDateOfLastKnownRateWhenNoRates() throws Exception {
        //data set
        exchangeRateCacheBean.rates = new HashMap<>();
        //execute
        Optional<LocalDate> latest = exchangeRateCacheBean.getDateOfLastKnownRate();

        assertFalse(latest.isPresent());
    }

    @Test
    public void testAdd() throws Exception {
        //data set
        ExchangeRateKey exchangeRateKey1 = new ExchangeRateKey(new LocalDate(2017, 1, 1), "EUR", "USD");
        BigDecimal rate1 = BigDecimal.valueOf(1.02546);

        ExchangeRateKey exchangeRateKey2 = new ExchangeRateKey(new LocalDate(2017, 2, 2), "USD", "EUR");
        BigDecimal rate2 = BigDecimal.valueOf(0.9889);
        ExchangeRate exchangeRate2 = new ExchangeRate()
                .rate(rate2)
                .key(exchangeRateKey2);

        ExchangeRateKey exchangeRateKey3 = new ExchangeRateKey(new LocalDate(2017, 3, 3), "BEL", "EUR");
        BigDecimal rate3 = BigDecimal.valueOf(40.564548);
        ExchangeRate exchangeRate3 = new ExchangeRate()
                .rate(rate3)
                .key(exchangeRateKey3);

        exchangeRateCacheBean.rates = new HashMap<>();
        exchangeRateCacheBean.rates.put(exchangeRateKey1, rate1);
        exchangeRateCacheBean.rates.put(exchangeRateKey2, rate2);

        //execute
        exchangeRateCacheBean.add(Lists.newArrayList(exchangeRate2, exchangeRate3));

        //assert
        assertEquals(3, exchangeRateCacheBean.rates.size());

        assertTrue(exchangeRateCacheBean.rates.containsKey(exchangeRateKey1));
        assertEquals(rate1, exchangeRateCacheBean.rates.get(exchangeRateKey1));

        assertTrue(exchangeRateCacheBean.rates.containsKey(exchangeRateKey2));
        assertEquals(rate2, exchangeRateCacheBean.rates.get(exchangeRateKey2));

        assertTrue(exchangeRateCacheBean.rates.containsKey(exchangeRateKey3));
        assertEquals(rate3, exchangeRateCacheBean.rates.get(exchangeRateKey3));
    }

}