package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.service;

import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.domain.constant.ParameterKey;

/**
 * When starting up sales-proxy-ecb, the settings in the config module are synchronised with the sales-proxy-ecb parameter table.
 * This means that the most up to date settings can be found in the parameter table.
 *
 * Config provides a ParameterService to retrieve this configuration from the sales-proxy-ecb parameter table.
 * This service acts as a kind wrapper to invoke the Config ParameterService, to avoid having a hard dependency
 * on Config everywhere.
 */
public interface ConfigService {

    String getParameter(ParameterKey parameterKey);

}
