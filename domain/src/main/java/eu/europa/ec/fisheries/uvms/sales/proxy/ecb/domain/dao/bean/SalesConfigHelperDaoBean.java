/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.domain.dao.bean;

import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.domain.dao.SalesConfigHelperDao;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Slf4j
@Stateless
public class SalesConfigHelperDaoBean implements SalesConfigHelperDao {

    @PersistenceContext(unitName = "ecbLocalProxyPU")
    protected EntityManager em;

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

}
