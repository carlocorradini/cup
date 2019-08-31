package it.unitn.disi.wp.cup.bean.dao;

import it.unitn.disi.wp.cup.persistence.dao.DoctorDAO;
import it.unitn.disi.wp.cup.persistence.dao.PrescriptionExamDAO;
import it.unitn.disi.wp.cup.persistence.dao.PrescriptionMedicineDAO;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOFactoryException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;
import it.unitn.disi.wp.cup.persistence.entity.Doctor;
import it.unitn.disi.wp.cup.persistence.entity.Person;
import it.unitn.disi.wp.cup.persistence.entity.Medicine;
import it.unitn.disi.wp.cup.persistence.entity.Exam;
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
 * Doctor Bean
 *
 * @author Carlo Corradini
 * @see Doctor
 */
@Named("doctor")
@RequestScoped
public final class DoctorDaoBean implements Serializable {
    private static final long serialVersionUID = -4028930644505062207L;
    private static final Logger LOGGER = Logger.getLogger(DoctorDaoBean.class.getName());

    private DoctorDAO doctorDAO = null;
    private PrescriptionMedicineDAO prescriptionMedicineDAO = null;
    private PrescriptionExamDAO prescriptionExamDAO = null;

    private Doctor authDoctor = null;

    /**
     * Initialize the {@link DoctorDaoBean}
     */
    @PostConstruct
    public void init() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();

        if (context.getRequest() instanceof HttpServletRequest) {
            authDoctor = AuthUtil.getAuthDoctor((HttpServletRequest) context.getRequest());
            try {
                doctorDAO = DAOFactory.getDAOFactory().getDAO(DoctorDAO.class);
                prescriptionMedicineDAO = DAOFactory.getDAOFactory().getDAO(PrescriptionMedicineDAO.class);
                prescriptionExamDAO = DAOFactory.getDAOFactory().getDAO(PrescriptionExamDAO.class);
            } catch (DAOFactoryException ex) {
                LOGGER.log(Level.SEVERE, "Unable to get DAOs", ex);
            }
        }
    }

    /**
     * Return the authenticated doctor in the current session
     *
     * @return The authenticated doctor in the current session, null otherwise
     */
    public Doctor getAuthDoctor() {
        return authDoctor;
    }

    /**
     * Return the {@link List list} of patients for the authenticated {@link Doctor Doctor}
     *
     * @return The {@link List list} of {@link Person Patients}
     */
    public List<Person> getPatients() {
        List<Person> patients = Collections.emptyList();

        if (authDoctor != null && doctorDAO != null) {
            try {
                patients = doctorDAO.getPatientsByDoctorId(authDoctor.getId());
            } catch (DAOException ex) {
                LOGGER.log(Level.SEVERE, "Unable to return the List of Patients for the Authenticated Doctor", ex);
            }
        }

        return patients;
    }

    /**
     * Return the Number of {@link Medicine medicines} prescribed by the Authenticated {@link Doctor doctor}
     *
     * @return Number of {@link Medicine medicines} prescribed
     */
    public long getPrescriptionMedicineCount() {
        long count = 0L;

        if (authDoctor != null && prescriptionMedicineDAO != null) {
            try {
                count = prescriptionMedicineDAO.getCountByDoctorId(authDoctor.getId());
            } catch (DAOException ex) {
                LOGGER.log(Level.SEVERE, "Unable to count the Prescription Medicine", ex);
            }
        }

        return count;
    }

    /**
     * Return the Number of {@link Exam exams} prescribed by the Authenticated {@link Doctor doctor}
     *
     * @return Number of {@link Exam exams} prescribed
     */
    public Long getPrescriptionExamCount() {
        long count = 0L;

        if (authDoctor != null && prescriptionExamDAO != null) {
            try {
                count = prescriptionExamDAO.getCountByDoctorId(authDoctor.getId());
            } catch (DAOException ex) {
                LOGGER.log(Level.SEVERE, "Unable to count the Prescription Exam", ex);
            }
        }

        return count;
    }

}
