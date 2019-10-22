package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.service.bean;

import eu.europa.ec.fisheries.uvms.config.exception.ConfigServiceException;
import eu.europa.ec.fisheries.uvms.config.service.ParameterService;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.domain.constant.ParameterKey;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.service.exception.SalesEcbProxyServiceException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ConfigServiceBeanTest {

    @InjectMocks
    private ConfigServiceBean configServiceBean;

    @Mock
    private ParameterService parameterService;

    @Test
    public void testGetParameter() throws Exception {
        //data
        String expectedParameterValue = "MyParameterValue";

        //mock
        doReturn(expectedParameterValue).when(parameterService).getParamValueById(anyString());

        //execute
        String actualParameterValue = configServiceBean.getParameter(ParameterKey.ECB_ENDPOINT);

        //verify and assert
        verify(parameterService).getParamValueById(anyString());
        verifyNoMoreInteractions(parameterService);
        assertEquals(actualParameterValue, expectedParameterValue);
    }

    @Test
    public void TryGetParameterForParameterServiceConfigServiceException() throws Exception {
        //data
        String expectedParameterValue = "MyParameterValue";
        String expectedErrorMessage = "Could not retrieve a setting with key sales.ebc.proxy.endpoint from Config. Reason: MyConfigServiceException";

        //mock
        doThrow(new ConfigServiceException("MyConfigServiceException")).when(parameterService).getParamValueById(anyString());

        //execute
        try {
            configServiceBean.getParameter(ParameterKey.ECB_ENDPOINT);

        } catch (SalesEcbProxyServiceException e) {
            assertEquals(expectedErrorMessage, e.getMessage());
        }

        //verify and assert
        verify(parameterService).getParamValueById(anyString());
        verifyNoMoreInteractions(parameterService);
    }
}
