package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.message.producer.bean;

import eu.europa.ec.fisheries.uvms.commons.message.api.MessageConstants;
import eu.europa.ec.fisheries.uvms.commons.message.impl.AbstractProducer;
import eu.europa.ec.fisheries.uvms.commons.message.impl.JMSUtils;
import eu.europa.ec.fisheries.uvms.config.exception.ConfigMessageException;
import eu.europa.ec.fisheries.uvms.config.message.ConfigMessageProducer;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.message.constants.SalesEcbProxyMessageConstants;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.Queue;

@Slf4j
@Stateless
public class ConfigMessageProducerBean extends AbstractProducer implements ConfigMessageProducer {

    private static final long TIME_TO_LIVE = 60000L;

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public String sendConfigMessage(String configMessage) throws ConfigMessageException {
        try {
            Queue replyToQueue = JMSUtils.lookupQueue(SalesEcbProxyMessageConstants.QUEUE_ECB_PROXY_CONFIG);
            return sendModuleMessageNonPersistent(configMessage, replyToQueue, TIME_TO_LIVE);

        } catch (Exception e) {
            String errorMessage = "Unable to send config messages for sales ecb proxy. Reason: " + e.getMessage();
            log.error(errorMessage);
            throw new ConfigMessageException(errorMessage);
        }
    }

    @Override
    public String getDestinationName() {
        return MessageConstants.QUEUE_CONFIG;
    }

}
