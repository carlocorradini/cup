package it.unitn.disi.wp.cup.bean.dao;

import it.unitn.disi.wp.cup.persistence.dao.HealthServiceDAO;
import it.unitn.disi.wp.cup.persistence.dao.PrescriptionExamDAO;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOFactoryException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;
import it.unitn.disi.wp.cup.persistence.entity.HealthService;
import it.unitn.disi.wp.cup.persistence.entity.PrescriptionExam;
import it.unitn.disi.wp.cup.util.AuthUtil;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
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
public final class HealthServiceDaoBean implements Serializable {
    private static final long serialVersionUID = 9217930324705051203L;
    private static final Logger LOGGER = Logger.getLogger(HealthServiceDaoBean.class.getName());

    private PrescriptionExamDAO prescriptionExamDAO = null;
    private HealthServiceDAO healthServiceDAO = null;
    private HealthService authHealthService = null;

    /**
     * Initialize the {@link HealthServiceDaoBean}
     */
    @PostConstruct
    public void init() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();

        if (context.getRequest() instanceof HttpServletRequest) {
            authHealthService = AuthUtil.getAuthHealthService((HttpServletRequest) context.getRequest());
            try {
                healthServiceDAO = DAOFactory.getDAOFactory().getDAO(HealthServiceDAO.class);
                prescriptionExamDAO = DAOFactory.getDAOFactory().getDAO(PrescriptionExamDAO.class);
            } catch (DAOFactoryException ex) {
                LOGGER.log(Level.SEVERE, "Unable to get DAOs", ex);
            }
        }
    }

    /**
     * Return the authenticated Health Service in the current session
     *
     * @return The authenticated Health Service in the current session, null otherwise
     */
    public HealthService getAuthHealthService() {
        return authHealthService;
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

    /**
     * Return the {@link List list} of all assigned {@link PrescriptionExam Prescription Exam}
     * to the Authenticated {@link HealthService Health Service}
     *
     * @return The {@link List list} of all assigned {@link PrescriptionExam}
     */
    public List<PrescriptionExam> getAssignedPrescriptionExam() {
        List<PrescriptionExam> exams = Collections.emptyList();

        if (authHealthService != null && prescriptionExamDAO != null) {
            try {
                exams = prescriptionExamDAO.getAllByHealthServiceId(authHealthService.getId());
            } catch (DAOException ex) {
                LOGGER.log(Level.SEVERE, "Unable to get the List of All Assigned Prescription Exam for authenticated Health Service", ex);
            }
        }

        return exams;
    }

    /**
     * Return the {@link List list} of all assigned but not performed {@link PrescriptionExam Prescription Exam}
     * for the Authenticated {@link HealthService Health Service}
     *
     * @return The {@link List list} of all assigned but never performed {@link PrescriptionExam Prescription Exam}
     */
    public List<PrescriptionExam> getAssignedToDoPrescriptionExam() {
        List<PrescriptionExam> exams = Collections.emptyList();

        if (authHealthService != null && prescriptionExamDAO != null) {
            try {
                exams = prescriptionExamDAO.getAllToDoByHealthServiceId(authHealthService.getId());
            } catch (DAOException ex) {
                LOGGER.log(Level.SEVERE, "Unable to get the List of All Assigned but not performed Prescription Exam for the authenticated Health Service", ex);
            }
        }

        return exams;
    }

    /**
     * Return the Number of {@link PrescriptionExam exams} assigned to the Authenticated {@link HealthService Health Service}
     *
     * @return Number of {@link PrescriptionExam exams} assigned
     */
    public Long getAssignedPrescriptionExamCount() {
        long count = 0L;

        if (authHealthService != null && prescriptionExamDAO != null) {
            try {
                count = prescriptionExamDAO.getCountByHealthServiceId(authHealthService.getId());
            } catch (DAOException ex) {
                LOGGER.log(Level.SEVERE, "Unable to count the assigned Prescription Exam for the authenticated Health Service", ex);
            }
        }

        return count;
    }

    /**
     * Return the Number of {@link PrescriptionExam exams} assigned but not performed to the Authenticated {@link HealthService Health Service}
     *
     * @return Number of assigned but not performed {@link PrescriptionExam exams}
     */
    public Long getAssignedToDoPrescriptionExamCount() {
        long count = 0L;

        if (authHealthService != null && prescriptionExamDAO != null) {
            try {
                count = prescriptionExamDAO.getCountToDoByHealthServiceId(authHealthService.getId());
            } catch (DAOException ex) {
                LOGGER.log(Level.SEVERE, "Unable to count the assigned and not performed Prescription Exam for the authenticated Health Service", ex);
            }
        }

        return count;
    }

}
