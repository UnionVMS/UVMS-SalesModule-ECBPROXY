package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.mapper;

import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.dto.ExchangeRate;
import org.joda.time.LocalDate;
import org.sdmx.resources.sdmxml.schemas.v2_1.data.generic.ComponentValueType;
import org.sdmx.resources.sdmxml.schemas.v2_1.data.generic.DataSetType;
import org.sdmx.resources.sdmxml.schemas.v2_1.data.generic.ObsType;
import org.sdmx.resources.sdmxml.schemas.v2_1.data.generic.SeriesType;
import org.sdmx.resources.sdmxml.schemas.v2_1.message.GenericDataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenericDataDtoMapper {

    private static final Logger LOG = LoggerFactory.getLogger(GenericDataDtoMapper.class);

    private static final String KEY_CURRENCY = "CURRENCY";
    private static final String KEY_CURRENCY_DENOM = "CURRENCY_DENOM";
    private static final String NOT_A_NUMBER = "NaN";

    public static List<ExchangeRate> mapToExchangeRates(GenericDataType genericData) {
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        for (DataSetType dataSet : genericData.getDataSet()) {
            for (SeriesType s : dataSet.getSeries()) {
                try {
                    Map<String, String> currencies = getCurrencies(s);
                    String sourceCurrency = currencies.get(KEY_CURRENCY);
                    if (sourceCurrency == null) {
                        LOG.info("Source currency should not be null");
                        continue;
                    }
                    String targetCurrency = currencies.get(KEY_CURRENCY_DENOM);
                    if (targetCurrency == null) {
                        LOG.info("Target currency should not be null");
                        continue;
                    }
                    exchangeRates.addAll(getExchangeRates(s, sourceCurrency, targetCurrency));
                } catch (RuntimeException e) {
                    LOG.error("Could not add one or more rates to the cache, because the data seems invalid", e);
                }
            }
        }
        return exchangeRates;
    }

    private static List<ExchangeRate> getExchangeRates(SeriesType s, String sourceCurrency, String targetCurrency) {
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        for (ObsType obs : s.getObs()) {
            try {
                String rateAsString = obs.getObsValue().getValue();
                if (!rateAsString.equals(NOT_A_NUMBER)) {
                    BigDecimal rate = new BigDecimal(rateAsString);
                    LocalDate date = new LocalDate(obs.getObsDimension().getValue());
                    exchangeRates.add(new ExchangeRate(rate, sourceCurrency, targetCurrency, date));
                }
            } catch (RuntimeException e) {
                LOG.error("Could not add one rate to the cache, because the data seems invalid", e);
            }
        }
        return exchangeRates;
    }

    private static Map<String, String> getCurrencies(SeriesType s) {
        Map<String, String> currencies = new HashMap<>();
        boolean foundCurrency = false;
        boolean foundCurrencyDenom = false;
        if ((s.getSeriesKey() == null)
                || (s.getSeriesKey().getValue() == null)) {
            return currencies;
        }
        for (ComponentValueType key : s.getSeriesKey().getValue()) {
            if ((!foundCurrency)
                    && (KEY_CURRENCY.equals(key.getId()))) {
                currencies.put(KEY_CURRENCY, key.getValue());
                if (foundCurrencyDenom) {
                    return currencies;
                }
                foundCurrency = true;
                continue;
            }
            if ((!foundCurrencyDenom)
                    && (KEY_CURRENCY_DENOM.equals(key.getId()))) {
                currencies.put(KEY_CURRENCY_DENOM, key.getValue());
                if (foundCurrency) {
                    return currencies;
                }
                foundCurrencyDenom = true;
            }
        }
        return currencies;
    }

    private GenericDataDtoMapper() { }

}
