package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.domain.bean;

import com.google.common.base.Optional;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.domain.ExchangeRateDomainModel;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.domain.dao.ExchangeRateDao;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.domain.entity.ExchangeRateEntity;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.domain.exception.SalesEcbProxyDaoException;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDate;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.math.BigDecimal;

@Slf4j
@Stateless
public class ExchangeRateDomainModelBean implements ExchangeRateDomainModel {

    @EJB
    private ExchangeRateDao exchangeRateDao;

    @Override
    public Optional<LocalDate> findDateOfMostRecentRate() throws SalesEcbProxyDaoException {
        Optional<ExchangeRateEntity> rateMostRecentTillDate = exchangeRateDao.findByMostRecentRate();
        if (rateMostRecentTillDate.isPresent()) {
            return Optional.of(rateMostRecentTillDate.get().getStartDateTime().toLocalDate());
        }
        return Optional.absent();
    }

    @Override
    public Optional<BigDecimal> findRateMostRecentTillDate(LocalDate date, String sourceCurrency, String targetCurrency) throws SalesEcbProxyDaoException {
        Optional<ExchangeRateEntity> rateMostRecentTillDate = exchangeRateDao.findByMostRecentTillDate(date, sourceCurrency, targetCurrency);
        if (rateMostRecentTillDate.isPresent()) {
            return Optional.of(rateMostRecentTillDate.get().getRate());
        }
        return Optional.absent();
    }

    @Override
    public ExchangeRateEntity persist(ExchangeRateEntity exchangeRateEntityToPersist) {
        if (exchangeRateEntityToPersist == null) {
            log.warn("Exchange rate to persist should not be null");
        }
        return exchangeRateDao.create(exchangeRateEntityToPersist);
    }
}
