package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.domain;

import com.google.common.base.Optional;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.domain.entity.ExchangeRateEntity;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.domain.exception.SalesEcbProxyDaoException;
import org.joda.time.LocalDate;

import java.math.BigDecimal;

public interface ExchangeRateDomainModel {

    /**
     * Queries the most recent rate date
     * @return the most recent rate date
     */
    Optional<LocalDate> findDateOfMostRecentRate() throws SalesEcbProxyDaoException;

    /**
     * Queries a rate most recent till date
     * @param date date
     * @param sourceCurrency sourceCurrency
     * @param targetCurrency targetCurrency
     * @return the rate most recent till date, wrapped in an optional
     */
    Optional<BigDecimal> findRateMostRecentTillDate(LocalDate date, String sourceCurrency, String targetCurrency) throws SalesEcbProxyDaoException;

    /**
     * Saves or update a response to database
     *
     * @param exchangeRateEntityToPersist to be saved
     * @return the created exchangeRateEntity
     */
    ExchangeRateEntity persist(ExchangeRateEntity exchangeRateEntityToPersist);

}
