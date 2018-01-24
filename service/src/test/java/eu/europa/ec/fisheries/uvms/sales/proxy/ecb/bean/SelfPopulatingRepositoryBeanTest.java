package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.bean;

import com.google.common.base.Optional;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.EcbRestService;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.dto.ExchangeRate;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.exception.EcbProxyException;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.service.ExchangeRateService;
import org.joda.time.LocalDate;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.ejb.Timer;
import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SelfPopulatingRepositoryBeanTest {

    @InjectMocks
    private SelfPopulatingRepositoryBean selfPopulatingRepositoryBean;

    @Mock
    private EcbRestService ecbRestService;

    @Mock
    private ExchangeRateService exchangeRateService;

    @Mock
    Timer timer;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testInitialize() throws Exception {
        //data set
        LocalDate mostRecentExchangeRateDate = new LocalDate(2018, 1, 16);
        LocalDate startDate = new LocalDate(2018, 1, 17);
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setRate(new BigDecimal(1.68));
        exchangeRate.setSourceCurrency("NZD");
        exchangeRate.setTargetCurrency("EUR");
        exchangeRate.setStartDate(new LocalDate(2018, 1, 16));
        ArrayList<ExchangeRate> exchangeRates = new ArrayList<>();
        exchangeRates.add(exchangeRate);

        //mock
        doReturn(Optional.of(mostRecentExchangeRateDate)).when(exchangeRateService).getMostRecentExchangeRateDate();
        doReturn(exchangeRates).when(ecbRestService).findExchangeRates(Optional.of(startDate));
        doNothing().when(exchangeRateService).persistExchangeRates(exchangeRates);

        //execute
        selfPopulatingRepositoryBean.initialize();

        //verify and assert
        verify(exchangeRateService).getMostRecentExchangeRateDate();
        verify(ecbRestService).findExchangeRates(Optional.of(startDate));
        verify(exchangeRateService).persistExchangeRates(exchangeRates);
        verifyNoMoreInteractions(ecbRestService, exchangeRateService, timer);
    }

    @Test
    public void tryInitializeForMissingExchangeRatesAndEcbProxyException() throws Exception {
        //data set
        LocalDate mostRecentExchangeRateDate = new LocalDate(2018, 1, 16);
        LocalDate startDate = new LocalDate(2018, 1, 17);
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setRate(new BigDecimal(1.68));
        exchangeRate.setSourceCurrency("NZD");
        exchangeRate.setTargetCurrency("EUR");
        exchangeRate.setStartDate(new LocalDate(2018, 1, 16));
        ArrayList<ExchangeRate> exchangeRates = new ArrayList<>();
        exchangeRates.add(exchangeRate);

        //mock
        doReturn(Optional.of(mostRecentExchangeRateDate)).when(exchangeRateService).getMostRecentExchangeRateDate();
        doReturn(exchangeRates).when(ecbRestService).findExchangeRates(Optional.of(startDate));
        doThrow(new EcbProxyException("MyEcbProxyException")).when(exchangeRateService).persistExchangeRates(exchangeRates);

        //execute
        try {
            selfPopulatingRepositoryBean.initialize();

        } catch(Exception e) {
            fail("No exception should have been thrown");
        }

        //verify and assert
        verify(exchangeRateService).getMostRecentExchangeRateDate();
        verify(ecbRestService).findExchangeRates(Optional.of(startDate));
        verify(exchangeRateService).persistExchangeRates(exchangeRates);
        verifyNoMoreInteractions(ecbRestService, exchangeRateService, timer);
    }

    @Test
    public void testRetryPopulateRepositoryForMissingExchangeRates() throws Exception {
        //data set
        LocalDate mostRecentExchangeRateDate = new LocalDate(2018, 1, 16);
        LocalDate startDate = new LocalDate(2018, 1, 17);
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setRate(new BigDecimal(1.68));
        exchangeRate.setSourceCurrency("NZD");
        exchangeRate.setTargetCurrency("EUR");
        exchangeRate.setStartDate(new LocalDate(2018, 1, 16));
        ArrayList<ExchangeRate> exchangeRates = new ArrayList<>();
        exchangeRates.add(exchangeRate);

        //mock
        doReturn(Optional.of(mostRecentExchangeRateDate)).when(exchangeRateService).getMostRecentExchangeRateDate();
        doReturn(exchangeRates).when(ecbRestService).findExchangeRates(Optional.of(startDate));
        doNothing().when(exchangeRateService).persistExchangeRates(exchangeRates);

        //execute
        selfPopulatingRepositoryBean.retryPopulateRepositoryForMissingExchangeRates(timer);

        //verify and assert
        verify(exchangeRateService).getMostRecentExchangeRateDate();
        verify(ecbRestService).findExchangeRates(Optional.of(startDate));
        verify(exchangeRateService).persistExchangeRates(exchangeRates);
        verifyNoMoreInteractions(ecbRestService, exchangeRateService, timer);
    }

    @Test
    public void testRetryPopulateRepositoryForExchangeRepositoryUpToDateNoActionRequired() throws Exception {
        //data set
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate startDate = new LocalDate(2018, 1, 17);
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setRate(new BigDecimal(1.68));
        exchangeRate.setSourceCurrency("NZD");
        exchangeRate.setTargetCurrency("EUR");
        exchangeRate.setStartDate(new LocalDate(2018, 1, 16));
        ArrayList<ExchangeRate> exchangeRates = new ArrayList<>();
        exchangeRates.add(exchangeRate);

        //mock
        doReturn(Optional.of(yesterday)).when(exchangeRateService).getMostRecentExchangeRateDate();

        //execute
        selfPopulatingRepositoryBean.retryPopulateRepositoryForMissingExchangeRates(timer);

        //verify and assert
        verify(exchangeRateService).getMostRecentExchangeRateDate();
        verifyNoMoreInteractions(ecbRestService, exchangeRateService, timer);
    }

    @Test
    public void tryRetryPopulateRepositoryForMissingExchangeRatesAndEcbProxyException() throws Exception {
        //data set
        LocalDate mostRecentExchangeRateDate = new LocalDate(2018, 1, 16);
        LocalDate startDate = new LocalDate(2018, 1, 17);
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setRate(new BigDecimal(1.68));
        exchangeRate.setSourceCurrency("NZD");
        exchangeRate.setTargetCurrency("EUR");
        exchangeRate.setStartDate(new LocalDate(2018, 1, 16));
        ArrayList<ExchangeRate> exchangeRates = new ArrayList<>();
        exchangeRates.add(exchangeRate);

        //mock
        doReturn(Optional.of(mostRecentExchangeRateDate)).when(exchangeRateService).getMostRecentExchangeRateDate();
        doReturn(exchangeRates).when(ecbRestService).findExchangeRates(Optional.of(startDate));
        doThrow(new EcbProxyException("MyEcbProxyException")).when(exchangeRateService).persistExchangeRates(exchangeRates);

        //execute
        try {
            selfPopulatingRepositoryBean.retryPopulateRepositoryForMissingExchangeRates(timer);

        } catch(Exception e) {
            fail("No exception should have been thrown");
        }

        //verify and assert
        verify(exchangeRateService).getMostRecentExchangeRateDate();
        verify(ecbRestService).findExchangeRates(Optional.of(startDate));
        verify(exchangeRateService).persistExchangeRates(exchangeRates);
        verifyNoMoreInteractions(ecbRestService, exchangeRateService, timer);
    }

    @Test
    public void testInitializeFirstTimePopulateCurrencyExchangeRateTableForEmptyRatesRepository() throws Exception {
        //data set
        LocalDate todayMinusThreeMonths = LocalDate.now().minusMonths(3);
        LocalDate mostRecentExchangeRateDate = new LocalDate(2018, 1, 16);
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setRate(new BigDecimal(1.68));
        exchangeRate.setSourceCurrency("NZD");
        exchangeRate.setTargetCurrency("EUR");
        exchangeRate.setStartDate(todayMinusThreeMonths);
        ArrayList<ExchangeRate> exchangeRates = new ArrayList<>();
        exchangeRates.add(exchangeRate);

        //mock
        doReturn(Optional.absent()).when(exchangeRateService).getMostRecentExchangeRateDate();
        doReturn(exchangeRates).when(ecbRestService).findExchangeRates(Optional.of(todayMinusThreeMonths));
        doNothing().when(exchangeRateService).persistExchangeRates(exchangeRates);

        //execute
        selfPopulatingRepositoryBean.initialize();

        //verify and assert
        verify(exchangeRateService).getMostRecentExchangeRateDate();
        verify(ecbRestService).findExchangeRates(Optional.of(todayMinusThreeMonths));
        verify(exchangeRateService).persistExchangeRates(exchangeRates);
        verifyNoMoreInteractions(ecbRestService, exchangeRateService, timer);
    }

}
