package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.integrationtest.test.factory;

import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.model.mapper.EcbProxyRequestMapper;
import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;

import javax.ejb.Stateless;
import java.io.IOException;
import java.io.InputStream;

@Stateless
public class SalesEcbProxyMessageFactory {

    public String composeGetExchangeRateRequestAsString() {
        DateTime dateTime = new DateTime(2018, 8, 12, 0, 0);
        return composeGetExchangeRateRequestAsString(dateTime);
    }

    public String composeGetExchangeRateRequestAsString(DateTime dateTime) {
        String sourceCurrency = "DKK";
        String targetCurrency = "EUR";
        try {
            return EcbProxyRequestMapper.createGetExchangeRateRequest(sourceCurrency, targetCurrency, dateTime);

        } catch (MessageException e) {
            throw new RuntimeException("Unable to create the request for the ECB proxy", e);
        }
    }

    public String composeGetExchangeRateResponseAsString() throws Exception {
        return getTestDataFromFile("test-data/GetExchangeRateResponse.txt");
    }

    private String getTestDataFromFile(String fileName) {
        try {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream(fileName);
            return IOUtils.toString(is);

        } catch (IOException e) {
            throw new IllegalStateException("Unable to retrieve test data: " + fileName + ". Reason: " + e.getMessage());
        }
    }

}
