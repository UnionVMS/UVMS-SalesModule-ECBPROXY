package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.bean;

import com.google.common.base.Optional;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.ParameterService;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.constant.ParameterKey;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.dto.DataSetDto;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.dto.ExchangeRate;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.dto.GenericDataDto;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.dto.SeriesDto;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.exception.EcbProxyException;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.mapper.GenericDataDtoMapper;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.service.ExchangeRateService;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@PowerMockIgnore("javax.management.*")
@PrepareForTest({ClientBuilder.class, Client.class, WebTarget.class, Invocation.Builder.class, Invocation.class, GenericDataDtoMapper.class})
@RunWith(PowerMockRunner.class)
public class EcbRestServiceBeanTest {

    @InjectMocks
    private EcbRestServiceBean ecbRestServiceBean;

    @Mock
    private ParameterService parameterService;

    @Mock
    private ExchangeRateService exchangeRateService;

    @Test
    public void testFindExchangeRates() throws Exception {
        //data set
        Optional<LocalDate> startDate = Optional.of(new LocalDate(2017, 3, 5));
        ExchangeRate exchangeRate = new ExchangeRate().rate(BigDecimal.ONE);
        List<ExchangeRate> expectedExchangeRates = new ArrayList<>();
        expectedExchangeRates.add(exchangeRate);
        List<SeriesDto> series = new ArrayList<>();
        DataSetDto dataSetDto = new DataSetDto();
        dataSetDto.setSeries(series);
        GenericDataDto genericDataDto = new GenericDataDto();
        genericDataDto.setDataSet(dataSetDto);

        //mock
        doReturn("MyEndPoint").when(parameterService).getParameterValue(ParameterKey.ECB_ENDPOINT);
        mockStatic(ClientBuilder.class);
        mockStatic(GenericDataDtoMapper.class);
        Client client = mock(Client.class);
        WebTarget target = mock(WebTarget.class);
        Invocation.Builder requestBuilder = mock(Invocation.Builder.class);
        Invocation invocation = mock(Invocation.class);
        when(ClientBuilder.newClient()).thenReturn(client);
        doReturn(target).when(client).target("MyEndPoint?startPeriod=2017-03-05");
        doReturn(requestBuilder).when(target).request(MediaType.APPLICATION_XML_TYPE);
        doReturn(invocation).when(requestBuilder).buildGet();
        doReturn(genericDataDto).when(invocation).invoke(GenericDataDto.class);
        when(GenericDataDtoMapper.mapToExchangeRates(genericDataDto)).thenReturn(expectedExchangeRates);

        //execute
        List<ExchangeRate> exchangeRates = ecbRestServiceBean.findExchangeRates(startDate);

        //verify and assert
        verify(parameterService).getParameterValue(ParameterKey.ECB_ENDPOINT);
        verifyStatic();
        ClientBuilder.newClient();
        verify(client).target("MyEndPoint?startPeriod=2017-03-05");
        verify(target).request(MediaType.APPLICATION_XML_TYPE);
        verify(requestBuilder).buildGet();
        verify(invocation).invoke(GenericDataDto.class);
        verifyStatic();
        GenericDataDtoMapper.mapToExchangeRates(genericDataDto);
        verifyNoMoreInteractions(parameterService, exchangeRateService, client, target, requestBuilder, invocation);

        assertEquals(1, exchangeRates.size());
        assertEquals(expectedExchangeRates.get(0), exchangeRates.get(0));
    }

