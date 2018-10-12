package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.service.bean.helper;

import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.domain.dao.SalesConfigHelperDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.persistence.EntityManager;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EcbProxyConfigHelperBeanTest {

    @InjectMocks
    private EcbProxyConfigHelperBean ecbProxyConfigHelperBean;

    @Mock
    private SalesConfigHelperDao salesConfigHelperDao;

    @Test
    public void testGetAllParameterKeys() {
        //execute
        List<String> allParameterKeys = ecbProxyConfigHelperBean.getAllParameterKeys();

        //assert
        assertEquals(1, allParameterKeys.size());
        String parameterKey = allParameterKeys.get(0);
        assertEquals("sales.ebc.proxy.endpoint", parameterKey);
    }

    @Test
    public void testGetEntityManager() {
        //mock
        EntityManager entityManagerMock = mock(EntityManager.class);
        doReturn(entityManagerMock).when(salesConfigHelperDao).getEntityManager();

        //execute
        EntityManager entityManager = ecbProxyConfigHelperBean.getEntityManager();

        //verify and assert
        verify(salesConfigHelperDao).getEntityManager();
        verifyNoMoreInteractions(salesConfigHelperDao, entityManagerMock);
        assertNotNull(entityManager);
    }

    @Test
    public void testGetModuleName() {
        //execute
        String moduleName = ecbProxyConfigHelperBean.getModuleName();
        assertEquals("sales-proxy-ecb", moduleName);
    }
}
