package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.schedule;

import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.dto.ExchangeRate;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.service.EcbRestService;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.service.ExchangeRateService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
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

    @Resource
    private ManagedScheduledExecutorService executorService;

    @PostConstruct
    public void initialize() {
        executorService.scheduleAtFixedRate(() -> {
            log.info("Populate repository for missing exchange rates");
            try {
                populateRepositoryForMissingExchangeRates();
            } catch(Exception t) {
                log.error("Something went wrong fetching exchange rates", t);
            }
        }, 0, 30, TimeUnit.MINUTES);
    }

    private boolean isAlreadyStartedPopulatingRepositoryForMissingExchangeRatesAllowed() {
        return !(this.isBusyPopulatingExchangeRateRepository.getAndSet(true));
    }

    private void setFinishedPopulatingExchangeRateRepository() {
        this.isBusyPopulatingExchangeRateRepository.getAndSet(false);
    }

    void populateRepositoryForMissingExchangeRates() {
        if (!isAlreadyStartedPopulatingRepositoryForMissingExchangeRatesAllowed()) {
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
