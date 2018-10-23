package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.service.exception;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class SalesEcbProxyServiceException extends RuntimeException {

    public SalesEcbProxyServiceException(String s) {
        super(s);
    }

    public SalesEcbProxyServiceException(String s, Throwable throwable) {
        super(s, throwable);
    }

}