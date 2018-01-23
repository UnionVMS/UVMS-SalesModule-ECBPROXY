package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.service;

import com.google.common.base.Optional;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.dto.ExchangeRate;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.exception.EcbProxyException;
import org.joda.time.LocalDate;

import java.math.BigDecimal;
import java.util.List;

public interface ExchangeRateService {

    Optional<LocalDate> getMostRecentExchangeRateDate() throws EcbProxyException;

    Optional<BigDecimal> findRateMostRecentTillDate(LocalDate date, String sourceCurrency, String targetCurrency) throws EcbProxyException;

    void persistExchangeRates(List<ExchangeRate> exchangeRates) throws EcbProxyException;
}
