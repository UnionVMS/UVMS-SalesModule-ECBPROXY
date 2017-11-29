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

import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.event.EcbProxyErrorEvent;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.event.EcbProxyEventMessage;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.event.EcbProxySendEvent;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.exception.EcbProxyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.jms.DeliveryMode;

@LocalBean
@Stateless
public class ProxyMessageSender {

    final static Logger LOG = LoggerFactory.getLogger(ProxyMessageSender.class);

    @EJB
    ResponseMessageProducerBean responseMessageProducerBean;

    /**
     * Send method that gets triggered when an {@link EcbProxySendEvent} is fired.
     * @param event
     * @throws EcbProxyException when something goes wrong
     */
    public void sendExchangeRate(@Observes @EcbProxySendEvent EcbProxyEventMessage event) throws EcbProxyException {
        try {
            responseMessageProducerBean.sendModuleResponseMessage(event.getRequestMessage(), event.getResponseMessage(), DeliveryMode.NON_PERSISTENT);

        } catch (Exception e) {
            throw new EcbProxyException("Could could respond with the converted currency because of a JMS problem.", e);
        }
    }

    /**
     * Send method that gets triggered when an {@link EcbProxyErrorEvent} is fired.
     * @param event
     * @throws EcbProxyException when something goes wrong
     */
    public void sendErrorMessage(@Observes @EcbProxyErrorEvent EcbProxyEventMessage event) {
        try {
            responseMessageProducerBean.sendModuleResponseMessage( event.getRequestMessage(), event.getResponseMessage(), DeliveryMode.NON_PERSISTENT);
        } catch (Exception e) {
            LOG.error("Could could respond with the converted currency because of a JMS problem.", e);
        }
    }

}
