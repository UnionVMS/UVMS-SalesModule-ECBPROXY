package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.bean;

import com.google.common.base.Optional;
import eu.europa.ec.fisheries.schema.sales.proxy.ecb.types.v1.GetExchangeRateRequest;
import eu.europa.ec.fisheries.schema.sales.proxy.ecb.types.v1.GetExchangeRateResponse;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.ExchangeRateCache;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.exception.EcbProxyException;
import org.joda.time.LocalDate;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EcbProxyClientBeanTest {

    @InjectMocks
    private EcbProxyClientBean ecbProxyClientBean;

    @Mock
    private ExchangeRateCache exchangeRateCache;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testGetExchangeRateWhenSuccessOnFirstTry() throws Exception {
        //data set
        String sourceCurrency = "USD";
        String targetCurrency = "EUR";
        LocalDate date = new LocalDate(2017, 3, 5);
        BigDecimal rate = BigDecimal.valueOf(2.00);


        GetExchangeRateRequest request = new GetExchangeRateRequest()
                .withSourceCurrency(sourceCurrency)
                .withDate(date.toDateTimeAtStartOfDay())
                .withTargetCurrency(targetCurrency);

        //mock
        when(exchangeRateCache.findRate(sourceCurrency, targetCurrency, date)).thenReturn(Optional.of(rate));

        //execute
        GetExchangeRateResponse response = ecbProxyClientBean.getExchangeRate(request);

        //verify and assert
        verify(exchangeRateCache).findRate(sourceCurrency, targetCurrency, date);

        assertEquals(sourceCurrency, response.getSourceCurrency());
        assertEquals(targetCurrency, response.getTargetCurrency());
        assertEquals(date.toDateTimeAtStartOfDay(), response.getDate());
        assertEquals(rate.stripTrailingZeros(), response.getExchangeRate().stripTrailingZeros());
    }

    @Test
    public void testetExchangeRateWhenSuccessOnSecondTry() throws Exception {
        //data set
        String sourceCurrency = "USD";
        String targetCurrency = "EUR";
        LocalDate date = new LocalDate(2017, 3, 5);
        BigDecimal rate = BigDecimal.valueOf(0.5000);


        GetExchangeRateRequest request = new GetExchangeRateRequest()
                .withSourceCurrency(sourceCurrency)
                .withDate(date.toDateTimeAtStartOfDay())
                .withTargetCurrency(targetCurrency);

        //mock
        when(exchangeRateCache.findRate(sourceCurrency, targetCurrency, date)).thenReturn(Optional.<BigDecimal>absent());
        when(exchangeRateCache.findRate(targetCurrency, sourceCurrency, date)).thenReturn(Optional.of(rate));

        //execute
        GetExchangeRateResponse response = ecbProxyClientBean.getExchangeRate(request);

        //verify and assert
        verify(exchangeRateCache).findRate(sourceCurrency, targetCurrency, date);
        verify(exchangeRateCache).findRate(targetCurrency, sourceCurrency, date);

        assertEquals(sourceCurrency, response.getSourceCurrency());
        assertEquals(targetCurrency, response.getTargetCurrency());
        assertEquals(date.toDateTimeAtStartOfDay(), response.getDate());
        assertEquals(new BigDecimal(2.00), response.getExchangeRate().stripTrailingZeros());
    }

    @Test
    public void testGetExchangeRateWhenRateIsUnknown() throws Exception {
        //data set
        String sourceCurrency = "USD";
        String targetCurrency = "EUR";
        LocalDate date = new LocalDate(2017, 3, 5);

        GetExchangeRateRequest request = new GetExchangeRateRequest()
                .withSourceCurrency(sourceCurrency)
                .withDate(date.toDateTimeAtStartOfDay())
                .withTargetCurrency(targetCurrency);

        //mock
        when(exchangeRateCache.findRate(sourceCurrency, targetCurrency, date)).thenReturn(Optional.<BigDecimal>absent());
        when(exchangeRateCache.findRate(targetCurrency, sourceCurrency, date)).thenReturn(Optional.<BigDecimal>absent());

        expectedException.expect(EcbProxyException.class);
        expectedException.expectMessage("No known exchange rate for " + sourceCurrency + " to " + targetCurrency + " on " + date);

        //execute
        ecbProxyClientBean.getExchangeRate(request);

        //verify and assert
        verify(exchangeRateCache).findRate(sourceCurrency, targetCurrency, date);
        verify(exchangeRateCache).findRate(targetCurrency, sourceCurrency, date);
    }

}