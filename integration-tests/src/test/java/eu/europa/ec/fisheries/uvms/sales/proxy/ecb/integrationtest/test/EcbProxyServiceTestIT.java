package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.integrationtest.test;

import com.google.common.base.Optional;
import eu.europa.ec.fisheries.schema.sales.proxy.ecb.types.v1.GetExchangeRateResponse;
import eu.europa.ec.fisheries.uvms.commons.message.impl.JAXBUtils;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.integrationtest.deployment.TestOnGoodWorkingEcbProxy;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.integrationtest.test.factory.SalesEcbProxyMessageFactory;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.integrationtest.test.helper.SalesEcbProxyServiceTestHelper;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.service.ExchangeRateService;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.persistence.Cleanup;
import org.jboss.arquillian.persistence.DataSource;
import org.jboss.arquillian.persistence.TestExecutionPhase;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.jms.TextMessage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
public class EcbProxyServiceTestIT extends TestOnGoodWorkingEcbProxy {

    static final Logger LOG = LoggerFactory.getLogger(EcbProxyServiceTestIT.class);

    @EJB
    ExchangeRateService exchangeRateService;

    @EJB
    SalesEcbProxyMessageFactory salesEcbProxyMessageFactory;

    @EJB
    SalesEcbProxyServiceTestHelper salesEcbProxyServiceTestHelper;

    @InSequence(1)
    @Test
    @OperateOnDeployment("good-working-ecb-proxy")
    @Transactional(TransactionMode.DISABLED)
    @DataSource("java:/jdbc/uvms_salesecbproxy")
    @UsingDataSet("datasets/exchange-rate-empty-dataset.xml")
    @Cleanup(phase = TestExecutionPhase.NONE)
    public void testSelfPopulatingFromEcbWsEndpointAndGetExchangeRate() throws Exception {
        //Data
        String expectedGetExchangeRateResponse = salesEcbProxyMessageFactory.composeGetExchangeRateResponseAsString();

        //Give the Sales ecb proxy some time to boot itself properly
        Thread.sleep(60000);

        //Use case 1: Assert Self populating ECB proxy repo using ECB WS endpoint
        Optional<LocalDate> mostRecentExchangeRateDateOptional = exchangeRateService.getMostRecentExchangeRateDate();
        assertTrue(mostRecentExchangeRateDateOptional.isPresent());
        DateTime tillDateTime = DateTime.now();
        DateTime fromDateTime = tillDateTime.minusDays(5);
        Interval interval = new Interval(fromDateTime, tillDateTime);
        interval.contains(mostRecentExchangeRateDateOptional.get().toDateTimeAtStartOfDay());


        //Use case 2: Get exchange rate for most recent date

        //Execute
        String strGetExchangeRateRequest = salesEcbProxyMessageFactory.composeGetExchangeRateRequestAsString(DateTime.now());
        String jmsMessageId = salesEcbProxyServiceTestHelper.sendMessageToSalesEcbProxyMessageConsumerBean(strGetExchangeRateRequest, salesEcbProxyServiceTestHelper.getReplyToSalesQueue());

        // Assert, receive GetExchangeRateResponse
        TextMessage textMessage = salesEcbProxyServiceTestHelper.receiveTextMessage(salesEcbProxyServiceTestHelper.getReplyToSalesQueue(), jmsMessageId);
        GetExchangeRateResponse getExchangeRateResponse = JAXBUtils.unMarshallMessage(textMessage.getText(), GetExchangeRateResponse.class);
        assertEquals("DKK", getExchangeRateResponse.getSourceCurrency());
        assertEquals("EUR", getExchangeRateResponse.getTargetCurrency());
        assertEquals(LocalDate.now().toDateTimeAtStartOfDay().toDate(), getExchangeRateResponse.getDate().toDate());
        assertEquals(1, getExchangeRateResponse.getExchangeRate().signum()); // if greater than zero
    }

}
