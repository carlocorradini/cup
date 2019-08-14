package it.unitn.disi.wp.cup.listener;

import it.unitn.disi.wp.cup.config.*;
import it.unitn.disi.wp.cup.config.exception.ConfigException;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOFactoryException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;
import it.unitn.disi.wp.cup.persistence.dao.factory.jdbc.JDBCDAOFactory;
import it.unitn.disi.wp.cup.util.AuthUtil;
import it.unitn.disi.wp.cup.util.EmailUtil;
import it.unitn.disi.wp.cup.util.ImageUtil;
import it.unitn.disi.wp.cup.util.pdf.PDFUtil;
import it.unitn.disi.wp.cup.util.pdf.PrescriptionExamPDFUtil;
import it.unitn.disi.wp.cup.util.pdf.PrescriptionMedicinePDFUtil;

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
            AppConfig.load();
            EmailConfig.load();
            PrescriptionConfig.load();
            JDBCDAOFactory.configure(DatabaseConfig.getDriver(), DatabaseConfig.getUrl());
            AuthUtil.configure(JDBCDAOFactory.getInstance());
            EmailUtil.configure();
            ImageUtil.configure(sce.getServletContext());
            PrescriptionMedicinePDFUtil.configure(JDBCDAOFactory.getInstance());
            PrescriptionExamPDFUtil.configure(JDBCDAOFactory.getInstance());
            /* END Load Configuration */
            sce.getServletContext().setAttribute(DAOFactory.DAO_FACTORY, JDBCDAOFactory.getInstance());
        } catch (DAOFactoryException | ConfigException | NullPointerException ex) {
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
