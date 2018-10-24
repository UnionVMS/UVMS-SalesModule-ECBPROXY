package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.service.bean;

import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.domain.ExchangeRateDomainModel;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.domain.entity.ExchangeRateEntity;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.domain.exception.SalesEcbProxyDaoException;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.dto.ExchangeRate;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.exception.EcbProxyException;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.service.ExchangeRateService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
@Stateless
public class ExchangeRateServiceBean implements ExchangeRateService {

    @EJB
    private ExchangeRateDomainModel exchangeRateDomainModel;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Optional<LocalDate> getMostRecentExchangeRateDate() throws EcbProxyException {
        try {
            return exchangeRateDomainModel.findDateOfMostRecentRate();

        } catch (SalesEcbProxyDaoException e) {
            throw new EcbProxyException("Unable to retrieve most recent exchange rate date", e);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Optional<BigDecimal> findRateMostRecentTillDate(LocalDate date, String sourceCurrency, String targetCurrency) throws EcbProxyException {
        try {
            return exchangeRateDomainModel.findRateMostRecentTillDate(date, sourceCurrency, targetCurrency);

        } catch (SalesEcbProxyDaoException e) {
            throw new EcbProxyException("Unable to retrieve exchange rates", e);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void persistExchangeRates(List<ExchangeRate> exchangeRates) throws EcbProxyException {
        log.info("Currency exchange rates to persist size: " + exchangeRates.size());
        if (exchangeRates.isEmpty()) {
            throw new EcbProxyException("Exchange rate is mandatory");
        }
        for (ExchangeRate exchangeRate : exchangeRates) {
            exchangeRateDomainModel.persist(mapDtoToEntity(exchangeRate));
        }
    }

    private ExchangeRateEntity mapDtoToEntity(ExchangeRate exchangeRate) {
        return new ExchangeRateEntity()
                .sourceCurrency(exchangeRate.getSourceCurrency())
                .targetCurrency(exchangeRate.getTargetCurrency())
                .startDateTime(exchangeRate.getStartDate().toDateTimeAtStartOfDay(DateTimeZone.UTC))
                .rate(exchangeRate.getRate())
                .creationDateTime(DateTime.now(DateTimeZone.UTC));
    }
}
