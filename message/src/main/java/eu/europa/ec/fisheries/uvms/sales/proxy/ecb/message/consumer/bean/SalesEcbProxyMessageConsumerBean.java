/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.message.consumer.bean;

import eu.europa.ec.fisheries.uvms.commons.message.api.MessageConsumer;
import eu.europa.ec.fisheries.uvms.commons.message.impl.AbstractConsumer;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.message.constants.SalesEcbProxyMessageConstants;

import javax.ejb.Stateless;

@Stateless
public class SalesEcbProxyMessageConsumerBean extends AbstractConsumer implements MessageConsumer {

    @Override
    public String getDestinationName() {
        return SalesEcbProxyMessageConstants.QUEUE_ECB_PROXY_CONFIG;
    }

}
