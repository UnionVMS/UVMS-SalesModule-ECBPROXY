package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.message.consumer.bean;

import eu.europa.ec.fisheries.uvms.commons.message.api.MessageConsumer;
import eu.europa.ec.fisheries.uvms.config.exception.ConfigMessageException;
import eu.europa.ec.fisheries.uvms.config.message.ConfigMessageConsumer;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
@Slf4j
public class ConfigMessageConsumerBean implements ConfigMessageConsumer {

    private static final long TIMEOUT = 30000;

    @EJB
    private MessageConsumer salesEcbProxyMessageConsumer;

    @Override
    public <T> T getConfigMessage(String correlationId, Class type) throws ConfigMessageException {
        try {
            return salesEcbProxyMessageConsumer.getMessage(correlationId, type, TIMEOUT);

        } catch (Exception e) {
            String errorMessage = "Unable to retrieve config messages for the sales ecb proxy. Reason: " + e.getMessage();
            log.error(errorMessage);
            throw new ConfigMessageException(errorMessage);
        }
    }

}
