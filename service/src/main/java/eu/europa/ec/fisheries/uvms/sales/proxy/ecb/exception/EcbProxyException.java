package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.exception;


public class EcbProxyException extends Exception {

    public EcbProxyException(String message) {
        super(message);
    }

    public EcbProxyException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
