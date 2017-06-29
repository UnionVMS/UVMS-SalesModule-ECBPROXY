/*
﻿Developed with the contribution of the European Commission - Directorate General for Maritime Affairs and Fisheries
© European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can
redistribute it and/or modify it under the terms of the GNU General Public License as published by the
Free Software Foundation, either version 3 of the License, or any later version. The IFDM Suite is distributed in
the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details. You should have received a
copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */
package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.message;

import eu.europa.ec.fisheries.uvms.message.MessageConstants;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.event.EcbProxyErrorEvent;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.event.EcbProxyEventMessage;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.event.EcbProxySendEvent;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.exception.EcbProxyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.jms.*;

@LocalBean
@Stateless
public class ProxyMessageSender {
    
    final static Logger LOG = LoggerFactory.getLogger(ProxyMessageSender.class);
    
    @Resource(lookup = MessageConstants.CONNECTION_FACTORY)
    private ConnectionFactory connectionFactory;

    /**
     * Send method that gets triggered when an {@link EcbProxySendEvent} is fired.
     * @param event
     * @throws EcbProxyException when something goes wrong
     */
    public void sendExchangeRate(@Observes @EcbProxySendEvent EcbProxyEventMessage event) throws EcbProxyException {
        TextMessage requestMessage = event.getRequestMessage();
        try {
            sendMessage(requestMessage.getJMSReplyTo(), event.getResponseMessage(), requestMessage.getJMSMessageID());
        } catch (JMSException e) {
            throw new EcbProxyException("Could could respond with the converted currency because of a JMS problem.", e);
        }
    }

    /**
     * Send method that gets triggered when an {@link EcbProxyErrorEvent} is fired.
     * @param event
     * @throws EcbProxyException when something goes wrong
     */
    public void sendErrorMessage(@Observes @EcbProxyErrorEvent EcbProxyEventMessage event) {
        TextMessage requestMessage = event.getRequestMessage();
        try {
            sendMessage(requestMessage.getJMSReplyTo(), event.getResponseMessage(), requestMessage.getJMSMessageID());
        } catch (JMSException | EcbProxyException e) {
            LOG.error("Could could respond with the converted currency because of a JMS problem.", e);
        }
    }

    /**
     * Send a "fire and forget" message to a recipient
     *
     * @param toQueue The destination of the response
     * @param responseQueue
     * @param textMessage The actual message as a String representation of an XML
     * @param deliveryMode The delivery mode to use
     * @param defaultPriority The priority for this message
     * @param timeToLive The message's lifetime (in milliseconds)
     * @return
     * @throws EcbProxyException
     */
    public String sendMessage(Destination toQueue, Destination responseQueue, String textMessage, int deliveryMode, int defaultPriority, long timeToLive) throws EcbProxyException {
        return sendMessage(toQueue, responseQueue, textMessage, null, deliveryMode, defaultPriority, timeToLive);
    }

    /**
     * Sends a response message to a receiver. The correlationId is the
     * JMSMessage id provided in the message this method responds to.
     *
     * @param responseQueue The destination of the response
     * @param textMessage The actual message as a String representation of an
     * XML
     * @param correlationId The correlationId to set on the message that is
     * returned
     * @return The JMSMessage id of the sent message
     * @throws EcbProxyException
     */
    public String sendMessage(Destination responseQueue, String textMessage, String correlationId) throws EcbProxyException {
        return sendMessage(responseQueue, null, textMessage, correlationId, null, null, null);
    }

    /**
     *
     * Sends a JMS message to a recipient and sets the expected response queue
     *
     * @param toQueue The destination of the message
     * @param replyQueue The destination that this message should respond to
     * when arriving at the toQueue
     * @param textMessage The actual message as a String representation of an
     * XML
     * @return The JMSMessage id of the sent message
     * @throws EcbProxyException
     */
    public String sendMessage(Destination toQueue, Destination replyQueue, String textMessage) throws EcbProxyException {
        return sendMessage(toQueue, replyQueue, textMessage, null, null, null, null);
    }

    /**
     *
     * Sends a message to a JMS destination
     *
     * @param toQueue The destination of the message
     * @param replyQueue The destination that this message should respond to
     * when arriving at the toQueue
     * @param textMessage The actual message as a String representation of an
     * XML
     * @param correlationId The correlationId to set on the message that is
     * returned
     * @param deliveryMode The delivery mode to use
     * @param defaultPriority The priority for this message
     * @param timeToLive The message's lifetime (in milliseconds)
     * @return The JMSMessage id of the sent message
     * @throws EcbProxyException
     */
    private String sendMessage(Destination toQueue, Destination replyQueue, String textMessage, String correlationId, Integer deliveryMode, Integer defaultPriority, Long timeToLive) throws EcbProxyException {

        try (Connection connection = createConnection();
               Session session = getSession(connection)){
            
            LOG.info("Sending message to recipient on queue {}", toQueue);

            TextMessage message = session.createTextMessage();
            message.setText(textMessage);
            message.setJMSReplyTo(replyQueue);
            message.setJMSDestination(toQueue);
            message.setJMSCorrelationID(correlationId);
            message.setJMSDeliveryMode(DeliveryMode.NON_PERSISTENT);

            if (deliveryMode != null && defaultPriority != null && timeToLive != null) {
                session.createProducer(toQueue).send(message, deliveryMode, defaultPriority, timeToLive);
            } else {
                session.createProducer(toQueue).send(message);
            }

            return message.getJMSMessageID();
        } catch (JMSException ex) {
            LOG.error("Error when sending message JMS queue", ex);
            throw new EcbProxyException("Error when sending message JMS queue");
        }
    }

    /**
     * Gets the session that will be used to send a JMS message
     *
     * @param connection The created connection that will be used for the
     * session
     * @return The newly created session
     * @throws JMSException
     */
    protected Session getSession(Connection connection) throws JMSException {
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        connection.start();
        return session;
    }

    /**
     * Creates a connection from the connection factory provided by the
     * application server.
     *
     * @return The newly created connection
     * @throws JMSException
     */
    protected Connection createConnection() throws JMSException {
        return connectionFactory.createConnection();
    }
    
}
