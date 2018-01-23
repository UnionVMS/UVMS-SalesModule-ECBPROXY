package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.dto;

import org.joda.time.LocalDate;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ExchangeRateTest {

    @Test
    public void testExchangeRateConstructor() throws Exception {
        BigDecimal rate = new BigDecimal(1.68);
        String sourceCurrency = "NZD";
        String targetCurrency = "EUR";

        LocalDate startDate = new LocalDate(2018, 1, 16);

        ExchangeRate exchangeRate = new ExchangeRate(rate, sourceCurrency, targetCurrency, startDate);
        assertEquals(rate, exchangeRate.getRate());
        assertEquals(sourceCurrency, exchangeRate.getSourceCurrency());
        assertEquals(targetCurrency, exchangeRate.getTargetCurrency());
        assertEquals(startDate, exchangeRate.getStartDate());
    }

    @Test
    public void testExchangeRateSetters() throws Exception {
        BigDecimal rate = new BigDecimal(1.68);
        String sourceCurrency = "NZD";
        String targetCurrency = "EUR";
        LocalDate startDate = new LocalDate(2018, 1, 16);

        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setRate(rate);
        exchangeRate.setSourceCurrency(sourceCurrency);
        exchangeRate.setTargetCurrency(targetCurrency);
        exchangeRate.setStartDate(startDate);

        assertEquals(rate, exchangeRate.getRate());
        assertEquals(sourceCurrency, exchangeRate.getSourceCurrency());
        assertEquals(targetCurrency, exchangeRate.getTargetCurrency());
        assertEquals(startDate, exchangeRate.getStartDate());
    }

    @Test
    public void testExchangeRateBuilder() throws Exception {
        BigDecimal rate = new BigDecimal(1.68);
        String sourceCurrency = "NZD";
        String targetCurrency = "EUR";
        LocalDate startDate = new LocalDate(2018, 1, 16);

        ExchangeRate exchangeRate = new ExchangeRate()
                .rate(rate)
                .sourceCurrency(sourceCurrency)
                .targetCurrency(targetCurrency)
                .startDate(startDate);

        assertEquals(rate, exchangeRate.getRate());
        assertEquals(sourceCurrency, exchangeRate.getSourceCurrency());
        assertEquals(targetCurrency, exchangeRate.getTargetCurrency());
        assertEquals(startDate, exchangeRate.getStartDate());
    }
}
