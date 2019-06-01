package it.unitn.disi.wp.cup.listeners;

import it.unitn.disi.wp.cup.config.DatabaseConfig;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOFactoryException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;
import it.unitn.disi.wp.cup.persistence.dao.factory.jdbc.JDBCDAOFactory;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Web App life-cycle listener
 *
 * @author Carlo Corradini
 */
public class WebAppContextListener implements ServletContextListener {

    private static final String DB_DRIVER = "org.postgresql.Driver";
    private static final String DB_URL = "jdbc:postgresql://%s:%d/%s?user=%s&password=%s&ssl=%b";
    private static final String DAO_FACTORY = "daoFactory";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        DAOFactory daoFactory;
        DatabaseConfig databaseConfig;

        databaseConfig = new DatabaseConfig();

        try {
            JDBCDAOFactory.configure(DB_DRIVER,
                    String.format(DB_URL,
                            databaseConfig.getHost(),
                            databaseConfig.getPort(),
                            databaseConfig.getName(),
                            databaseConfig.getUsername(),
                            databaseConfig.getPassword(),
                            databaseConfig.getSsl()));
            daoFactory = JDBCDAOFactory.getInstance();
            sce.getServletContext().setAttribute(DAO_FACTORY, daoFactory);
        } catch (DAOFactoryException ex) {
            Logger.getLogger(getClass().getName()).severe(ex.toString());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        DAOFactory daoFactory;

        daoFactory = (DAOFactory) sce.getServletContext().getAttribute(DAO_FACTORY);
        if (daoFactory != null) {
            daoFactory.shutdown();
        }
    }
}
