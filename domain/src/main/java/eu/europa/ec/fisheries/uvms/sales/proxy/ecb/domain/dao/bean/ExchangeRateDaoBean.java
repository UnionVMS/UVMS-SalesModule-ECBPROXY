package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.domain.dao.bean;

import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.domain.dao.ExchangeRateDao;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.domain.entity.ExchangeRateEntity;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.domain.exception.SalesEcbProxyDaoException;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Slf4j
@Stateless
public class ExchangeRateDaoBean extends BaseDaoForSalesECBProxy<ExchangeRateEntity, Integer> implements ExchangeRateDao {

    @Override
    public Optional<ExchangeRateEntity> findByMostRecentRate() throws SalesEcbProxyDaoException {
        try {
            TypedQuery<ExchangeRateEntity> exchangeRateQuery = em.createNamedQuery(ExchangeRateEntity.FIND_BY_MOST_RECENT_RATE_DATE, ExchangeRateEntity.class);
            exchangeRateQuery.setMaxResults(1);
            List<ExchangeRateEntity> exchangeRateEntities = exchangeRateQuery.getResultList();
            if (!exchangeRateEntities.isEmpty()) {
                return Optional.of(exchangeRateEntities.get(0));
            }
            return Optional.empty();

        } catch (Exception e) {
            String errorMessage = "Unable to find date of most recent rate";
            log.error(errorMessage, e);
            throw new SalesEcbProxyDaoException(errorMessage, e);
        }
    }

    @Override
    public Optional<ExchangeRateEntity> findByMostRecentTillDate(LocalDate date, String sourceCurrency, String targetCurrency) throws SalesEcbProxyDaoException {
        try {
            TypedQuery<ExchangeRateEntity> exchangeRateQuery = em.createNamedQuery(ExchangeRateEntity.FIND_BY_TILL_DATE, ExchangeRateEntity.class);
            exchangeRateQuery.setMaxResults(1);
            exchangeRateQuery.setParameter("startDateTime", date.toDateTimeAtStartOfDay(DateTimeZone.UTC));
            exchangeRateQuery.setParameter("sourceCurrency", sourceCurrency);
            exchangeRateQuery.setParameter("targetCurrency", targetCurrency);

            List<ExchangeRateEntity> exchangeRateEntities = exchangeRateQuery.getResultList();
            if (!exchangeRateEntities.isEmpty()) {
                return Optional.of(exchangeRateEntities.get(0));
            }
            return Optional.empty();

        } catch (Exception e) {
            String errorMessage = "Unable to find exchange rates till date";
            log.error(errorMessage, e);
            throw new SalesEcbProxyDaoException(errorMessage, e);
        }
    }

}
