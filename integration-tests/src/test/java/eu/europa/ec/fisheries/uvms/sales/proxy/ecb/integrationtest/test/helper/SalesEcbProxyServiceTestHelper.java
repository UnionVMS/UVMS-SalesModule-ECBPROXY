package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.integrationtest.test.helper;

import eu.europa.ec.fisheries.uvms.commons.message.api.MessageConstants;
import eu.europa.ec.fisheries.uvms.commons.message.impl.JMSUtils;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.jms.*;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

@Slf4j
@Singleton
public class SalesEcbProxyServiceTestHelper {

    private static final long TIMEOUT = 60000;

    private Queue salesEcbProxyEventQueue;
    private Queue replyToSalesQueue;
    private ConnectionFactory connectionFactory;

    @PostConstruct
    public void setup() {
        salesEcbProxyEventQueue = JMSUtils.lookupQueue(MessageConstants.QUEUE_ECB_PROXY);
        replyToSalesQueue = JMSUtils.lookupQueue(MessageConstants.QUEUE_SALES);
    }

    public String sendMessageToSalesEcbProxyMessageConsumerBean(String messageToSend, Destination replyToQueue) {

        try (Connection connection = JMSUtils.getConnectionV2();
             Session session = JMSUtils.createSessionAndStartConnection(connection);
             MessageProducer producer = session.createProducer(salesEcbProxyEventQueue)) {

            TextMessage textMessage = session.createTextMessage(messageToSend);
            textMessage.setJMSReplyTo(replyToQueue);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            producer.setTimeToLive(60000L);
            producer.send(textMessage);
            return textMessage.getJMSMessageID();

        } catch (Exception e) {
            log.error("Test should not fail for consume JMS message exception: " + e.getMessage());
            throw new RuntimeException("Unable to send message to sales message consumber bean. Reason: " + e.getMessage());
        }
    }


    public TextMessage receiveTextMessage(Destination receiveFromDestination, String correlationId) {
        assertNotNull(correlationId);
        try (Connection connection = JMSUtils.getConnectionV2();
            Session session = JMSUtils.createSessionAndStartConnection(connection);
            MessageConsumer consumer = session.createConsumer(receiveFromDestination, "JMSCorrelationID='" + correlationId + "'")) {
            Message receivedMessage = consumer.receive(TIMEOUT);
            if (receivedMessage == null) {
                log.error("Message consumer timeout is reached");
                return null;
            }
            return (TextMessage) receivedMessage;

        } catch (Exception e) {
            fail("Test should not fail for UniqueIdReceived consumer JMS message exception: " + e.getMessage());
            return null;
        }
    }

    public Destination getReplyToSalesQueue() {
        return replyToSalesQueue;
    }

}
