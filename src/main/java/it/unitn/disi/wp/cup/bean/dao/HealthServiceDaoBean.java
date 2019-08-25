package it.unitn.disi.wp.cup.bean.dao;

import it.unitn.disi.wp.cup.persistence.dao.HealthServiceDAO;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOFactoryException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;
import it.unitn.disi.wp.cup.persistence.entity.HealthService;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * {@link HealthService Health Service} Bean
 *
 * @author Carlo Corradini
 * @see HealthService
 */
@Named("healthService")
@RequestScoped
public class HealthServiceDaoBean implements Serializable {
    private static final long serialVersionUID = 9217930324705051203L;
    private static final Logger LOGGER = Logger.getLogger(HealthServiceDaoBean.class.getName());
    private HealthServiceDAO healthServiceDAO = null;

    /**
     * Initialize the {@link HealthServiceDaoBean}
     */
    @PostConstruct
    public void init() {
        try {
            healthServiceDAO = DAOFactory.getDAOFactory().getDAO(HealthServiceDAO.class);
        } catch (DAOFactoryException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get DAOs", ex);
        }
    }

    /**
     * Return the {@link List list} of all {@link HealthService Health Services} available
     *
     * @return The {@link List list} of all {@link HealthService Health Services} available
     */
    public List<HealthService> getHealthServices() {
        List<HealthService> healthServices = Collections.emptyList();

        if (healthServiceDAO != null) {
            try {
                healthServices = healthServiceDAO.getAll();
            } catch (DAOException ex) {
                LOGGER.log(Level.SEVERE, "Unable to get the List of Health Services", ex);
            }
        }

        return healthServices;
    }

}
