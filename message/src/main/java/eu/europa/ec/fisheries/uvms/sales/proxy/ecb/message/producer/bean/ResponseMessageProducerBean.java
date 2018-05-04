package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.message.producer.bean;

import eu.europa.ec.fisheries.uvms.commons.message.api.MessageConstants;
import eu.europa.ec.fisheries.uvms.commons.message.impl.AbstractProducer;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Slf4j
@Stateless
@LocalBean
public class ResponseMessageProducerBean extends AbstractProducer {
    @Override
    public String getDestinationName() {
        return MessageConstants.QUEUE_EXCHANGE_EVENT;
    }
}
