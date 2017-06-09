package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.message;

import eu.europa.ec.fisheries.schema.sales.proxy.ecb.types.v1.EcbProxyBaseRequest;
import eu.europa.ec.fisheries.schema.sales.proxy.ecb.types.v1.GetExchangeRateRequest;
import eu.europa.ec.fisheries.schema.sales.proxy.ecb.types.v1.GetExchangeRateResponse;
import eu.europa.ec.fisheries.uvms.message.MessageConstants;
import eu.europa.ec.fisheries.uvms.sales.model.exception.SalesMarshallException;
import eu.europa.ec.fisheries.uvms.sales.model.mapper.JAXBMarshaller;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.EcbProxyClient;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.constant.EcbProxyMessageConstants;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.event.EcbProxyErrorEvent;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.event.EcbProxyEventMessage;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.event.EcbProxySendEvent;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.exception.EcbProxyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@MessageDriven(mappedName = EcbProxyMessageConstants.QUEUE_JNDI, activationConfig = {
        @ActivationConfigProperty(propertyName = "messagingType", propertyValue = MessageConstants.CONNECTION_TYPE),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = MessageConstants.DESTINATION_TYPE_QUEUE),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = EcbProxyMessageConstants.QUEUE_NAME)
})
public class ProxyMessageReceiver implements MessageListener {

    private static final Logger LOG = LoggerFactory.getLogger(ProxyMessageReceiver.class);

    @Inject
    @EcbProxySendEvent
    private Event<EcbProxyEventMessage> sendBackEvent;

    @Inject
    @EcbProxyErrorEvent
    private Event<EcbProxyEventMessage> errorEvent;

    @EJB
    private EcbProxyClient client;

    @Override
    public void onMessage(Message message) {
        LOG.debug("Received message in ProxyMessageReceiver of ECB Proxy");

        TextMessage requestMessage = (TextMessage) message;

        try {
            EcbProxyBaseRequest request = JAXBMarshaller.unmarshallTextMessage(requestMessage, EcbProxyBaseRequest.class);

            switch (request.getMethod()) {
                case GET_EXCHANGE_RATE:
                    GetExchangeRateRequest convertCurrencyRequest = JAXBMarshaller.unmarshallTextMessage(requestMessage, GetExchangeRateRequest.class);
                    GetExchangeRateResponse convertCurrencyResponse = client.getExchangeRate(convertCurrencyRequest);

                    String responseAsString = JAXBMarshaller.marshallJaxBObjectToString(convertCurrencyResponse);
                    sendBackEvent.fire(new EcbProxyEventMessage(requestMessage, responseAsString));
                    break;
                default:
                    LOG.error("Error when receiving a message in the ECB Proxy module, method {} not implemented ] {}", request.getMethod().name());
                    throw new EcbProxyException("Method " + request.getMethod().name() + " implemented!");

            }
        } catch (SalesMarshallException | EcbProxyException e) {
            LOG.error("Something went wrong interpreting a received message in the ECB proxy", e);
            errorEvent.fire(new EcbProxyEventMessage(requestMessage, e.getMessage()));
        }
    }


}