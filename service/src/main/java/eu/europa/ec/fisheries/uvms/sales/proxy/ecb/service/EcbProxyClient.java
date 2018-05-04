package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.service;

import eu.europa.ec.fisheries.schema.sales.proxy.ecb.types.v1.GetExchangeRateRequest;
import eu.europa.ec.fisheries.schema.sales.proxy.ecb.types.v1.GetExchangeRateResponse;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.exception.EcbProxyException;

/**
 * Facade to interact with the ecb proxy functionality.
 */
public interface EcbProxyClient {

    /**
     * Returns how much money in currency B you'll get for one instance of A/
     * @param request containing the current currency, the to-be currency and the date
     * @return a response with all request parameters and the exchange rate
     * @throws EcbProxyException when something goes wrong
     */
    GetExchangeRateResponse getExchangeRate(GetExchangeRateRequest request) throws EcbProxyException;
}
