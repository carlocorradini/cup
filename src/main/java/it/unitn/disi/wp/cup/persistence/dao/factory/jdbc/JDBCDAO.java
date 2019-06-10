package it.unitn.disi.wp.cup.persistence.dao.factory.jdbc;

import it.unitn.disi.wp.cup.persistence.dao.DAO;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOFactoryException;

import java.sql.Connection;
import java.util.HashMap;

/**
 * This is the base DAO class all concrete DAO using JDBC technology must extend
 *
 * @param <ENTITY_CLASS>      the class of the entities the dao handle
 * @param <PRIMARY_KEY_CLASS> the class of the primary key of the entity the dao
 *                            handle
 * @author Carlo Corradini
 */
public abstract class JDBCDAO<ENTITY_CLASS, PRIMARY_KEY_CLASS> implements DAO<ENTITY_CLASS, PRIMARY_KEY_CLASS> {

    /**
     * The JDBC {@link Connection} used to access the persistence system
     */
    protected final Connection CONNECTION;
    /**
     * The list of other DAOs this DAO can interact with
     */
    protected final HashMap<Class, DAO> FRIEND_DAOS;

    /**
     * The base constructor for all the JDBC DAOs
     *
     * @param connection The internal {@code Connection}
     */
    protected JDBCDAO(Connection connection) {
        super();
        this.CONNECTION = connection;
        FRIEND_DAOS = new HashMap<>();
    }

    /**
     * If this DAO can interact with it, then returns the DAO of class passed as
     * parameter
     *
     * @param <DAO_CLASS> The class name of the DAO that can interact with this
     *                    DAO
     * @param daoClass    The class of the DAO that can interact with this DAO
     * @return The instance of the DAO or null if no DAO of the type passed as
     * parameter can interact with this DAO
     * @throws DAOFactoryException If an error occurred
     */
    @Override
    public <DAO_CLASS extends DAO> DAO_CLASS getDAO(Class<DAO_CLASS> daoClass) throws DAOFactoryException {
        return (DAO_CLASS) FRIEND_DAOS.get(daoClass);
    }

}
