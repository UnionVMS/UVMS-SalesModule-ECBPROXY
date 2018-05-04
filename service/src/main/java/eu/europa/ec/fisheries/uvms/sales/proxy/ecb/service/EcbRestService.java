package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.service;

import com.google.common.base.Optional;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.dto.ExchangeRate;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.exception.EcbProxyException;
import org.joda.time.LocalDate;

import java.util.List;

/**
 * This server communicates with the REST service of the ECB.
 */
public interface EcbRestService {

    /**
     * Retrieves all exchange rates, optionally since a certain date (inclusive)
     * @param date the first date for which the currencies need to be retrieved
     * @return a list of currency rates. When nothing is found, an empty list.
     */
    List<ExchangeRate> findExchangeRates(Optional<LocalDate> date) throws EcbProxyException;

}