    @Test
    public void tryFindExchangeRatesForResponseProcessingException() throws Exception {
        //data set
        Optional<LocalDate> startDate = Optional.of(new LocalDate(2017, 3, 5));
        ExchangeRate exchangeRate = new ExchangeRate().rate(BigDecimal.ONE);
        List<ExchangeRate> expectedExchangeRates = new ArrayList<>();
        expectedExchangeRates.add(exchangeRate);
        List<SeriesDto> series = new ArrayList<>();
        DataSetDto dataSetDto = new DataSetDto();
        dataSetDto.setSeries(series);
        GenericDataDto genericDataDto = new GenericDataDto();
        genericDataDto.setDataSet(dataSetDto);

        //mock
        doReturn("MyEndPoint").when(parameterService).getParameterValue(ParameterKey.ECB_ENDPOINT);
        mockStatic(ClientBuilder.class);
        mockStatic(GenericDataDtoMapper.class);
        Client client = mock(Client.class);
        WebTarget target = mock(WebTarget.class);
        Invocation.Builder requestBuilder = mock(Invocation.Builder.class);
        Invocation invocation = mock(Invocation.class);
        when(ClientBuilder.newClient()).thenReturn(client);
        doReturn(target).when(client).target("MyEndPoint?startPeriod=2017-03-05");
        doReturn(requestBuilder).when(target).request(MediaType.APPLICATION_XML_TYPE);
        doReturn(invocation).when(requestBuilder).buildGet();
        doReturn(genericDataDto).when(invocation).invoke(GenericDataDto.class);
        Response responseMock = mock(Response.class);
        when(GenericDataDtoMapper.mapToExchangeRates(genericDataDto)).thenThrow(new ResponseProcessingException(responseMock, "MyResponseProcessingException"));

        //execute
        try {
            ecbRestServiceBean.findExchangeRates(startDate);
            fail("findExchangeRates should fail for ResponseProcessingException");

        } catch(EcbProxyException e) {
            assertEquals("Unable to process ECB currency exchange response. HTTP response status: 0. Reason: MyResponseProcessingException", e.getMessage());
        }

        //verify and assert
        verify(parameterService).getParameterValue(ParameterKey.ECB_ENDPOINT);
        verifyStatic();
        ClientBuilder.newClient();
        verify(client).target("MyEndPoint?startPeriod=2017-03-05");
        verify(target).request(MediaType.APPLICATION_XML_TYPE);
        verify(requestBuilder).buildGet();
        verify(invocation).invoke(GenericDataDto.class);
        verifyStatic();
        GenericDataDtoMapper.mapToExchangeRates(genericDataDto);
        verifyNoMoreInteractions(parameterService, exchangeRateService, client, target, requestBuilder, invocation);
    }

    @Test
    public void tryFindExchangeRatesForWebApplicationException() throws Exception {
        //data set
        Optional<LocalDate> startDate = Optional.of(new LocalDate(2017, 3, 5));
        ExchangeRate exchangeRate = new ExchangeRate().rate(BigDecimal.ONE);
        List<ExchangeRate> expectedExchangeRates = new ArrayList<>();
        expectedExchangeRates.add(exchangeRate);
        List<SeriesDto> series = new ArrayList<>();
        DataSetDto dataSetDto = new DataSetDto();
        dataSetDto.setSeries(series);
        GenericDataDto genericDataDto = new GenericDataDto();
        genericDataDto.setDataSet(dataSetDto);

        //mock
        doReturn("MyEndPoint").when(parameterService).getParameterValue(ParameterKey.ECB_ENDPOINT);
        mockStatic(ClientBuilder.class);
        mockStatic(GenericDataDtoMapper.class);
        Client client = mock(Client.class);
        WebTarget target = mock(WebTarget.class);
        Invocation.Builder requestBuilder = mock(Invocation.Builder.class);
        Invocation invocation = mock(Invocation.class);
        when(ClientBuilder.newClient()).thenReturn(client);
        doReturn(target).when(client).target("MyEndPoint?startPeriod=2017-03-05");
        doReturn(requestBuilder).when(target).request(MediaType.APPLICATION_XML_TYPE);
        doReturn(invocation).when(requestBuilder).buildGet();
        doReturn(genericDataDto).when(invocation).invoke(GenericDataDto.class);
        Response responseMock = mock(Response.class);
        when(GenericDataDtoMapper.mapToExchangeRates(genericDataDto)).thenThrow(new WebApplicationException("MyWebApplicationException", responseMock));

        //execute
        try {
            ecbRestServiceBean.findExchangeRates(startDate);
            fail("findExchangeRates should fail for ResponseProcessingException");

        } catch(EcbProxyException e) {
            assertEquals("Unable to retrieve ECB currency exchange rates. HTTP response status: 0. Reason: MyWebApplicationException", e.getMessage());
        }

        //verify and assert
        verify(parameterService).getParameterValue(ParameterKey.ECB_ENDPOINT);
        verifyStatic();
        ClientBuilder.newClient();
        verify(client).target("MyEndPoint?startPeriod=2017-03-05");
        verify(target).request(MediaType.APPLICATION_XML_TYPE);
        verify(requestBuilder).buildGet();
        verify(invocation).invoke(GenericDataDto.class);
        verifyStatic();
        GenericDataDtoMapper.mapToExchangeRates(genericDataDto);
        verifyNoMoreInteractions(parameterService, exchangeRateService, client, target, requestBuilder, invocation);
    }

