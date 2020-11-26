package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.service.bean.helper;

import eu.europa.ec.fisheries.uvms.config.constants.ConfigHelper;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.constant.ServiceConstants;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.domain.constant.ParameterKey;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.domain.dao.SalesConfigHelperDao;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class EcbProxyConfigHelperBean implements ConfigHelper {

    private static final String MODULE_VERSION_PROPERTIES_KEY = "uvms.module.version";

    @EJB
    private SalesConfigHelperDao salesConfigHelperDao;

    @Inject
    private PropertiesBean propertiesBean;

    @Override
    public List<String> getAllParameterKeys() {
        List<String> allParameterKeys = new ArrayList<>();
        for (ParameterKey parameterKey : ParameterKey.values()) {
            allParameterKeys.add(parameterKey.getKey());
        }

        return allParameterKeys;
    }

    @Override
    public String getModuleName() {
        return ServiceConstants.SALES_ECB_PROCY_CONFIG_NAME;
    }

    @Override
    public String getModuleVersion() {
        return propertiesBean.getProperty(MODULE_VERSION_PROPERTIES_KEY);
    }

    @Override
    public EntityManager getEntityManager() {
        return salesConfigHelperDao.getEntityManager();
    }
}
