package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.service.bean;

import eu.europa.ec.fisheries.uvms.config.exception.ConfigServiceException;
import eu.europa.ec.fisheries.uvms.config.service.ParameterService;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.domain.constant.ParameterKey;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.dto.ExchangeRate;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.exception.EcbProxyException;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.mapper.GenericDataDtoMapper;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.service.EcbRestService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDate;
import org.sdmx.resources.sdmxml.schemas.v2_1.message.GenericDataType;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Optional;

@Slf4j
@Stateless
public class EcbRestServiceBean implements EcbRestService {

    @EJB
    private ParameterService parameterService;

    public List<ExchangeRate> findExchangeRates(Optional<LocalDate> startDate) throws EcbProxyException {
        String ecbEndpoint = getEcbEndpointForStartDate(startDate);
        log.info("Invoke ECB currency exchange service: " + ecbEndpoint);
        try {
            Client client = ClientBuilder.newClient();
            WebTarget target = client.target(ecbEndpoint);
            Invocation.Builder requestBuilder = target.request(MediaType.APPLICATION_XML_TYPE);

            GenericDataType response = requestBuilder
                    .accept("application/vnd.sdmx.genericdata+xml;version=2.1")
                    .buildGet()
                    .invoke(GenericDataType.class);

            return GenericDataDtoMapper.mapToExchangeRates(response);

        } catch (ResponseProcessingException e) {
            String errorMessage = "Unable to process ECB currency exchange response. HTTP response status: " +
                    e.getResponse().getStatus() + ". Reason: " + e.getMessage();
            log.error(errorMessage);
            throw new EcbProxyException(errorMessage);
        } catch (WebApplicationException e) {
            String errorMessage = "Unable to retrieve ECB currency exchange rates. HTTP response status: " +
                    e.getResponse().getStatus() + ". Reason: " + e.getMessage();
            log.error(errorMessage);
            throw new EcbProxyException(errorMessage);
        } catch (Exception e) {
            String errorMessage = "Unable to retrieve ECB currency exchange rates.";
            log.error(errorMessage, e);
            throw new EcbProxyException(errorMessage, e);
        }
    }

    private String getEcbEndpointForStartDate(Optional<LocalDate> startDate) throws EcbProxyException {
        if (!startDate.isPresent()) {
            throw new EcbProxyException("ECB exchange rate lookup start date is mandatory");
        }
        if (!LocalDate.now().isAfter(startDate.get())) {
            // Note that start date should be before today to obtain latest exchange rates
            throw new EcbProxyException("ECB exchange rate lookup start date should be before today");
        }

        return getEcbEndpointFromSettingsConfig() + "?startPeriod=" + startDate.get().toString("YYYY-MM-dd");

    }

    private String getEcbEndpointFromSettingsConfig() throws EcbProxyException {
        try {
            return parameterService.getStringValue(ParameterKey.ECB_ENDPOINT.getKey());

        } catch (ConfigServiceException e) {
            throw new EcbProxyException("Unable to retrieve settings configuration for key: " + ParameterKey.ECB_ENDPOINT.getKey() + " Reason: " + e.getMessage());
        }
    }

}
