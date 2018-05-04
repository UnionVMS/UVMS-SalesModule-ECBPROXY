package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.domain.dao.bean;

import eu.europa.ec.fisheries.uvms.sales.proxy.ecb.domain.dao.DaoForSalesECBProxy;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

/**
 * The base for every DAO in the Sales proxy ECB. Provides the most used methods.
 */
public abstract class BaseDaoForSalesECBProxy<T, PK extends Serializable> implements DaoForSalesECBProxy<T, PK> {

    @PersistenceContext(unitName = "ecbLocalProxyPU")
    protected EntityManager em;

    public BaseDaoForSalesECBProxy() {
    }

    public T findByIdOrNull(PK id) {
        return em.find(getEntityClass(), id);
    }

    public T create(T entity) {
        em.persist(entity);
        return entity;
    }

    public void delete(T entity) {
        em.remove(entity);
    }

    @Override
    public T update(T entity) {
        em.merge(entity);
        em.flush();
        return entity;
    }

    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    @SuppressWarnings("unchecked")
    public Class<T> getEntityClass() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

}
