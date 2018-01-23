package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.bean;

import com.google.common.base.Optional;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.EcbRestService;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.dto.ExchangeRate;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.service.ExchangeRateService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDate;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Singleton
@Startup
public class SelfPopulatingRepositoryBean {

    private final AtomicBoolean isBusyPopulatingExchangeRateRepository = new AtomicBoolean(false);

    @EJB
    private EcbRestService ecbRestService;

    @EJB
    private ExchangeRateService exchangeRateService;

    @PostConstruct
    public void initialize() {
        log.info("Populate repository for missing exchange rates");
        populateRepositoryForMissingExchangeRates();
    }

    // Retry interval: 30 minute
    @Schedule(minute = "*/30", hour = "*", persistent = false)
    public void retryPopulateRepositoryForMissingExchangeRates(Timer timer) {
        log.info("Scheduled populate repository for missing exchange rates");
        populateRepositoryForMissingExchangeRates();
    }

    private boolean isAlreadyStartedPopulatingRepositoryForMissingExchangeRatesAllowed() {
        return !(this.isBusyPopulatingExchangeRateRepository.getAndSet(true));
    }

    private void setFinishedPopulatingExchangeRateRepository() {
        this.isBusyPopulatingExchangeRateRepository.getAndSet(false);
    }

    private void populateRepositoryForMissingExchangeRates() {
        if (!isAlreadyStartedPopulatingRepositoryForMissingExchangeRatesAllowed()) {
            setFinishedPopulatingExchangeRateRepository();
            log.info("Already busy populating currency exchange rate repository for today's rates");
            return;
        }

        try {
            LocalDate yesterday = LocalDate.now().minusDays(1);
            Optional<LocalDate> mostRecentRateDate = exchangeRateService.getMostRecentExchangeRateDate();
            if (!mostRecentRateDate.isPresent()) {
                log.info("First time populate exchange rates up to three months back from today");
                mostRecentRateDate = Optional.of(yesterday.minusMonths(3));
            }

            if (yesterday.equals(mostRecentRateDate.get())) {
                log.info("Currency exchange rates for today have already been loaded. No more actions required.");
                return;
            }

            Optional<LocalDate> startDate = Optional.of(mostRecentRateDate.get().plusDays(1));
            List<ExchangeRate> exchangeRates = ecbRestService.findExchangeRates(startDate);

            exchangeRateService.persistExchangeRates(exchangeRates);

        } catch (Exception e) {
            log.error("Unable to update repository for missing currency exchange rates.", e);

        } finally {
            setFinishedPopulatingExchangeRateRepository();
        }
    }
}
