package eu.europa.ec.fisheries.uvms.sales.proxy.ecb;

import com.google.common.base.Optional;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.dto.ExchangeRate;
import org.joda.time.LocalDate;

import java.math.BigDecimal;
import java.util.Collection;

/**
 * Keeps the rates of known currencies for certain dates.
 */
public interface ExchangeRateCache {

    /**
     * Gets the requested currency rate, from the cache.
     */
    Optional<BigDecimal> findRate(String sourceCurrency, String targetCurrency, LocalDate date);

    /**
     * Searches for the newest rate in the cache, and returns its date.
     */
    Optional<LocalDate> getDateOfLastKnownRate();

    /**
     * Adds new currency rates to the cache.
     * When given rates already exist, they are not added (leaving no doubles in the cache).
     * Old rates are not removed.
     * @param exchangeRates new currency rates
     */
    void add(Collection<ExchangeRate> exchangeRates);
}
