package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.schedule;

import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.dto.ExchangeRate;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.exception.EcbProxyException;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.service.EcbRestService;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.service.ExchangeRateService;
import org.joda.time.LocalDate;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SelfPopulatingRepositoryBeanTest {

    @InjectMocks
    private SelfPopulatingRepositoryBean selfPopulatingRepositoryBean;

    @Mock
    private EcbRestService ecbRestService;

    @Mock
    private ExchangeRateService exchangeRateService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void tryPopulateRepositoryForMissingExchangeRatesWhenMissingExchangeRatesAndEcbProxyException() throws Exception {
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
        selfPopulatingRepositoryBean.populateRepositoryForMissingExchangeRates();

        //verify and assert
        verify(exchangeRateService).getMostRecentExchangeRateDate();
        verify(ecbRestService).findExchangeRates(Optional.of(startDate));
        verify(exchangeRateService).persistExchangeRates(exchangeRates);
        verifyNoMoreInteractions(ecbRestService, exchangeRateService);
    }

    @Test
    public void testPopulateRepositoryForMissingExchangeRatesWhenThereAreMissingExchangeRates() throws Exception {
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
        selfPopulatingRepositoryBean.populateRepositoryForMissingExchangeRates();

        //verify and assert
        verify(exchangeRateService).getMostRecentExchangeRateDate();
        verify(ecbRestService).findExchangeRates(Optional.of(startDate));
        verify(exchangeRateService).persistExchangeRates(exchangeRates);
        verifyNoMoreInteractions(ecbRestService, exchangeRateService);
    }

    @Test
    public void testPopulateRepositoryForMissingExchangeRatesWhenThereIsNoActionRequired() throws Exception {
        //data set
        LocalDate yesterday = LocalDate.now().minusDays(1);

        //mock
        doReturn(Optional.of(yesterday)).when(exchangeRateService).getMostRecentExchangeRateDate();

        //execute
        selfPopulatingRepositoryBean.populateRepositoryForMissingExchangeRates();

        //verify and assert
        verify(exchangeRateService).getMostRecentExchangeRateDate();
        verifyNoMoreInteractions(ecbRestService, exchangeRateService);
    }

    @Test
    public void testPopulateRepositoryForMissingExchangeRatesFirstTimePopulateCurrencyExchangeRateTableForEmptyRatesRepository() throws Exception {
        //data set
        LocalDate startDate = LocalDate.now().minusDays(1).minusMonths(3).plusDays(1);
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setRate(new BigDecimal(1.68));
        exchangeRate.setSourceCurrency("NZD");
        exchangeRate.setTargetCurrency("EUR");
        exchangeRate.setStartDate(startDate);
        ArrayList<ExchangeRate> exchangeRates = new ArrayList<>();
        exchangeRates.add(exchangeRate);

        //mock
        doReturn(Optional.empty()).when(exchangeRateService).getMostRecentExchangeRateDate();
        doReturn(exchangeRates).when(ecbRestService).findExchangeRates(Optional.of(startDate));
        doNothing().when(exchangeRateService).persistExchangeRates(exchangeRates);

        //execute
        selfPopulatingRepositoryBean.populateRepositoryForMissingExchangeRates();

        //verify and assert
        verify(exchangeRateService).getMostRecentExchangeRateDate();
        verify(ecbRestService).findExchangeRates(Optional.of(startDate));
        verify(exchangeRateService).persistExchangeRates(exchangeRates);
        verifyNoMoreInteractions(ecbRestService, exchangeRateService);
    }

}
