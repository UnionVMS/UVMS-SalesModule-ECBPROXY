package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.message;

import eu.europa.ec.fisheries.uvms.commons.message.api.MessageConstants;
import eu.europa.ec.fisheries.uvms.commons.message.impl.AbstractProducer;
import eu.europa.ec.fisheries.uvms.commons.message.impl.JMSUtils;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.*;

@Slf4j
@Stateless
@LocalBean
public class ResponseMessageProducerBean extends AbstractProducer {

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void sendModuleResponseMessage(final TextMessage message, final String text, final int jmsDeliveryMode) {
        Connection connection = null;
        try {
            connection = getConnectionFactory().createConnection();
            final Session session = JMSUtils.connectToQueue(connection);

            log.info("Sending message back to recipient from  with correlationId {} on queue: {}",
                    message.getJMSMessageID(), message.getJMSReplyTo());

            final TextMessage response = session.createTextMessage(text);
            response.setJMSCorrelationID(message.getJMSMessageID());

            MessageProducer producer = session.createProducer(message.getJMSReplyTo());
            producer.setDeliveryMode(jmsDeliveryMode);
            producer.send(response);

        } catch (final JMSException e) {
            log.error("[ Error when returning request. ] {} {}", e.getMessage(), e.getStackTrace());
        } finally {
            JMSUtils.disconnectQueue(connection);
        }
    }

    @Override
    public String getDestinationName() {
        return MessageConstants.QUEUE_EXCHANGE_EVENT;
    }
}
