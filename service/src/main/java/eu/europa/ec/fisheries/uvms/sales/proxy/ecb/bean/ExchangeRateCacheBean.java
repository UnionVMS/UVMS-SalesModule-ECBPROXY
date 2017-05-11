package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.bean;

import com.google.common.base.Optional;
import com.google.common.collect.Ordering;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.ExchangeRateCache;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.dto.ExchangeRate;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.dto.ExchangeRateKey;
import org.joda.time.LocalDate;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Singleton
@Startup
public class ExchangeRateCacheBean implements ExchangeRateCache {

    protected Map<ExchangeRateKey, BigDecimal> rates;

    @PostConstruct
    public void init() {
        rates = new HashMap<>();
    }


    @Override
    public Optional<BigDecimal> findRate(String sourceCurrency, String targetCurrency, LocalDate date) {
        ExchangeRateKey key = new ExchangeRateKey(date, sourceCurrency, targetCurrency);
        return Optional.fromNullable(rates.get(key));
    }

    @Override
    public Optional<LocalDate> getDateOfLastKnownRate() {
        if (rates.isEmpty()) {
            return Optional.absent();
        } else {
            return Optional.of(Ordering .natural()
                                        .max(rates.keySet())
                                        .getDate());
        }
    }

    @Override
    public void add(Collection<ExchangeRate> newExchangeRates) {
        for (ExchangeRate newExchangeRate : newExchangeRates) {
            this.rates.put(newExchangeRate.getKey(), newExchangeRate.getRate());
        }
    }
}
