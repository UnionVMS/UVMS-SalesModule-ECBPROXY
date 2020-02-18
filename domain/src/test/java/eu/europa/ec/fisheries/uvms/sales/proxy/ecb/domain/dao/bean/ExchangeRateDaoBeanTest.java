package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.domain.dao.bean;

import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.domain.entity.ExchangeRateEntity;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.domain.framework.AbstractDaoTest;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.domain.framework.DataSet;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.TimeZone;

import static org.junit.Assert.*;

public class ExchangeRateDaoBeanTest  extends AbstractDaoTest<ExchangeRateDaoBean> {

    static TimeZone defaultTZ;

    @BeforeClass
    public static void init() {
        defaultTZ = TimeZone.getDefault();
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @AfterClass
    public static void shutdown() {
        TimeZone.setDefault(defaultTZ);
    }

    @Test
    @DataSet(initialData = "data/ExchangeRateDaoBeanTest-testMapping-initial.xml")
    public void testFindByMostRecentRate() throws Exception {
        LocalDate expectedLocalDate = new LocalDate(2018, 1, 16);
        Optional<ExchangeRateEntity> byMostRecentRate = dao.findByMostRecentRate();
        assertTrue(byMostRecentRate.isPresent());
        ExchangeRateEntity exchangeRateEntity = byMostRecentRate.get();
        assertEquals(new Integer(3), exchangeRateEntity.getId());
        assertEquals("NZD", exchangeRateEntity.getSourceCurrency());
        assertEquals("EUR", exchangeRateEntity.getTargetCurrency());
        assertEquals(new BigDecimal("1.69"), exchangeRateEntity.getRate());
        assertEquals(expectedLocalDate.toDateTimeAtStartOfDay(DateTimeZone.getDefault()), exchangeRateEntity.getStartDateTime().withZone(DateTimeZone.getDefault()).toLocalDate().toDateTimeAtStartOfDay());
    }

    @Test
    @DataSet(initialData = "data/Empty-testMapping-initial.xml")
    public void testFindByMostRecentRateForEmptyRepository() throws Exception {
        Optional<ExchangeRateEntity> byMostRecentRate = dao.findByMostRecentRate();
        assertFalse(byMostRecentRate.isPresent());
    }

    @Test
    @DataSet(initialData = "data/ExchangeRateDaoBeanTest-testMapping-initial.xml")
    public void testFindByMostRecentTillDateForToday() throws Exception {

        LocalDate today = LocalDate.now();
        LocalDate expectedLocalDate = new LocalDate(2018, 1, 16);

        Optional<ExchangeRateEntity> byMostRecentRate = dao.findByMostRecentTillDate(today,"NZD", "EUR");
        assertTrue(byMostRecentRate.isPresent());
        ExchangeRateEntity exchangeRateEntity = byMostRecentRate.get();
        assertEquals(new Integer(3), exchangeRateEntity.getId());
        assertEquals("NZD", exchangeRateEntity.getSourceCurrency());
        assertEquals("EUR", exchangeRateEntity.getTargetCurrency());
        assertEquals(new BigDecimal("1.69"), exchangeRateEntity.getRate());
        assertEquals(expectedLocalDate.toDateTimeAtStartOfDay(DateTimeZone.getDefault()), exchangeRateEntity.getStartDateTime().withZone(DateTimeZone.getDefault()).toLocalDate().toDateTimeAtStartOfDay());
    }

    @Test
    @DataSet(initialData = "data/ExchangeRateDaoBeanTest-testMapping-initial.xml")
    public void testFindByMostRecentTillDateForDateEquals20180116() throws Exception {

        LocalDate localDate = new LocalDate(2018, 1, 16);

        Optional<ExchangeRateEntity> byMostRecentRate = dao.findByMostRecentTillDate(localDate,"NZD", "EUR");
        assertTrue(byMostRecentRate.isPresent());
        ExchangeRateEntity exchangeRateEntity = byMostRecentRate.get();
        assertEquals(new Integer(3), exchangeRateEntity.getId());
        assertEquals("NZD", exchangeRateEntity.getSourceCurrency());
        assertEquals("EUR", exchangeRateEntity.getTargetCurrency());
        assertEquals(new BigDecimal("1.69"), exchangeRateEntity.getRate());
        assertEquals(localDate.toDateTimeAtStartOfDay(DateTimeZone.getDefault()), exchangeRateEntity.getStartDateTime().withZone(DateTimeZone.getDefault()).toLocalDate().toDateTimeAtStartOfDay());
    }

    @Test
    @DataSet(initialData = "data/ExchangeRateDaoBeanTest-testMapping-initial.xml")
    public void testFindByMostRecentTillDateForDateEquals20180115() throws Exception {

        LocalDate localDate = new LocalDate(2018, 1, 15);

        Optional<ExchangeRateEntity> byMostRecentRate = dao.findByMostRecentTillDate(localDate,"NZD", "EUR");
        assertTrue(byMostRecentRate.isPresent());
        ExchangeRateEntity exchangeRateEntity = byMostRecentRate.get();
        assertEquals(new Integer(2), exchangeRateEntity.getId());
        assertEquals("NZD", exchangeRateEntity.getSourceCurrency());
        assertEquals("EUR", exchangeRateEntity.getTargetCurrency());
        assertEquals(new BigDecimal("1.67"), exchangeRateEntity.getRate());
        assertEquals(localDate.toDateTimeAtStartOfDay(DateTimeZone.getDefault()), exchangeRateEntity.getStartDateTime().withZone(DateTimeZone.getDefault()).toLocalDate().toDateTimeAtStartOfDay());
    }

    @Test
    @DataSet(initialData = "data/ExchangeRateDaoBeanTest-testMapping-initial.xml")
    public void testFindByMostRecentTillDateForDateEquals20180114() throws Exception {

        LocalDate localDate = new LocalDate(2018, 1, 14);

        Optional<ExchangeRateEntity> byMostRecentRate = dao.findByMostRecentTillDate(localDate,"NZD", "EUR");
        assertTrue(byMostRecentRate.isPresent());
        ExchangeRateEntity exchangeRateEntity = byMostRecentRate.get();
        assertEquals(new Integer(1), exchangeRateEntity.getId());
        assertEquals("NZD", exchangeRateEntity.getSourceCurrency());
        assertEquals("EUR", exchangeRateEntity.getTargetCurrency());
        assertEquals(new BigDecimal("1.68"), exchangeRateEntity.getRate());
        assertEquals(localDate.toDateTimeAtStartOfDay(DateTimeZone.getDefault()), exchangeRateEntity.getStartDateTime().withZone(DateTimeZone.getDefault()).toLocalDate().toDateTimeAtStartOfDay());
    }

    @Test
    @DataSet(initialData = "data/ExchangeRateDaoBeanTest-testMapping-initial.xml")
    public void testFindByMostRecentTillDateForDateAfter20180117() throws Exception {

        LocalDate localDate = new LocalDate(2018, 1, 17);
        LocalDate expectedLocalDate = new LocalDate(2018, 1, 16);

        Optional<ExchangeRateEntity> byMostRecentRate = dao.findByMostRecentTillDate(localDate,"NZD", "EUR");
        assertTrue(byMostRecentRate.isPresent());
        ExchangeRateEntity exchangeRateEntity = byMostRecentRate.get();
        assertEquals(new Integer(3), exchangeRateEntity.getId());
        assertEquals("NZD", exchangeRateEntity.getSourceCurrency());
        assertEquals("EUR", exchangeRateEntity.getTargetCurrency());
        assertEquals(new BigDecimal("1.69"), exchangeRateEntity.getRate());
        assertEquals(expectedLocalDate.toDateTimeAtStartOfDay(DateTimeZone.getDefault()), exchangeRateEntity.getStartDateTime().withZone(DateTimeZone.getDefault()).toLocalDate().toDateTimeAtStartOfDay());
    }

    @Test
    @DataSet(initialData = "data/ExchangeRateDaoBeanTest-testMapping-initial.xml")
    public void testFindByMostRecentTillDateForNoExchangeRateDefinedFor20140113() throws Exception {

        LocalDate localDate = new LocalDate(2018, 1, 13);

        Optional<ExchangeRateEntity> byMostRecentRate = dao.findByMostRecentTillDate(localDate,"NZD", "EUR");
        assertFalse(byMostRecentRate.isPresent());
    }

    @Test
    @DataSet(initialData = "data/Empty-testMapping-initial.xml")
    public void testFindByMostRecentTillDateForEmptyRepository() throws Exception {
        LocalDate localDate = new LocalDate(2018, 1, 16);
        Optional<ExchangeRateEntity> byMostRecentRate = dao.findByMostRecentTillDate(localDate,"NZD", "EUR");
        assertFalse(byMostRecentRate.isPresent());
    }

    @Test
    @DataSet(initialData = "data/ExchangeRateDaoBeanTest-testMapping-initial.xml")
    public void testFindByMostRecentTillDateForDateEquals20180115_Currency_HUF() throws Exception {

        LocalDate localDate = new LocalDate(2018, 1, 15);

        Optional<ExchangeRateEntity> byMostRecentRate = dao.findByMostRecentTillDate(localDate,"HUF", "EUR");
        assertTrue(byMostRecentRate.isPresent());
        ExchangeRateEntity exchangeRateEntity = byMostRecentRate.get();
        assertEquals(new Integer(4), exchangeRateEntity.getId());
        assertEquals("HUF", exchangeRateEntity.getSourceCurrency());
        assertEquals("EUR", exchangeRateEntity.getTargetCurrency());
        assertEquals(new BigDecimal("309.20"), exchangeRateEntity.getRate());
        assertEquals(localDate.toDateTimeAtStartOfDay(DateTimeZone.getDefault()), exchangeRateEntity.getStartDateTime().withZone(DateTimeZone.getDefault()).toLocalDate().toDateTimeAtStartOfDay());
    }

}
