package eu.europa.ec.fisheries.uvms.sales.proxy.ecb.domain.dao;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/** Interface for every DAO in the Sales proxy ECB. Provides CRUD methods + a hook for the entity manager for testing
 * purposes **.
 * @param <T> the type entity we're doing CRUD with
 * @param <PK> the type of the primary key of the entity
 */
public interface DaoForSalesECBProxy<T, PK extends Serializable> {

    /** Find exactly one object by its id. If no object has been found, null is returned
     * @param id id
     * @return the searched object, or null
     */
    T findByIdOrNull(@NotNull PK id);

    /** Create a new record for the provided entity
     * @param entity unmanaged entity
     * @return created, managed entity
     */
    T create(@NotNull T entity);

    /** Delete the entity from the database
     * @param entity managed entity
     */
    void delete(@NotNull T entity);

    /** Delete the entity from the database
     * @param entity managed entity
     */
    T update(@NotNull T entity);

    /**
     * Used by the test framework, to inject an EntityManager.
     * @param em an instance of an entityManager
     */
    void setEntityManager(EntityManager em);

}
