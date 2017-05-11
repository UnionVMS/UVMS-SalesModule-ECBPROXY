package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.bean;

import com.google.common.base.Optional;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.EcbRestService;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.ExchangeRateCache;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.dto.ExchangeRate;
import org.joda.time.LocalDate;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.List;

@Singleton
@Startup
public class FillUpCurrencyCacheBean {

    @EJB
    private ExchangeRateCache cache;

    @EJB
    private EcbRestService ecbRestService;

    @PostConstruct
    public void init() {
        updateCache();
    }

    @Schedule
    public void updateCache() {
        Optional<LocalDate> startDate = cache.getDateOfLastKnownRate();
        List<ExchangeRate> exchangeRates = ecbRestService.findExchangeRates(startDate);
        cache.add(exchangeRates);
    }

}
