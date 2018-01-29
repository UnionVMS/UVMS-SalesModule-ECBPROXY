package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.message.consumer.bean;

import eu.europa.ec.fisheries.uvms.commons.message.api.MessageConsumer;
import eu.europa.ec.fisheries.uvms.config.exception.ConfigMessageException;
import eu.europa.ec.fisheries.uvms.config.message.ConfigMessageConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class ConfigMessageConsumerBean implements ConfigMessageConsumer {

    private static final long TIMEOUT = 10000;

    static final Logger LOG = LoggerFactory.getLogger(ConfigMessageConsumerBean.class);

    @EJB
    private MessageConsumer salesEcbProxyMessageConsumer;

    @Override
    public <T> T getConfigMessage(String correlationId, Class type) throws ConfigMessageException {
        try {
            return salesEcbProxyMessageConsumer.getMessage(correlationId, type, TIMEOUT);

        } catch (Exception e) {
            String errorMessage = "Unable to retrieve config messages for the sales ecb proxy. Reason: " + e.getMessage();
            LOG.error(errorMessage);
            throw new ConfigMessageException(errorMessage);
        }
    }

}
