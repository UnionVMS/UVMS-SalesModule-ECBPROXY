package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.model.mapper;

import eu.europa.ec.fisheries.schema.sales.proxy.ecb.types.v1.EcbProxyRequestMethod;
import eu.europa.ec.fisheries.schema.sales.proxy.ecb.types.v1.GetExchangeRateRequest;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.commons.message.impl.JAXBUtils;
import org.joda.time.DateTime;

import javax.xml.bind.JAXBException;

public class EcbProxyRequestMapper {

    private EcbProxyRequestMapper() {}

    public static String createGetExchangeRateRequest(String sourceCurrency, String targetCurrency, DateTime date) throws MessageException {
        GetExchangeRateRequest convertCurrencyRequest = new GetExchangeRateRequest()
                .withSourceCurrency(sourceCurrency)
                .withTargetCurrency(targetCurrency)
                .withDate(date)
                .withMethod(EcbProxyRequestMethod.GET_EXCHANGE_RATE);
        try {
            return JAXBUtils.marshallJaxBObjectToString(convertCurrencyRequest);
        } catch (JAXBException e) {
            throw new MessageException("Could not create exchange rate request", e);
        }
    }

}
