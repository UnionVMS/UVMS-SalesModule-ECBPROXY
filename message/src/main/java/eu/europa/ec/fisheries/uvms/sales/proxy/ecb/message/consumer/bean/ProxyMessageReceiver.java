package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.message.consumer.bean;

import eu.europa.ec.fisheries.schema.sales.proxy.ecb.types.v1.EcbProxyBaseRequest;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageConstants;
import eu.europa.ec.fisheries.uvms.commons.message.context.MappedDiagnosticContext;
import eu.europa.ec.fisheries.uvms.commons.message.impl.JAXBUtils;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.message.constants.SalesEcbProxyMessageConstants;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.message.event.EcbProxyErrorEvent;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.message.event.EcbProxyEventMessage;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.message.event.EcbProxyGetExchangeRateEvent;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBException;

import static eu.europa.ec.fisheries.schema.sales.proxy.ecb.types.v1.EcbProxyRequestMethod.GET_EXCHANGE_RATE;

@MessageDriven(mappedName = SalesEcbProxyMessageConstants.QUEUE_JNDI, activationConfig = {
        @ActivationConfigProperty(propertyName = "messagingType", propertyValue = MessageConstants.CONNECTION_TYPE),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = MessageConstants.DESTINATION_TYPE_QUEUE),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = SalesEcbProxyMessageConstants.QUEUE_NAME)
})
@Slf4j
public class ProxyMessageReceiver implements MessageListener {

    @Inject
    @EcbProxyErrorEvent
    private Event<EcbProxyEventMessage> errorEvent;

    @Inject
    @EcbProxyGetExchangeRateEvent
    private Event<EcbProxyEventMessage> getExchangeRateEvent;

    @Override
    public void onMessage(Message message) {
        MDC.remove("requestId");
        log.debug("Received message in ProxyMessageReceiver of ECB Proxy");
        TextMessage requestMessage = null;
        try {
            requestMessage = (TextMessage) message;
            MappedDiagnosticContext.addMessagePropertiesToThreadMappedDiagnosticContext(requestMessage);
            EcbProxyBaseRequest request = JAXBUtils.unMarshallMessage(requestMessage.getText(), EcbProxyBaseRequest.class);
            if (request.getMethod() == GET_EXCHANGE_RATE) {
                getExchangeRateEvent.fire(new EcbProxyEventMessage(requestMessage, null));
                return;
            }
            String errorMessage = "Unknown method for received message in ECB Proxy, method: " + request.getMethod().name();
            log.error(errorMessage);
            errorEvent.fire(new EcbProxyEventMessage(requestMessage, errorMessage));

        } catch (JMSException | JAXBException e) {
            String errorMessage = "Invalid message received in ECB proxy";
            log.error(errorMessage);
            errorEvent.fire(new EcbProxyEventMessage(requestMessage, errorMessage));
        }
    }

}