package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.domain.dao;

import com.google.common.base.Optional;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.domain.entity.ExchangeRateEntity;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.domain.exception.SalesEcbProxyDaoException;
import org.joda.time.LocalDate;

import javax.validation.constraints.NotNull;

public interface ExchangeRateDao extends DaoForSalesECBProxy<ExchangeRateEntity, Integer> {

    /**
     * Queries a most recent exchange rate
     * @return the most recent exchange rate, wrapped in an optional
     */
    Optional<ExchangeRateEntity> findByMostRecentRate() throws SalesEcbProxyDaoException;

    /**
     * Queries a rate most recent till date
     * @param date date
     * @param sourceCurrency sourceCurrency
     * @param targetCurrency targetCurrency
     * @return the rate most recent till date, wrapped in an optional
     */
    Optional<ExchangeRateEntity> findByMostRecentTillDate(@NotNull LocalDate date, @NotNull String sourceCurrency, @NotNull String targetCurrency) throws SalesEcbProxyDaoException;

}
