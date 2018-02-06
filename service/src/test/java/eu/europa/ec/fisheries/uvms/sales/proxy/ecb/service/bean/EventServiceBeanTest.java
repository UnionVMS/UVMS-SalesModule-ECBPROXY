package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.service.bean;

import eu.europa.ec.fisheries.schema.sales.proxy.ecb.types.v1.GetExchangeRateRequest;
import eu.europa.ec.fisheries.schema.sales.proxy.ecb.types.v1.GetExchangeRateResponse;
import eu.europa.ec.fisheries.uvms.sales.model.mapper.JAXBMarshaller;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.message.event.EcbProxyEventMessage;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.message.producer.bean.ResponseMessageProducerBean;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.service.EcbProxyClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.jms.TextMessage;

import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@PowerMockIgnore({"javax.management.*", "javax.xml.*", "org.xml.sax.*"})
@PrepareForTest({JAXBMarshaller.class})
@RunWith(PowerMockRunner.class)
public class EventServiceBeanTest {

    @InjectMocks
    private EventServiceBean eventServiceBean;

    @Mock
    private ResponseMessageProducerBean responseMessageProducerBean;

    @Mock
    private EcbProxyClient ecbProxyClient;

    @Test
    public void testGetExchangeRateAndSendResponse() throws Exception {
        //data set
        String responseMessage = null;
        GetExchangeRateRequest getExchangeRateRequest = new GetExchangeRateRequest();
        GetExchangeRateResponse getExchangeRateResponse = new GetExchangeRateResponse();
        String responseAsString = "MyResponseAsString";

        //mock
        TextMessage requestMessageMock = mock(TextMessage.class);
        mockStatic(JAXBMarshaller.class);
        when(JAXBMarshaller.unmarshallTextMessage(requestMessageMock, GetExchangeRateRequest.class)).thenReturn(getExchangeRateRequest);
        doReturn(getExchangeRateResponse).when(ecbProxyClient).getExchangeRate(getExchangeRateRequest);
        when(JAXBMarshaller.marshallJaxBObjectToString(getExchangeRateResponse)).thenReturn(responseAsString);
        doNothing().when(responseMessageProducerBean).sendModuleResponseMessage(requestMessageMock, responseAsString);

        //execute
        EcbProxyEventMessage ecbProxyEventMessage = new EcbProxyEventMessage(requestMessageMock, responseMessage);
        eventServiceBean.getExchangeRate(ecbProxyEventMessage);

        //verify and assert
        verifyStatic();
        JAXBMarshaller.unmarshallTextMessage(requestMessageMock, GetExchangeRateRequest.class);
        verify(ecbProxyClient).getExchangeRate(getExchangeRateRequest);
        verifyStatic();
        JAXBMarshaller.marshallJaxBObjectToString(getExchangeRateResponse);
        verify(responseMessageProducerBean).sendModuleResponseMessage(requestMessageMock, responseAsString);
        verifyNoMoreInteractions(responseMessageProducerBean, ecbProxyClient, requestMessageMock);
    }

    @Test
    public void testGetExchangeRateAndSendErrorMessage() throws Exception {
        //data set
        String responseMessage = null;
        GetExchangeRateRequest getExchangeRateRequest = new GetExchangeRateRequest();
        GetExchangeRateResponse getExchangeRateResponse = new GetExchangeRateResponse();
        String responseAsString = "MyResponseAsString";
        String errorResponseAsString = "Unable to process get exchange rate request in ECB proxy. Reason: MyRuntimeException";

        //mock
        TextMessage requestMessageMock = mock(TextMessage.class);
        mockStatic(JAXBMarshaller.class);
        when(JAXBMarshaller.unmarshallTextMessage(requestMessageMock, GetExchangeRateRequest.class)).thenReturn(getExchangeRateRequest);
        doReturn(getExchangeRateResponse).when(ecbProxyClient).getExchangeRate(getExchangeRateRequest);
        when(JAXBMarshaller.marshallJaxBObjectToString(getExchangeRateResponse)).thenReturn(responseAsString);
        doThrow(new RuntimeException("MyRuntimeException")).when(responseMessageProducerBean).sendModuleResponseMessage(requestMessageMock, responseAsString);
        doNothing().when(responseMessageProducerBean).sendModuleResponseMessage(requestMessageMock, errorResponseAsString);

        //execute
        EcbProxyEventMessage ecbProxyEventMessage = new EcbProxyEventMessage(requestMessageMock, responseMessage);
        eventServiceBean.getExchangeRate(ecbProxyEventMessage);

        //verify and assert
        verifyStatic();
        JAXBMarshaller.unmarshallTextMessage(requestMessageMock, GetExchangeRateRequest.class);
        verify(ecbProxyClient).getExchangeRate(getExchangeRateRequest);
        verifyStatic();
        JAXBMarshaller.marshallJaxBObjectToString(getExchangeRateResponse);
        verify(responseMessageProducerBean).sendModuleResponseMessage(requestMessageMock, responseAsString);
        verify(responseMessageProducerBean).sendModuleResponseMessage(requestMessageMock, errorResponseAsString);
        verifyNoMoreInteractions(responseMessageProducerBean, ecbProxyClient, requestMessageMock);
    }

    @Test
    public void testReturnError() {
        //data set
        String errorResponseAsString = "Unable to process get exchange rate request in ECB proxy. Reason: MyRuntimeException";

        //mock
        TextMessage requestMessageMock = mock(TextMessage.class);
        doNothing().when(responseMessageProducerBean).sendModuleResponseMessage(requestMessageMock, errorResponseAsString);

        //execute
        EcbProxyEventMessage ecbProxyEventMessage = new EcbProxyEventMessage(requestMessageMock, errorResponseAsString);
        eventServiceBean.returnError(ecbProxyEventMessage);

        //verify and assert
        verify(responseMessageProducerBean).sendModuleResponseMessage(requestMessageMock, errorResponseAsString);
        verifyNoMoreInteractions(responseMessageProducerBean, ecbProxyClient, requestMessageMock);
    }

    @Test
    public void testReturnErrorForSendModuleResponseMessageRuntimeException() {
        //data set
        String errorResponseAsString = "Unable to process get exchange rate request in ECB proxy. Reason: MyRuntimeException";

        //mock
        TextMessage requestMessageMock = mock(TextMessage.class);
        doThrow(new RuntimeException("MyRuntimeException")).when(responseMessageProducerBean).
                sendModuleResponseMessage(requestMessageMock, errorResponseAsString);

        //execute
        EcbProxyEventMessage ecbProxyEventMessage = new EcbProxyEventMessage(requestMessageMock, errorResponseAsString);
        eventServiceBean.returnError(ecbProxyEventMessage);

        //verify and assert
        verify(responseMessageProducerBean).sendModuleResponseMessage(requestMessageMock, errorResponseAsString);
        verifyNoMoreInteractions(responseMessageProducerBean, ecbProxyClient, requestMessageMock);
    }

}
