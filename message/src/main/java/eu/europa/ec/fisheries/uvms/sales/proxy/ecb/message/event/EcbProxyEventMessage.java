package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.message.event;

import javax.jms.TextMessage;

public class EcbProxyEventMessage {

    private TextMessage requestMessage;
    private String responseMessage;

    public EcbProxyEventMessage(TextMessage requestMessage, String responseMessage) {
        this.requestMessage = requestMessage;
        this.responseMessage = responseMessage;
    }

    public TextMessage getRequestMessage() {
        return requestMessage;
    }

    public void setRequestMessage(TextMessage requestMessage) {
        this.requestMessage = requestMessage;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

}