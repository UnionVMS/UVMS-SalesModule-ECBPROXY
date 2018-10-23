package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.mapper;

import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.dto.ExchangeRate;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.sdmx.resources.sdmxml.schemas.v2_1.data.generic.*;
import org.sdmx.resources.sdmxml.schemas.v2_1.message.GenericDataType;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GenericDataDtoMapperTest {

    @Test
    public void testMapToExchangeRates() {
        //data set
        ComponentValueType seriesKey1a = new ComponentValueType()
                .withId("FREQ")
                .withValue("D");

        ComponentValueType seriesKey1b = new ComponentValueType()
                .withId("CURRENCY")
                .withValue("ARS");

        ComponentValueType seriesKey1c = new ComponentValueType()
                .withId("CURRENCY_DENOM")
                .withValue("EUR");

        ComponentValueType seriesKey2a = new ComponentValueType()
                .withId("FREQ")
                .withValue("D");

        ComponentValueType seriesKey2b = new ComponentValueType()
                .withId("CURRENCY")
                .withValue("AUD");

        ComponentValueType seriesKey2c = new ComponentValueType()
                .withId("CURRENCY_DENOM")
                .withValue("EUR");

        ObsType seriesObs1a = new ObsType()
                .withObsDimension(new ObsValueType().withValue("2017-01-01"))
                .withObsValue(new ObsValueType().withValue("1.0123"));

        ObsType seriesObs1b = new ObsType()
                .withObsDimension(new ObsValueType().withValue("2017-02-01"))
                .withObsValue(new ObsValueType().withValue("2.0123"));

        ObsType seriesObs1c = new ObsType()
                .withObsDimension(new ObsValueType().withValue("2017-03-01"))
                .withObsValue(new ObsValueType().withValue("3.0123"));

        ObsType seriesObs2a = new ObsType()
                .withObsDimension(new ObsValueType().withValue("2017-01-01"))
                .withObsValue(new ObsValueType().withValue("4.0123"));

        ObsType seriesObs2b = new ObsType()
                .withObsDimension(new ObsValueType().withValue("2017-02-01"))
                .withObsValue(new ObsValueType().withValue("something weird"));

        ObsType seriesObs2c = new ObsType()
                .withObsDimension(new ObsValueType().withValue("2017-03-01"))
                .withObsValue(new ObsValueType().withValue("NaN"));

        ValuesType seriesKey1 = new ValuesType()
                .withValue(seriesKey1a, seriesKey1b, seriesKey1c);

        ValuesType seriesKey2 = new ValuesType()
                .withValue(seriesKey2a, seriesKey2b, seriesKey2c);

        SeriesType series1 = new SeriesType()
                .withSeriesKey(seriesKey1)
                .withObs(seriesObs1a, seriesObs1b, seriesObs1c);

        SeriesType series2 = new SeriesType()
                .withSeriesKey(seriesKey2)
                .withObs(seriesObs2a, seriesObs2b, seriesObs2c);

        DataSetType dataSet = new DataSetType()
                .withSeries(series1, series2);

        GenericDataType genericData = new GenericDataType()
            .withDataSet(dataSet);

        //execute
        List<ExchangeRate> exchangeRates = GenericDataDtoMapper.mapToExchangeRates(genericData);

        //assert
        assertEquals(4, exchangeRates.size());

        ExchangeRate exchangeRate1 = exchangeRates.get(0);
        assertEquals(new LocalDate(2017, 1, 1), exchangeRate1.getStartDate());
        assertEquals("ARS", exchangeRate1.getSourceCurrency());
        assertEquals("EUR", exchangeRate1.getTargetCurrency());
        assertEquals(BigDecimal.valueOf(1.0123), exchangeRate1.getRate().stripTrailingZeros());

        ExchangeRate exchangeRate2 = exchangeRates.get(1);
        assertEquals(new LocalDate(2017, 2, 1), exchangeRate2.getStartDate());
        assertEquals("ARS", exchangeRate2.getSourceCurrency());
        assertEquals("EUR", exchangeRate2.getTargetCurrency());
        assertEquals(BigDecimal.valueOf(2.0123), exchangeRate2.getRate().stripTrailingZeros());

        ExchangeRate exchangeRate3 = exchangeRates.get(2);
        assertEquals(new LocalDate(2017, 3, 1), exchangeRate3.getStartDate());
        assertEquals("ARS", exchangeRate3.getSourceCurrency());
        assertEquals("EUR", exchangeRate3.getTargetCurrency());
        assertEquals(BigDecimal.valueOf(3.0123), exchangeRate3.getRate().stripTrailingZeros());

        ExchangeRate exchangeRate4 = exchangeRates.get(3);
        assertEquals(new LocalDate(2017, 1, 1), exchangeRate4.getStartDate());
        assertEquals("AUD", exchangeRate4.getSourceCurrency());
        assertEquals("EUR", exchangeRate4.getTargetCurrency());
        assertEquals(BigDecimal.valueOf(4.0123), exchangeRate4.getRate().stripTrailingZeros());
    }

    @Test
    public void testMapToExchangeRateForSingleRate() {
        //data set
        ComponentValueType seriesKey1a = new ComponentValueType()
                .withId("FREQ")
                .withValue("D");

        ComponentValueType seriesKey1b = new ComponentValueType()
                .withId("CURRENCY")
                .withValue("ARS");

        ComponentValueType seriesKey1c = new ComponentValueType()
                .withId("CURRENCY_DENOM")
                .withValue("EUR");

        ObsType seriesObs1a = new ObsType()
                .withObsDimension(new ObsValueType().withValue("2017-01-01"))
                .withObsValue(new ObsValueType().withValue("1.0123"));

        ValuesType seriesKey1 = new ValuesType()
                .withValue(seriesKey1a, seriesKey1b, seriesKey1c);

        SeriesType series1 = new SeriesType()
                .withSeriesKey(seriesKey1)
                .withObs(seriesObs1a);

        DataSetType dataSet = new DataSetType()
                .withSeries(series1);

        GenericDataType genericData = new GenericDataType()
                .withDataSet(dataSet);

        //execute
        List<ExchangeRate> exchangeRates = GenericDataDtoMapper.mapToExchangeRates(genericData);

        //assert
        assertEquals(1, exchangeRates.size());

        ExchangeRate exchangeRate1 = exchangeRates.get(0);
        assertEquals(new LocalDate(2017, 1, 1), exchangeRate1.getStartDate());
        assertEquals("ARS", exchangeRate1.getSourceCurrency());
        assertEquals("EUR", exchangeRate1.getTargetCurrency());
        assertEquals(BigDecimal.valueOf(1.0123), exchangeRate1.getRate().stripTrailingZeros());
    }



    @Test
    public void tryMapToExchangeRateForSingleRateNoCurrencies() {
        //data set
        ObsType seriesObs1a = new ObsType()
                .withObsDimension(new ObsValueType().withValue("2017-01-01"))
                .withObsValue(new ObsValueType().withValue("1.0123"));

        ValuesType seriesKey1 = new ValuesType();

        SeriesType series1 = new SeriesType()
                .withSeriesKey(seriesKey1)
                .withObs(seriesObs1a);

        DataSetType dataSet = new DataSetType()
                .withSeries(series1);

        GenericDataType genericData = new GenericDataType()
                .withDataSet(dataSet);

        //execute
        List<ExchangeRate> exchangeRates = GenericDataDtoMapper.mapToExchangeRates(genericData);

        //assert
        assertTrue(exchangeRates.isEmpty());
    }

    @Test
    public void tryMapToExchangeRateForSingleRateAndSourceCurrencyOnly() {
        //data set
        ComponentValueType seriesKey1b = new ComponentValueType()
                .withId("CURRENCY")
                .withValue("ARS");

        ObsType seriesObs1a = new ObsType()
                .withObsDimension(new ObsValueType().withValue("2017-01-01"))
                .withObsValue(new ObsValueType().withValue("1.0123"));

        ValuesType seriesKey1 = new ValuesType()
                .withValue(seriesKey1b);

        SeriesType series1 = new SeriesType()
                .withSeriesKey(seriesKey1)
                .withObs(seriesObs1a);

        DataSetType dataSet = new DataSetType()
                .withSeries(series1);

        GenericDataType genericData = new GenericDataType()
                .withDataSet(dataSet);

        //execute
        List<ExchangeRate> exchangeRates = GenericDataDtoMapper.mapToExchangeRates(genericData);

        //assert
        assertTrue(exchangeRates.isEmpty());
    }

    @Test
    public void tryMapToExchangeRateForSingleRateAndTargetCurrencyOnly() {
        //data set
        ComponentValueType seriesKey1c = new ComponentValueType()
                .withId("CURRENCY_DENOM")
                .withValue("EUR");

        ObsType seriesObs1a = new ObsType()
                .withObsDimension(new ObsValueType().withValue("2017-01-01"))
                .withObsValue(new ObsValueType().withValue("1.0123"));

        ValuesType seriesKey1 = new ValuesType()
                .withValue(seriesKey1c);

        SeriesType series1 = new SeriesType()
                .withSeriesKey(seriesKey1)
                .withObs(seriesObs1a);

        DataSetType dataSet = new DataSetType()
                .withSeries(series1);

        GenericDataType genericData = new GenericDataType()
                .withDataSet(dataSet);

        //execute
        List<ExchangeRate> exchangeRates = GenericDataDtoMapper.mapToExchangeRates(genericData);

        //assert
        assertTrue(exchangeRates.isEmpty());
    }
}
