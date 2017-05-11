package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.bean;

import com.google.common.base.Optional;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.EcbRestService;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.ParameterService;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.constant.ParameterKey;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.dto.ExchangeRate;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.dto.GenericDataDto;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.mapper.GenericDataDtoMapper;
import org.joda.time.LocalDate;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Stateless
public class EcbRestServiceBean implements EcbRestService {

    @EJB
    private ParameterService parameterService;

    public List<ExchangeRate> findExchangeRates(Optional<LocalDate> startDate) {
        String ecbEndpoint = parameterService.getParameterValue(ParameterKey.ECB_ENDPOINT);
        if (startDate.isPresent()) {
            ecbEndpoint = ecbEndpoint + "?startPeriod=" + startDate.get().toString("YYYY-MM-dd");
        }

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(ecbEndpoint);
        Invocation.Builder requestBuilder = target.request(MediaType.APPLICATION_XML_TYPE);
        GenericDataDto response = requestBuilder
                .buildGet()
                .invoke(GenericDataDto.class);

        return GenericDataDtoMapper.mapToExchangeRates(response);
    }
}
