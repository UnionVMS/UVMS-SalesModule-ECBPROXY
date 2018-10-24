package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.service.bean;

import eu.europa.ec.fisheries.schema.sales.proxy.ecb.types.v1.GetExchangeRateRequest;
import eu.europa.ec.fisheries.schema.sales.proxy.ecb.types.v1.GetExchangeRateResponse;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.exception.EcbProxyException;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.service.EcbProxyClient;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.service.ExchangeRateService;
import org.joda.time.LocalDate;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.math.BigDecimal;
import java.util.Optional;

@Stateless
public class EcbProxyClientBean implements EcbProxyClient {

    @EJB
    private ExchangeRateService exchangeRateService;

    @Override
    public GetExchangeRateResponse getExchangeRate(GetExchangeRateRequest request) throws EcbProxyException {
        String sourceCurrency = request.getSourceCurrency();
        String targetCurrency = request.getTargetCurrency();
        LocalDate date = request.getDate().toLocalDate();

        BigDecimal exchangeRate = calculateExchangeRate(sourceCurrency, targetCurrency, date);

        if (exchangeRate != null) {
            return new GetExchangeRateResponse()
                    .withSourceCurrency(sourceCurrency)
                    .withDate(date.toDateTimeAtStartOfDay())
                    .withTargetCurrency(targetCurrency)
                    .withExchangeRate(exchangeRate);
        } else {
            throw new EcbProxyException("No known exchange rate for " + sourceCurrency + " to " + targetCurrency + " on " + date);
        }
    }

    private BigDecimal calculateExchangeRate(String sourceCurrency, String targetCurrency, LocalDate date) throws EcbProxyException {
        //find the rate in the cache
        Optional<BigDecimal> rate = exchangeRateService.findRateMostRecentTillDate(date, sourceCurrency, targetCurrency);
        if (rate.isPresent()) {
            return rate.get();
        } else {
            //maybe the inverse rate is in the cache?
            Optional<BigDecimal> inverseRate = exchangeRateService.findRateMostRecentTillDate(date, targetCurrency, sourceCurrency);

            if (inverseRate.isPresent()) {
                return BigDecimal.ONE.setScale(4)
                        .divide(inverseRate.get());
            } else {
                //meh, nothing found
                return null;
            }
        }
    }
}