    @Test
    public void tryFindExchangeRatesForRuntimeException() throws Exception {
        //data set
        Optional<LocalDate> startDate = Optional.of(new LocalDate(2017, 3, 5));
        ExchangeRate exchangeRate = new ExchangeRate().rate(BigDecimal.ONE);
        List<ExchangeRate> expectedExchangeRates = new ArrayList<>();
        expectedExchangeRates.add(exchangeRate);
        List<SeriesDto> series = new ArrayList<>();
        DataSetDto dataSetDto = new DataSetDto();
        dataSetDto.setSeries(series);
        GenericDataDto genericDataDto = new GenericDataDto();
        genericDataDto.setDataSet(dataSetDto);

        //mock
        doReturn("MyEndPoint").when(parameterService).getParameterValue(ParameterKey.ECB_ENDPOINT);
        mockStatic(ClientBuilder.class);
        mockStatic(GenericDataDtoMapper.class);
        Client client = mock(Client.class);
        WebTarget target = mock(WebTarget.class);
        Invocation.Builder requestBuilder = mock(Invocation.Builder.class);
        Invocation invocation = mock(Invocation.class);
        when(ClientBuilder.newClient()).thenReturn(client);
        doReturn(target).when(client).target("MyEndPoint?startPeriod=2017-03-05");
        doReturn(requestBuilder).when(target).request(MediaType.APPLICATION_XML_TYPE);
        doReturn(invocation).when(requestBuilder).buildGet();
        doReturn(genericDataDto).when(invocation).invoke(GenericDataDto.class);
        when(GenericDataDtoMapper.mapToExchangeRates(genericDataDto)).thenThrow(new RuntimeException("MyRuntimeException"));

        //execute
        try {
            ecbRestServiceBean.findExchangeRates(startDate);
            fail("findExchangeRates should fail for ResponseProcessingException");

        } catch(EcbProxyException e) {
            assertTrue(e.getMessage().contains("Unable to retrieve ECB currency exchange rates. Reason:"));
        }

        //verify and assert
        verify(parameterService).getParameterValue(ParameterKey.ECB_ENDPOINT);
        verifyStatic();
        ClientBuilder.newClient();
        verify(client).target("MyEndPoint?startPeriod=2017-03-05");
        verify(target).request(MediaType.APPLICATION_XML_TYPE);
        verify(requestBuilder).buildGet();
        verify(invocation).invoke(GenericDataDto.class);
        verifyStatic();
        GenericDataDtoMapper.mapToExchangeRates(genericDataDto);
        verifyNoMoreInteractions(parameterService, exchangeRateService, client, target, requestBuilder, invocation);
    }

    @Test
    public void tryFindExchangeRatesForMandatoryStartDateEcbProxyException() throws Exception {
        try {
            ecbRestServiceBean.findExchangeRates(Optional.<LocalDate>absent());
            fail("EcbProxyException should fail for mandatory start date");

        } catch (EcbProxyException e) {
            assertEquals("ECB exchange rate lookup start date is mandatory", e.getMessage());
        }
    }

    @Test
    public void tryFindExchangeRatesForStartDateEqualsTodayEcbProxyException() throws Exception {
        try {
            ecbRestServiceBean.findExchangeRates(Optional.of(LocalDate.now()));
            fail("EcbProxyException should fail for mandatory start date");

        } catch (EcbProxyException e) {
            assertEquals("ECB exchange rate lookup start date should be before today", e.getMessage());
        }
    }

    @Test
    public void tryFindExchangeRatesForStartDateAfterTodayEcbProxyException() throws Exception {
        try {
            ecbRestServiceBean.findExchangeRates(Optional.of(LocalDate.now().plusDays(1)));
            fail("EcbProxyException should fail for mandatory start date");

        } catch (EcbProxyException e) {
            assertEquals("ECB exchange rate lookup start date should be before today", e.getMessage());
        }
    }
}
