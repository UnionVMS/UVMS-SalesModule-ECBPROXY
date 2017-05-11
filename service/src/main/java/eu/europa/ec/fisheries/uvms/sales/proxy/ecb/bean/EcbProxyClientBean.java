package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.bean;

import com.google.common.base.Optional;
import eu.europa.ec.fisheries.schema.sales.proxy.ecb.types.v1.GetExchangeRateRequest;
import eu.europa.ec.fisheries.schema.sales.proxy.ecb.types.v1.GetExchangeRateResponse;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.EcbProxyClient;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.ExchangeRateCache;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.exception.EcbProxyException;
import org.joda.time.LocalDate;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.math.BigDecimal;

@Stateless
public class EcbProxyClientBean implements EcbProxyClient {

    @EJB
    private ExchangeRateCache exchangeRateCache;

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

    private BigDecimal calculateExchangeRate(String sourceCurrency, String targetCurrency, LocalDate date) {
        //find the rate in the cache
        Optional<BigDecimal> rate = exchangeRateCache.findRate(sourceCurrency, targetCurrency, date);

        if (rate.isPresent()) {
            return rate.get();
        } else {
            //maybe the inverse rate is in the cache?
            Optional<BigDecimal> inverseRate = exchangeRateCache.findRate(targetCurrency, sourceCurrency, date);

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
