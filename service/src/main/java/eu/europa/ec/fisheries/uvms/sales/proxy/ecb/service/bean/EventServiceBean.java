package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.service.bean;

import eu.europa.ec.fisheries.schema.sales.proxy.ecb.types.v1.GetExchangeRateRequest;
import eu.europa.ec.fisheries.schema.sales.proxy.ecb.types.v1.GetExchangeRateResponse;
import eu.europa.ec.fisheries.uvms.sales.model.mapper.JAXBMarshaller;
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
        try {
            GetExchangeRateRequest getExchangeRateRequest = JAXBMarshaller.unmarshallTextMessage(event.getRequestMessage(), GetExchangeRateRequest.class);
            GetExchangeRateResponse getExchangeRateResponse = client.getExchangeRate(getExchangeRateRequest);
            String responseAsString = JAXBMarshaller.marshallJaxBObjectToString(getExchangeRateResponse);
            responseMessageProducerBean.sendModuleResponseMessage(event.getRequestMessage(), responseAsString);

        } catch (Exception e) {
            String errorMessage = "Unable to process get exchange rate request in ECB proxy. Reason: " + e.getMessage();
            log.error(errorMessage, e);
            responseMessageProducerBean.sendModuleResponseMessage(event.getRequestMessage(), errorMessage);
        }
    }

    public void returnError(@Observes @EcbProxyErrorEvent EcbProxyEventMessage event) {
        try {
            responseMessageProducerBean.sendModuleResponseMessage(event.getRequestMessage(), event.getResponseMessage());

        } catch (Exception e) {
            String errorMessage = "Unable to send ECB proxy get exchange rate error response. Reason: " + e.getMessage();
            log.error(errorMessage, e);
        }
    }

}
