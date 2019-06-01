package it.unitn.disi.wp.cup.persistence.dao.factory;

import it.unitn.disi.wp.cup.persistence.dao.DAO;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOFactoryException;

/**
 * This interface must be implemented by all the concrete
 * {@code DAOFactor(y)}ies
 *
 * @author Carlo Corradini
 */
public interface DAOFactory {

    /**
     * Shutdowns the connection to the storage system
     */
    public void shutdown();

    /**
     * Returns the concrete {@link DAO dao} which type is the class passed as
     * parameter
     *
     * @param <DAO_CLASS> The class name of the {@code dao} to get
     * @param daoInterface The class instance of the {@code dao} to get
     * @return The concrete {@code dao} which type is the class passed as
     * parameter
     * @throws DAOFactoryException If an error occurred during the operation
     */
    public <DAO_CLASS extends DAO> DAO_CLASS getDAO(Class<DAO_CLASS> daoInterface) throws DAOFactoryException;

}
