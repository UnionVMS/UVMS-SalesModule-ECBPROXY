package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.task;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.EcbRestService;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.ExchangeRateCache;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.bean.FillUpCurrencyCacheBean;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.dto.ExchangeRate;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FillUpCurrencyCacheBeanTest {

    @InjectMocks
    private FillUpCurrencyCacheBean fillUpCurrencyCacheBean;

    @Mock
    private ExchangeRateCache exchangeRateCache;

    @Mock
    private EcbRestService ecbRestService;

    @Test
    public void testUpdateCache() throws Exception {
        Optional<LocalDate> startDate = Optional.of(new LocalDate(2017, 3, 7));
        List<ExchangeRate> exchangeRates = Lists.newArrayList(new ExchangeRate());

        when(exchangeRateCache.getDateOfLastKnownRate()).thenReturn(startDate);
        when(ecbRestService.findExchangeRates(startDate)).thenReturn(exchangeRates);

        fillUpCurrencyCacheBean.updateCache();

        verify(exchangeRateCache).getDateOfLastKnownRate();
        verify(ecbRestService).findExchangeRates(startDate);
        verify(exchangeRateCache).add(exchangeRates);
    }

}