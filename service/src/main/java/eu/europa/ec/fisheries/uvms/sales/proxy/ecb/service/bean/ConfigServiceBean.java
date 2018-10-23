/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

*/
package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.service.bean;

import eu.europa.ec.fisheries.uvms.config.exception.ConfigServiceException;
import eu.europa.ec.fisheries.uvms.config.service.ParameterService;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.domain.constant.ParameterKey;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.service.ConfigService;
import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.service.exception.SalesEcbProxyServiceException;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
@Slf4j
public class ConfigServiceBean implements ConfigService {

    @EJB
    private ParameterService parameterService;

    @Override
    public String getParameter(ParameterKey parameterKey) {
        try {
            return parameterService.getStringValue(parameterKey.getKey());

        } catch (ConfigServiceException e) {
            String errorMessage = "Could not retrieve a setting with key " + parameterKey.getKey() + " from Config. Reason: " + e.getMessage();
            log.error(errorMessage);
            throw new SalesEcbProxyServiceException(errorMessage, e);
        }
    }
}