package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.integrationtest.test;

import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.integrationtest.deployment.TestOnStaticDataEcbProxy;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.integrationtest.test.factory.SalesEcbProxyMessageFactory;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.integrationtest.test.helper.SalesEcbProxyServiceTestHelper;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.integrationtest.test.xmlunit.XmlUtil;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.service.ExchangeRateService;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.persistence.*;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.jms.TextMessage;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class StaticDataEcbProxyServiceTestIT extends TestOnStaticDataEcbProxy {

    static final Logger LOG = LoggerFactory.getLogger(StaticDataEcbProxyServiceTestIT.class);

    @EJB
    ExchangeRateService exchangeRateService;

    @EJB
    SalesEcbProxyMessageFactory salesEcbProxyMessageFactory;

    @EJB
    SalesEcbProxyServiceTestHelper salesEcbProxyServiceTestHelper;

    @InSequence(1)
    @Test
    @OperateOnDeployment("static-data-ecb-proxy")
    @Transactional(TransactionMode.DISABLED)
    @DataSource("java:/jdbc/uvms_salesecbproxy")
    @UsingDataSet("datasets/exchange-rate-dataset.xml")
    @Cleanup(phase = TestExecutionPhase.BEFORE, strategy = CleanupStrategy.USED_TABLES_ONLY)
    public void testGetExchangeRate() throws Exception {
        //Data
        String strGetExchangeRateRequest = salesEcbProxyMessageFactory.composeGetExchangeRateRequestAsString();
        String expectedGetExchangeRateResponse = salesEcbProxyMessageFactory.composeGetExchangeRateResponseAsString();

        //Give the Sales ecb proxy some time to boot itself properly
        Thread.sleep(5000);

        //Execute
        String jmsMessageId = salesEcbProxyServiceTestHelper.sendMessageToSalesEcbProxyMessageConsumerBean(strGetExchangeRateRequest, salesEcbProxyServiceTestHelper.getReplyToSalesQueue());

        // Assert, receive GetExchangeRateResponse
        TextMessage textMessage = salesEcbProxyServiceTestHelper.receiveTextMessage(salesEcbProxyServiceTestHelper.getReplyToSalesQueue(), jmsMessageId);
        XmlUtil.equalsXML(expectedGetExchangeRateResponse, textMessage.getText());
    }

    @InSequence(2)
    @Test
    @OperateOnDeployment("static-data-ecb-proxy")
    @Transactional(TransactionMode.DISABLED)
    @DataSource("java:/jdbc/uvms_salesecbproxy")
    @UsingDataSet("datasets/exchange-rate-empty-dataset.xml")
    @Cleanup(phase = TestExecutionPhase.BEFORE, strategy = CleanupStrategy.USED_TABLES_ONLY)
    public void tryGetExchangeRateForUnknownExchangeRate() throws Exception {
        //Data
        String strGetExchangeRateRequest = salesEcbProxyMessageFactory.composeGetExchangeRateRequestAsString();
        String expectedGetExchangeRateResponse = "Unable to process get exchange rate request in ECB proxy. Reason: No known exchange rate for DKK to EUR on 2018-08-12";

        //Give the Sales ecb proxy some time to boot itself properly
        Thread.sleep(5000);

        //Execute
        String jmsMessageId = salesEcbProxyServiceTestHelper.sendMessageToSalesEcbProxyMessageConsumerBean(strGetExchangeRateRequest, salesEcbProxyServiceTestHelper.getReplyToSalesQueue());

        // Assert, receive GetExchangeRateResponse
        TextMessage textMessage = salesEcbProxyServiceTestHelper.receiveTextMessage(salesEcbProxyServiceTestHelper.getReplyToSalesQueue(), jmsMessageId);
        assertEquals(expectedGetExchangeRateResponse, textMessage.getText());
    }

}
