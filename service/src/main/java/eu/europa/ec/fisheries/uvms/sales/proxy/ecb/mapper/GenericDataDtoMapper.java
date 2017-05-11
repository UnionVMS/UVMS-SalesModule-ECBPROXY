package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.mapper;

import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.dto.*;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GenericDataDtoMapper {

    private static final Logger LOG = LoggerFactory.getLogger(GenericDataDtoMapper.class);

    public static List<ExchangeRate> mapToExchangeRates(GenericDataDto genericDataDto) {
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        String sourceCurrency = null;
        String targetCurrency = null;
        BigDecimal rate;
        LocalDate date;


        for (SeriesDto s : genericDataDto.getDataSet().getSeries()) {
            try {
                for (KeyDto key : s.getSeriesKey().getKeys()) {
                    switch (key.getId()) {
                        case "CURRENCY":
                            sourceCurrency = key.getValue();
                            break;
                        case "CURRENCY_DENOM":
                            targetCurrency = key.getValue();
                            break;
                    }
                }

                for (ObsDto obs : s.getObs()) {
                    try {
                        String rateAsString = obs.getRate().getValue();
                        if (!rateAsString.equals("NaN")) {
                            rate = new BigDecimal(rateAsString);
                            date = new LocalDate(obs.getDate().getValue());

                            exchangeRates.add(new ExchangeRate(rate, sourceCurrency, targetCurrency, date));
                        }
                    } catch (RuntimeException e) {
                        LOG.error("Could not add one rate to the cache, because the data seems invalid", e);
                    }
                }
            } catch (RuntimeException e) {
                LOG.error("Could not add one or more rates to the cache, because the data seems invalid", e);
            }
        }

        return exchangeRates;
    }

}
