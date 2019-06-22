package it.unitn.disi.wp.cup.persistence.dao.factory;

import it.unitn.disi.wp.cup.persistence.dao.DAO;
import it.unitn.disi.wp.cup.persistence.dao.PersonDAO;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOFactoryException;
import it.unitn.disi.wp.cup.persistence.dao.factory.jdbc.JDBCDAOFactory;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

/**
 * This interface must be implemented by all the concrete
 * {@code DAOFactor(y)}ies
 *
 * @author Carlo Corradini
 */
public interface DAOFactory {

    /**
     * The name of the Dao Factory
     */
    String DAO_FACTORY = "daoFactory";

    /**
     * Shutdowns the connection to the storage system
     */
    void shutdown();

    /**
     * Returns the concrete {@link DAO dao} which type is the class passed as
     * parameter
     *
     * @param <DAO_CLASS>  The class name of the {@code dao} to get
     * @param daoInterface The class instance of the {@code dao} to get
     * @return The concrete {@code dao} which type is the class passed as
     * parameter
     * @throws DAOFactoryException If an error occurred during the operation
     */
    <DAO_CLASS extends DAO> DAO_CLASS getDAO(Class<DAO_CLASS> daoInterface) throws DAOFactoryException;

    /**
     * Get the DAOFactory in the Faces Context
     *
     * @return The {@link DAOFactory} instance
     * @throws DAOFactoryException If {@link DAOFactory} is null
     */
    static DAOFactory getDAOFactory() throws DAOFactoryException {
        DAOFactory daoFactory = (DAOFactory) ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getAttribute(DAO_FACTORY);
        if (daoFactory == null)
            throw new DAOFactoryException("Impossible to get dao factory for storage system");

        return daoFactory;
    }

    /**
     * Get the DAOFactory in the Servlet Context using Servlet Object
     *
     * @param servlet The servlet to get from
     * @return The {@link DAOFactory} instance
     * @throws DAOFactoryException  If {@link DAOFactory} is null
     * @throws NullPointerException If {@code servlet} is null
     */
    static DAOFactory getDAOFactory(final HttpServlet servlet) throws DAOFactoryException, NullPointerException {
        if (servlet == null)
            throw new NullPointerException("Servlet cannot bet null");

        return getDAOFactory(servlet.getServletContext());
    }

    /**
     * Get the DAOFactory in the Servlet Context
     *
     * @param servletContext The ServletContext to get from
     * @return The {@link DAOFactory}
     * @throws DAOFactoryException  If {@link DAOFactory} is null
     * @throws NullPointerException If {@code servletContext} is null
     */
    static DAOFactory getDAOFactory(final ServletContext servletContext) throws DAOFactoryException, NullPointerException {
        DAOFactory daoFactory;
        if (servletContext == null)
            throw new NullPointerException("Servlet cannot be null");

        daoFactory = (DAOFactory) servletContext.getAttribute(DAO_FACTORY);
        if (daoFactory == null)
            throw new DAOFactoryException("Impossible to get dao factory for storage system");

        return daoFactory;
    }
}
