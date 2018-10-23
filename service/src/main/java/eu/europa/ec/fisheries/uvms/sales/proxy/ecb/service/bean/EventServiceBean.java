package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.service.bean;

import eu.europa.ec.fisheries.schema.sales.proxy.ecb.types.v1.GetExchangeRateRequest;
import eu.europa.ec.fisheries.schema.sales.proxy.ecb.types.v1.GetExchangeRateResponse;
import eu.europa.ec.fisheries.uvms.commons.message.impl.JAXBUtils;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.message.event.EcbProxyErrorEvent;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.message.event.EcbProxyEventMessage;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.message.event.EcbProxyGetExchangeRateEvent;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.message.producer.bean.ResponseMessageProducerBean;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.service.EcbProxyClient;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.service.EventService;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.xml.bind.JAXBException;

@Slf4j
@Stateless
public class EventServiceBean implements EventService {

    @EJB
    ResponseMessageProducerBean responseMessageProducerBean;

    @EJB
    private EcbProxyClient client;

    @Override
    public void getExchangeRate(@Observes @EcbProxyGetExchangeRateEvent EcbProxyEventMessage event) {
        log.info("Get exchange rate");
        GetExchangeRateResponse getExchangeRateResponse;
        try {
            getExchangeRateResponse = client.getExchangeRate(getGetExchangeRateRequest(event));
        } catch (Exception e) {
            String errorMessage = "Unable to process get exchange rate request in ECB proxy. Reason: " + e.getMessage();
            log.error(errorMessage, e);
            sendErrorResponse(event, errorMessage);
            return;
        }

        try {
            log.info("Send exchange rate response");
            responseMessageProducerBean.sendResponseMessageToSender(event.getRequestMessage(),
                    getExchangeRateResponseAsString(getExchangeRateResponse), 60000, DeliveryMode.NON_PERSISTENT);

        } catch (Exception e) {
            log.error("Unable to send GetExchangeRateResponse response message.", e);
        }
    }

    public void returnError(@Observes @EcbProxyErrorEvent EcbProxyEventMessage event) {
        try {
            responseMessageProducerBean.sendResponseMessageToSender(event.getRequestMessage(), event.getResponseMessage(), 60000, DeliveryMode.NON_PERSISTENT);

        } catch (Exception e) {
            String errorMessage = "Unable to send ECB proxy get exchange rate error response. Reason: " + e.getMessage();
            log.error(errorMessage, e);
        }
    }

    private GetExchangeRateRequest getGetExchangeRateRequest(EcbProxyEventMessage event) throws JMSException, JAXBException {
        return JAXBUtils.unMarshallMessage(event.getRequestMessage().getText(), GetExchangeRateRequest.class);
    }

    private String getExchangeRateResponseAsString(GetExchangeRateResponse getExchangeRateResponse) throws JAXBException {
        return JAXBUtils.marshallJaxBObjectToString(getExchangeRateResponse);
    }

    private void sendErrorResponse(EcbProxyEventMessage event, String errorMessage) {
        try {
            responseMessageProducerBean.sendResponseMessageToSender(event.getRequestMessage(), errorMessage, 60000, DeliveryMode.NON_PERSISTENT);
        } catch (Exception e) {
            log.error("Unable to send error response message. ", e);
        }
    }
}
