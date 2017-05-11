package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.mapper;

import com.google.common.collect.Lists;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.dto.*;
import org.joda.time.LocalDate;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class GenericDataDtoMapperTest {

    @Test
    public void testMapToExchangeRates() throws Exception {
        //data set
        KeyDto seriesKey1a = new KeyDto()
                .id("FREQ")
                .value("D");

        KeyDto seriesKey1b = new KeyDto()
                .id("CURRENCY")
                .value("ARS");

        KeyDto seriesKey1c = new KeyDto()
                .id("CURRENCY_DENOM")
                .value("EUR");

        KeyDto seriesKey2a = new KeyDto()
                .id("FREQ")
                .value("D");

        KeyDto seriesKey2b = new KeyDto()
                .id("CURRENCY")
                .value("AUD");

        KeyDto seriesKey2c = new KeyDto()
                .id("CURRENCY_DENOM")
                .value("EUR");

        ObsDto seriesObs1a = new ObsDto()
                .date(new ValueDto().value("2017-01-01"))
                .rate(new ValueDto().value("1.0123"));

        ObsDto seriesObs1b = new ObsDto()
                .date(new ValueDto().value("2017-02-01"))
                .rate(new ValueDto().value("2.0123"));

        ObsDto seriesObs1c = new ObsDto()
                .date(new ValueDto().value("2017-03-01"))
                .rate(new ValueDto().value("3.0123"));

        ObsDto seriesObs2a = new ObsDto()
                .date(new ValueDto().value("2017-01-01"))
                .rate(new ValueDto().value("4.0123"));

        ObsDto seriesObs2b = new ObsDto()
                .date(new ValueDto().value("2017-02-01"))
                .rate(new ValueDto().value("something weird"));

        ObsDto seriesObs2c = new ObsDto()
                .date(new ValueDto().value("2017-03-01"))
                .rate(new ValueDto().value("NaN"));

        SeriesKeyDto seriesKey1 = new SeriesKeyDto()
                .keys(Lists.newArrayList(seriesKey1a, seriesKey1b, seriesKey1c));

        SeriesKeyDto seriesKey2 = new SeriesKeyDto()
                .keys(Lists.newArrayList(seriesKey2a, seriesKey2b, seriesKey2c));

        SeriesDto series1 = new SeriesDto()
                .seriesKey(seriesKey1)
                .obs(Lists.newArrayList(seriesObs1a, seriesObs1b, seriesObs1c));

        SeriesDto series2 = new SeriesDto()
                .seriesKey(seriesKey2)
                .obs(Lists.newArrayList(seriesObs2a, seriesObs2b, seriesObs2c));

        DataSetDto dataSet = new DataSetDto()
                .series(Lists.newArrayList(series1, series2));

        GenericDataDto genericData = new GenericDataDto()
            .dataSet(dataSet);

        //execute
        List<ExchangeRate> exchangeRates = GenericDataDtoMapper.mapToExchangeRates(genericData);

        //assert
        assertEquals(4, exchangeRates.size());

        ExchangeRate exchangeRate1 = exchangeRates.get(0);
        assertEquals(new LocalDate(2017, 1, 1), exchangeRate1.getKey().getDate());
        assertEquals("ARS", exchangeRate1.getKey().getSourceCurrency());
        assertEquals("EUR", exchangeRate1.getKey().getTargetCurrency());
        assertEquals(BigDecimal.valueOf(1.0123), exchangeRate1.getRate().stripTrailingZeros());

        ExchangeRate exchangeRate2 = exchangeRates.get(1);
        assertEquals(new LocalDate(2017, 2, 1), exchangeRate2.getKey().getDate());
        assertEquals("ARS", exchangeRate2.getKey().getSourceCurrency());
        assertEquals("EUR", exchangeRate2.getKey().getTargetCurrency());
        assertEquals(BigDecimal.valueOf(2.0123), exchangeRate2.getRate().stripTrailingZeros());

        ExchangeRate exchangeRate3 = exchangeRates.get(2);
        assertEquals(new LocalDate(2017, 3, 1), exchangeRate3.getKey().getDate());
        assertEquals("ARS", exchangeRate3.getKey().getSourceCurrency());
        assertEquals("EUR", exchangeRate3.getKey().getTargetCurrency());
        assertEquals(BigDecimal.valueOf(3.0123), exchangeRate3.getRate().stripTrailingZeros());

        ExchangeRate exchangeRate4 = exchangeRates.get(3);
        assertEquals(new LocalDate(2017, 1, 1), exchangeRate4.getKey().getDate());
        assertEquals("AUD", exchangeRate4.getKey().getSourceCurrency());
        assertEquals("EUR", exchangeRate4.getKey().getTargetCurrency());
        assertEquals(BigDecimal.valueOf(4.0123), exchangeRate4.getRate().stripTrailingZeros());
    }

}