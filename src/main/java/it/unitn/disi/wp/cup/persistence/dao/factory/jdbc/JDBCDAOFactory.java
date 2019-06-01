package it.unitn.disi.wp.cup.persistence.dao.factory.jdbc;

import it.unitn.disi.wp.cup.persistence.dao.DAO;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOFactoryException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * This JDBC implementation of {@code DAOFactory}
 *
 * @author Carlo Corradini
 */
public class JDBCDAOFactory implements DAOFactory {

    private final transient Connection connection;
    private final transient HashMap<Class, DAO> DAO_CACHE;
    private static JDBCDAOFactory instance;

    private JDBCDAOFactory(String dbDriver, String dbUrl) throws DAOFactoryException {
        super();

        try {
            Class.forName(dbDriver, true, getClass().getClassLoader());
            connection = DriverManager.getConnection(dbUrl);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex.getMessage(), ex.getCause());
        } catch (SQLException ex) {
            throw new DAOFactoryException("Cannot create Connection", ex);
        }

        DAO_CACHE = new HashMap<>();
    }

    /**
     * Call this method before use the instance of this class
     *
     * @param dbDriver The Driver to access the database represented by
     * "org.[DB_VENDOR].Driver
     * @param dbUrl The Url to access the database, represented by
     * jdbc:[DB_NAME]://[DB_HOST]:[DB_PORT]/[DB_NAME]?user=[DB_USERNAME]&password=[DB_PASSWORD]&ssl=[DB_SSL]
     * @throws DAOFactoryException If an error occurred during DAO factory
     * configuration.
     *
     */
    public static void configure(String dbDriver, String dbUrl) throws DAOFactoryException {
        if (instance == null) {
            instance = new JDBCDAOFactory(dbDriver, dbUrl);
        } else {
            throw new DAOFactoryException("JDBCDAOFactory already configured");
        }
    }

    /**
     * Returns the singleton instance of this {@link DAOFactory}
     *
     * @return The singleton instance of this {@code DAOFactory}
     * @throws DAOFactoryException If an error occurred if this DAO factory is
     * not yet configured
     */
    public static JDBCDAOFactory getInstance() throws DAOFactoryException {
        if (instance == null) {
            throw new DAOFactoryException("JDBCDAOFactory not yet configured");
        }
        return instance;
    }

    /**
     * Shutdowns the access to the storage system
     */
    @Override
    public void shutdown() {
        try {
            DriverManager.getConnection("jdbc:postgresql:;shutdown=true");
        } catch (SQLException ex) {
            Logger.getLogger(JDBCDAOFactory.class.getName()).info(ex.getMessage());
        }
    }

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
    @Override
    public <DAO_CLASS extends DAO> DAO_CLASS getDAO(Class<DAO_CLASS> daoInterface) throws DAOFactoryException {
        DAO dao;
        Package pkg;
        String prefix;
        Class daoClass;
        Constructor<DAO_CLASS> constructor;
        DAO_CLASS daoInstance;

        if ((dao = DAO_CACHE.get(daoInterface)) != null) {
            return (DAO_CLASS) dao;
        }

        pkg = daoInterface.getPackage();
        prefix = pkg.getName() + ".jdbc.JDBC";

        try {
            daoClass = Class.forName(prefix + daoInterface.getSimpleName());
            constructor = daoClass.getConstructor(Connection.class);
            daoInstance = constructor.newInstance(connection);

            if (!(daoInstance instanceof JDBCDAO)) {
                throw new DAOFactoryException("The daoInterface passed as parameter doesn't extend JDBCDAO class");
            }

            DAO_CACHE.put(daoInterface, daoInstance);
            return daoInstance;
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException | SecurityException ex) {
            throw new DAOFactoryException("Impossible to return the DAO", ex);
        }
    }
}
