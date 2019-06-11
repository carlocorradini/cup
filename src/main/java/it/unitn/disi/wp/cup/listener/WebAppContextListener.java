package it.unitn.disi.wp.cup.listener;

import it.unitn.disi.wp.cup.config.AuthConfig;
import it.unitn.disi.wp.cup.config.DatabaseConfig;
import it.unitn.disi.wp.cup.config.StdTemplateConfig;
import it.unitn.disi.wp.cup.config.exception.ConfigException;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOFactoryException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;
import it.unitn.disi.wp.cup.persistence.dao.factory.jdbc.JDBCDAOFactory;

import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Web App Application listener
 *
 * @author Carlo Corradini
 */
@WebListener
public class WebAppContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            /* - Load Configurations - */
            DatabaseConfig.load();
            StdTemplateConfig.load();
            AuthConfig.load();
            JDBCDAOFactory.configure(DatabaseConfig.getDriver(), DatabaseConfig.getUrl());
            /* END Load Configuration */

            sce.getServletContext().setAttribute(DAOFactory.DAO_FACTORY, JDBCDAOFactory.getInstance());
        } catch (DAOFactoryException | ConfigException ex) {
            Logger.getLogger(getClass().getName()).severe(ex.toString());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        DAOFactory daoFactory = (DAOFactory) sce.getServletContext().getAttribute(DAOFactory.DAO_FACTORY);
        if (daoFactory != null) {
            daoFactory.shutdown();
        }
    }
}
