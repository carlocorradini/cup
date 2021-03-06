package it.unitn.disi.wp.cup.bean.dao;

import it.unitn.disi.wp.cup.persistence.dao.HealthServiceDAO;
import it.unitn.disi.wp.cup.persistence.dao.PersonDAO;
import it.unitn.disi.wp.cup.persistence.dao.PrescriptionExamDAO;
import it.unitn.disi.wp.cup.persistence.dao.PrescriptionMedicineDAO;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOFactoryException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;
import it.unitn.disi.wp.cup.persistence.entity.HealthService;
import it.unitn.disi.wp.cup.persistence.entity.Province;
import it.unitn.disi.wp.cup.persistence.entity.Person;
import it.unitn.disi.wp.cup.persistence.entity.PrescriptionExam;
import it.unitn.disi.wp.cup.persistence.entity.PrescriptionMedicine;
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
    private PrescriptionMedicineDAO prescriptionMedicineDAO = null;
    private HealthServiceDAO healthServiceDAO = null;
    private HealthService authHealthService = null;
    private PersonDAO personDAO = null;

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
                prescriptionMedicineDAO = DAOFactory.getDAOFactory().getDAO(PrescriptionMedicineDAO.class);
                personDAO = DAOFactory.getDAOFactory().getDAO(PersonDAO.class);
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
     * Return the {@link List} of {@link Person Patients} that live in the same {@link Province Province}
     * managed by the current authenticated {@link HealthService Health Service}
     *
     * @return The {@link List} of all {@link Person Patients}
     */
    public List<Person> getPatients() {
        List<Person> patients = Collections.emptyList();

        if (authHealthService != null && personDAO != null) {
            try {
                patients = personDAO.getAllByHealthServiceId(authHealthService.getId());
            } catch (DAOException ex) {
                LOGGER.log(Level.SEVERE, "Unable to get the List of All Patients for the authenticated Health Service", ex);
            }
        }

        return patients;
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
     * Return the {@link List List} of all assigned done {@link PrescriptionExam Prescription Exam}
     * for the authenticated {@link HealthService Health Service}
     *
     * @return The {@link List list} of all assigned done {@link PrescriptionExam Prescription Exam}
     */
    public List<PrescriptionExam> getAssignedDonePrescriptionExam() {
        List<PrescriptionExam> exams = Collections.emptyList();

        if (authHealthService != null && prescriptionExamDAO != null) {
            try {
                exams = prescriptionExamDAO.getAllDoneByHealthServiceId(authHealthService.getId());
            } catch (DAOException ex) {
                LOGGER.log(Level.SEVERE, "Unable to get the List of All Assigned done Prescription Exam for the Authenticated Health Service", ex);
            }
        }

        return exams;
    }

    /**
     * Return the {@link List List} of all NOT assigned/scheduled {@link PrescriptionExam Prescription Exam}
     * for the current Authenticated Health Service
     *
     * @return The {@link List List} of all NOT assigned {@link PrescriptionExam Prescription Exam}
     */
    public List<PrescriptionExam> getNotAssignedPrescriptionExam() {
        List<PrescriptionExam> exams = Collections.emptyList();

        if (authHealthService != null && prescriptionExamDAO != null) {
            try {
                exams = prescriptionExamDAO.getAllToAssignByHealthServiceId(authHealthService.getId());
            } catch (DAOException ex) {
                LOGGER.log(Level.SEVERE, "Unable to get the List of All Not Assigned Prescription Exam for the Authenticated Health Service", ex);
            }
        }

        return exams;
    }

    /**
     * Return the {@link List List} of all NOT assigned/scheduled {@link PrescriptionMedicine Prescription Medicine}
     * for the current Authenticated Health Service
     *
     * @return The {@link List List} of all NOT assigned {@link PrescriptionMedicine Prescription Medicine}
     */
    public List<PrescriptionMedicine> getNotAssignedPrescriptionMedicine() {
        List<PrescriptionMedicine> medicines = Collections.emptyList();

        if (authHealthService != null && prescriptionMedicineDAO != null) {
            try {
                medicines = prescriptionMedicineDAO.getAllToAssignByHealthServiceId(authHealthService.getId());
            } catch (DAOException ex) {
                LOGGER.log(Level.SEVERE, "Unable to get the List of All Not Assigned Prescription Medicine for the Authenticated Health Service", ex);
            }
        }

        return medicines;
    }

    /**
     * Return the Number of {@link PrescriptionExam exams} assigned but NOT performed to the Authenticated {@link HealthService Health Service}
     *
     * @return Number of assigned but NOT performed {@link PrescriptionExam exams}
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

    /**
     * Return the Number of {@link PrescriptionExam Prescription Exams} assigned and performed to the Authenticated {@link HealthService Health Service}
     *
     * @return Number of assigned and performed {@link PrescriptionExam Prescription Exams}
     */
    public Long getAssignedDonePrescriptionExamCount() {
        long count = 0L;

        if (authHealthService != null && prescriptionExamDAO != null) {
            try {
                count = prescriptionExamDAO.getCountDoneByHealthServiceId(authHealthService.getId());
            } catch (DAOException ex) {
                LOGGER.log(Level.SEVERE, "Unable to count the assigned and performed Prescription Exam for the authenticated Health Service", ex);
            }
        }

        return count;
    }

    /**
     * Return the Number of {@link PrescriptionExam Prescription Exams} that has not been assigned for the authenticated {@link HealthService Health Service}
     *
     * @return Number of not assigned {@link PrescriptionExam Prescription Exams}
     */
    public Long getNotAssignedPrescriptionExamCount() {
        long count = 0L;

        if (authHealthService != null && prescriptionExamDAO != null) {
            try {
                count = prescriptionExamDAO.getCountToAssignByHealthServiceId(authHealthService.getId());
            } catch (DAOException ex) {
                LOGGER.log(Level.SEVERE, "Unable to count the Prescription Exam that has not been assigned for the Authenticated Health Service", ex);
            }
        }

        return count;
    }

    /**
     * Return the Number of {@link PrescriptionMedicine Prescription Medicines} that has not been assigned for the authenticated {@link HealthService Health Service}
     *
     * @return Number of not assigned {@link PrescriptionMedicine Prescription Medicines}
     */
    public Long getNotAssignedPrescriptionMedicineCount() {
        long count = 0L;

        if (authHealthService != null && prescriptionMedicineDAO != null) {
            try {
                count = prescriptionMedicineDAO.getCountToAssignByHealthServiceId(authHealthService.getId());
            } catch (DAOException ex) {
                LOGGER.log(Level.SEVERE, "Unable to count the Prescription Medicine that has not been assigned for the Authenticated Health Service", ex);
            }
        }

        return count;
    }
}
