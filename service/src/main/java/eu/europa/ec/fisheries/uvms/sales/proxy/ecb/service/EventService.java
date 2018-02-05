package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.service;

import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.message.event.EcbProxyErrorEvent;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.message.event.EcbProxyEventMessage;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.message.event.EcbProxyGetExchangeRateEvent;

import javax.ejb.Local;
import javax.enterprise.event.Observes;

@Local
public interface EventService {

    /**
     * Get exchange rate.
     * @param event the event received event
     */
    void getExchangeRate(@Observes @EcbProxyGetExchangeRateEvent EcbProxyEventMessage event);

    /**
     * Sends back an error message over the queue
     * @param event the event received event
     */
    void returnError(@Observes @EcbProxyErrorEvent EcbProxyEventMessage event);

}
