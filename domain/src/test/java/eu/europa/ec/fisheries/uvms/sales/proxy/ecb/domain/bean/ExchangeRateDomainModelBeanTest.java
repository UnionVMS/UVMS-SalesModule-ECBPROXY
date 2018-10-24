package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.domain.bean;

import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.domain.dao.ExchangeRateDao;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.domain.entity.ExchangeRateEntity;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.domain.exception.SalesEcbProxyDaoException;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ExchangeRateDomainModelBeanTest {

    @InjectMocks
    ExchangeRateDomainModelBean exchangeRateDomainModelBean;

    @Mock
    ExchangeRateDao exchangeRateDao;

    @Test
    public void testFindDateOfMostRecentRate() throws Exception {
        //data set
        DateTime dateTime = new DateTime(2018, 1, 16, 0, 0, 0, DateTimeZone.UTC);
        LocalDate expectedLocalDate = new LocalDate(2018, 1, 16);

        ExchangeRateEntity expectedExchangeRateEntity = new ExchangeRateEntity()
                .rate(new BigDecimal(1L))
                .sourceCurrency("NZD")
                .targetCurrency("EUR")
                .startDateTime(dateTime);

        //mock
        doReturn(Optional.of(expectedExchangeRateEntity)).when(exchangeRateDao).findByMostRecentRate();

        //Execute
        Optional<LocalDate> localDate = exchangeRateDomainModelBean.findDateOfMostRecentRate();

        //verify and assert
        verify(exchangeRateDao).findByMostRecentRate();
        verifyNoMoreInteractions(exchangeRateDao);
        assertTrue(localDate.isPresent());
        assertEquals(expectedLocalDate, localDate.get());
    }

    @Test
    public void testFindDateOfMostRecentRateForEmptyRepository() throws Exception {
        //mock
        doReturn(Optional.empty()).when(exchangeRateDao).findByMostRecentRate();

        //Execute
        Optional<LocalDate> localDate = exchangeRateDomainModelBean.findDateOfMostRecentRate();

        //verify and assert
        verify(exchangeRateDao).findByMostRecentRate();
        verifyNoMoreInteractions(exchangeRateDao);
        assertFalse(localDate.isPresent());
    }

    @Test
    public void tryFindDateOfMostRecentRateForSalesEcbProxyDaoException() throws Exception {
        //data set
        SalesEcbProxyDaoException salesEcbProxyDaoException = new SalesEcbProxyDaoException("MySalesEcbProxyDaoException");

        //mock
        doThrow(salesEcbProxyDaoException).when(exchangeRateDao).findByMostRecentRate();

        //Execute
        try {
            exchangeRateDomainModelBean.findDateOfMostRecentRate();
            fail("findDateOfMostRecentRate should fail for SalesEcbProxyDaoException");

        } catch(SalesEcbProxyDaoException e) {
            assertEquals(salesEcbProxyDaoException, e);
        }

        //verify and assert
        verify(exchangeRateDao).findByMostRecentRate();
        verifyNoMoreInteractions(exchangeRateDao);
    }

    @Test
    public void testFindRateMostRecentTillDate() throws Exception {
        //data set
        BigDecimal rate = new BigDecimal("1.68");
        LocalDate localDate = new LocalDate(2018, 1, 16);
        String sourceCurrency = "NZD";
        String targetCurrency = "EUR";
        ExchangeRateEntity exchangeRateEntity = new ExchangeRateEntity()
                .rate(rate);

        //mock
        doReturn(Optional.of(exchangeRateEntity)).when(exchangeRateDao).findByMostRecentTillDate(localDate, sourceCurrency, targetCurrency);

        //Execute
        Optional<BigDecimal> rateMostRecentTillDate = exchangeRateDomainModelBean.findRateMostRecentTillDate(localDate, sourceCurrency, targetCurrency);

        //verify and assert
        verify(exchangeRateDao).findByMostRecentTillDate(localDate, sourceCurrency, targetCurrency);
        verifyNoMoreInteractions(exchangeRateDao);
        assertTrue(rateMostRecentTillDate.isPresent());
        assertEquals(rate, rateMostRecentTillDate.get());
    }

    @Test
    public void testFindRateMostRecentTillDateForEmptyRepository() throws Exception {
        //data set
        LocalDate localDate = new LocalDate(2018, 1, 16);
        String sourceCurrency = "NZD";
        String targetCurrency = "EUR";

        //mock
        doReturn(Optional.empty()).when(exchangeRateDao).findByMostRecentTillDate(localDate, sourceCurrency, targetCurrency);

        //Execute
        Optional<BigDecimal> rateMostRecentTillDate = exchangeRateDomainModelBean.findRateMostRecentTillDate(localDate, sourceCurrency, targetCurrency);

        //verify and assert
        verify(exchangeRateDao).findByMostRecentTillDate(localDate, sourceCurrency, targetCurrency);
        verifyNoMoreInteractions(exchangeRateDao);
        assertFalse(rateMostRecentTillDate.isPresent());
    }

    @Test
    public void tryFindRateMostRecentTillDateForSalesEcbProxyDaoException() throws Exception {
        //data set
        LocalDate localDate = new LocalDate(2018, 1, 16);
        String sourceCurrency = "NZD";
        String targetCurrency = "EUR";
        SalesEcbProxyDaoException salesEcbProxyDaoException = new SalesEcbProxyDaoException("MySalesEcbProxyDaoException");

        //mock
        doThrow(salesEcbProxyDaoException).when(exchangeRateDao).findByMostRecentTillDate(localDate, sourceCurrency, targetCurrency);

        //Execute
        try {
            exchangeRateDomainModelBean.findRateMostRecentTillDate(localDate, sourceCurrency, targetCurrency);
            fail("findRateMostRecentTillDate should fail for SalesEcbProxyDaoException");

        } catch(SalesEcbProxyDaoException e) {
            assertEquals(salesEcbProxyDaoException, e);
        }

        //verify and assert
        verify(exchangeRateDao).findByMostRecentTillDate(localDate, sourceCurrency, targetCurrency);
        verifyNoMoreInteractions(exchangeRateDao);
    }

    @Test
    public void testPersistExchangeRateEntity() throws Exception {
        //data set
        ExchangeRateEntity exchangeRateEntity = new ExchangeRateEntity();

        //mock
        doReturn(exchangeRateEntity).when(exchangeRateDao).create(exchangeRateEntity);

        //Execute
        ExchangeRateEntity persistedExchangeRateEntity = exchangeRateDomainModelBean.persist(exchangeRateEntity);

        //verify and assert
        verify(exchangeRateDao).create(exchangeRateEntity);
        verifyNoMoreInteractions(exchangeRateDao);
        assertEquals(exchangeRateEntity, persistedExchangeRateEntity);
    }

}
