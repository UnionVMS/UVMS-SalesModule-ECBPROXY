package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.service.bean;

import com.google.common.base.Optional;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.domain.ExchangeRateDomainModel;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.domain.entity.ExchangeRateEntity;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.domain.exception.SalesEcbProxyDaoException;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.dto.ExchangeRate;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.exception.EcbProxyException;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ExchangeRateServiceBeanTest {

    @InjectMocks
    private ExchangeRateServiceBean exchangeRateServiceBean;

    @Mock
    private ExchangeRateDomainModel exchangeRateDomainModel;

    @Test
    public void testFindRateMostRecentTillDate() throws Exception {
        //data set
        String sourceCurrency = "USD";
        String targetCurrency = "EUR";
        LocalDate date = new LocalDate(2017, 3, 5);
        BigDecimal rate = BigDecimal.valueOf(0.5000);

        //mock
        doReturn(Optional.of(rate)).when(exchangeRateDomainModel).findRateMostRecentTillDate(date, sourceCurrency, targetCurrency);

        //execute
        Optional<BigDecimal> rateMostRecentTillDate = exchangeRateServiceBean.findRateMostRecentTillDate(date, sourceCurrency, targetCurrency);

        //verify and assert
        verify(exchangeRateDomainModel).findRateMostRecentTillDate(date, sourceCurrency, targetCurrency);
        verifyNoMoreInteractions(exchangeRateDomainModel);
        assertTrue(rateMostRecentTillDate.isPresent());
        assertEquals(rate, rateMostRecentTillDate.get());
    }

    @Test
    public void tryFindRateMostRecentTillDateForEcbProxyException() throws Exception {
        //data set
        String sourceCurrency = "USD";
        String targetCurrency = "EUR";
        LocalDate date = new LocalDate(2017, 3, 5);

        //mock
        doThrow(new SalesEcbProxyDaoException("MySalesEcbProxyDaoException")).when(exchangeRateDomainModel).findRateMostRecentTillDate(date, sourceCurrency, targetCurrency);

        //execute
        try {
            exchangeRateServiceBean.findRateMostRecentTillDate(date, sourceCurrency, targetCurrency);

        } catch(EcbProxyException e) {
            assertEquals("Unable to retrieve exchange rates", e.getMessage());
        }

        //verify and assert
        verify(exchangeRateDomainModel).findRateMostRecentTillDate(date, sourceCurrency, targetCurrency);
        verifyNoMoreInteractions(exchangeRateDomainModel);
    }

    @Test
    public void testGetMostRecentExchangeRateDate() throws Exception {
        //data set
        LocalDate date = new LocalDate(2017, 3, 5);

        //mock
        doReturn(Optional.of(date)).when(exchangeRateDomainModel).findDateOfMostRecentRate();

        //execute
        Optional<LocalDate> mostRecentExchangeRateDate = exchangeRateServiceBean.getMostRecentExchangeRateDate();

        //verify and assert
        verify(exchangeRateDomainModel).findDateOfMostRecentRate();
        verifyNoMoreInteractions(exchangeRateDomainModel);
        assertTrue(mostRecentExchangeRateDate.isPresent());
        assertEquals(date, mostRecentExchangeRateDate.get());
    }

    @Test
    public void tryGetMostRecentExchangeRateDateForEcbProxyDaoException() throws Exception {
        //mock
        doThrow(new SalesEcbProxyDaoException("MySalesEcbProxyDaoException")).when(exchangeRateDomainModel).findDateOfMostRecentRate();

        //execute
        try {
            exchangeRateServiceBean.getMostRecentExchangeRateDate();

        } catch(EcbProxyException e) {
            assertEquals("Unable to retrieve most recent exchange rate date", e.getMessage());
        }

        //verify and assert
        verify(exchangeRateDomainModel).findDateOfMostRecentRate();
        verifyNoMoreInteractions(exchangeRateDomainModel);
    }

    @Test
    public void testPersistExchangeRates() throws Exception {
        //data set
        String sourceCurrency = "USD";
        String targetCurrency = "EUR";
        LocalDate date = new LocalDate(2017, 3, 5);
        BigDecimal rate = BigDecimal.valueOf(0.5000);
        ExchangeRate exchangeRate = new ExchangeRate()
                .sourceCurrency(sourceCurrency)
                .targetCurrency(targetCurrency)
                .startDate(date)
                .rate(rate);
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        exchangeRates.add(exchangeRate);
        ExchangeRateEntity exchangeRateEntity = new ExchangeRateEntity()
                .sourceCurrency(sourceCurrency)
                .targetCurrency(targetCurrency)
                .startDateTime(date.toDateTimeAtStartOfDay(DateTimeZone.UTC))
                .rate(rate)
                .creationDateTime(date.toDateTimeAtStartOfDay(DateTimeZone.UTC));

        //mock
        ArgumentCaptor<ExchangeRateEntity> captorExchangeRateEntity = ArgumentCaptor.forClass(ExchangeRateEntity.class);
        doReturn(exchangeRateEntity).when(exchangeRateDomainModel).persist(isA(ExchangeRateEntity.class));

        //execute
        exchangeRateServiceBean.persistExchangeRates(exchangeRates);

        //verify and assert
        verify(exchangeRateDomainModel).persist(captorExchangeRateEntity.capture());
        verifyNoMoreInteractions(exchangeRateDomainModel);

        ExchangeRateEntity exchangeRateEntityToPersist = captorExchangeRateEntity.getValue();
        assertNotNull(exchangeRateEntityToPersist);
        assertEquals(exchangeRateEntity.getSourceCurrency(), exchangeRateEntityToPersist.getSourceCurrency());
        assertEquals(exchangeRateEntity.getTargetCurrency(), exchangeRateEntityToPersist.getTargetCurrency());
        assertEquals(exchangeRateEntity.getStartDateTime(), exchangeRateEntityToPersist.getStartDateTime());
        assertEquals(exchangeRateEntity.getRate(), exchangeRateEntityToPersist.getRate());
        assertNotNull(exchangeRateEntity.getCreationDateTime());
    }

    @Test
    public void tryPersistExchangeRatesForNoRatesToPersistEcbProxyException() throws Exception {
        //data set
        List<ExchangeRate> exchangeRates = new ArrayList<>();

        //execute
        try {
            exchangeRateServiceBean.persistExchangeRates(exchangeRates);
            fail("Persist exchange rates should fail for empty exchange rates");

        } catch (EcbProxyException e) {
            assertEquals("Exchange rate is mandatory", e.getMessage());
        }

        //verify and assert
        verifyNoMoreInteractions(exchangeRateDomainModel);
    }
}
