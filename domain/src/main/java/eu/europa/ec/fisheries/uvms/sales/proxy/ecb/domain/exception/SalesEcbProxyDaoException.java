package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.domain.exception;

public class SalesEcbProxyDaoException extends Exception {
    private static final long serialVersionUID = 1L;

    public SalesEcbProxyDaoException(String message) {
        super(message);
    }

    public SalesEcbProxyDaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
