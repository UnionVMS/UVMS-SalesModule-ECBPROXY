package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.bean;

import com.google.common.base.Optional;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.EcbRestService;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.ParameterService;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.constant.ParameterKey;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.dto.ExchangeRate;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.dto.GenericDataDto;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.exception.EcbProxyException;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.mapper.GenericDataDtoMapper;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.service.ExchangeRateService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDate;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Slf4j
@Stateless
public class EcbRestServiceBean implements EcbRestService {

    @EJB
    private ParameterService parameterService;

    @EJB
    private ExchangeRateService exchangeRateService;

    public List<ExchangeRate> findExchangeRates(Optional<LocalDate> startDate) throws EcbProxyException {
        String ecbEndpoint = getEcbEndpointForStartDate(startDate);
        log.info("Invoke ECB currency exchange service: " + ecbEndpoint);
        try {
            Client client = ClientBuilder.newClient();
            WebTarget target = client.target(ecbEndpoint);
            Invocation.Builder requestBuilder = target.request(MediaType.APPLICATION_XML_TYPE);

            GenericDataDto response = requestBuilder
                    .buildGet()
                    .invoke(GenericDataDto.class);

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
            String errorMessage = "Unable to retrieve ECB currency exchange rates. Reason: " + e.getStackTrace();
            log.error(errorMessage);
            throw new EcbProxyException(errorMessage);
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
        String ecbEndpoint = parameterService.getParameterValue(ParameterKey.ECB_ENDPOINT);
        return ecbEndpoint + "?startPeriod=" + startDate.get().toString("YYYY-MM-dd");

    }
}
