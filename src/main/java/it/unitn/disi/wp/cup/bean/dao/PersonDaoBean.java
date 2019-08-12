package it.unitn.disi.wp.cup.bean.dao;

import it.unitn.disi.wp.cup.persistence.dao.*;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOFactoryException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;
import it.unitn.disi.wp.cup.persistence.entity.*;
import it.unitn.disi.wp.cup.util.AuthUtil;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Person Bean
 *
 * @author Carlo Corradini
 * @see Person
 */
@Named("person")
@RequestScoped
public final class PersonDaoBean implements Serializable {

    private static final long serialVersionUID = -4058980644509062209L;
    private static final Logger LOGGER = Logger.getLogger(PersonDaoBean.class.getName());
    private DoctorDAO doctorDAO = null;
    private PersonAvatarDAO personAvatarDAO = null;
    private PrescriptionMedicineDAO prescriptionMedicineDAO = null;
    private PrescriptionExamDAO prescriptionExamDAO = null;
    private Person authPerson = null;
    private Person doctor = null;

    /**
     * Initialize the {@link PersonDaoBean}
     */
    @PostConstruct
    public void init() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        PersonDAO personDAO;
        Doctor _doctor;

        if (context.getRequest() instanceof HttpServletRequest) {
            authPerson = AuthUtil.getAuthPerson((HttpServletRequest) context.getRequest());
            if (authPerson != null) {
                try {
                    personDAO = DAOFactory.getDAOFactory().getDAO(PersonDAO.class);
                    doctorDAO = DAOFactory.getDAOFactory().getDAO(DoctorDAO.class);
                    personAvatarDAO = DAOFactory.getDAOFactory().getDAO(PersonAvatarDAO.class);
                    prescriptionMedicineDAO = DAOFactory.getDAOFactory().getDAO(PrescriptionMedicineDAO.class);
                    prescriptionExamDAO = DAOFactory.getDAOFactory().getDAO(PrescriptionExamDAO.class);

                    // Check if the authenticated Person has a Doctor
                    if ((_doctor = doctorDAO.getDoctorByPatientId(authPerson.getId())) != null) {
                        doctor = personDAO.getByPrimaryKey(_doctor.getId());
                    }
                } catch (DAOFactoryException ex) {
                    LOGGER.log(Level.SEVERE, "Unable to get DAO Factory", ex);
                } catch (DAOException ex) {
                    LOGGER.log(Level.SEVERE, "Unable to get DAOs", ex);
                }
            }
        }
    }

    /**
     * Return the Authenticated person in the current session
     *
     * @return The Authenticated person in the current session, null otherwise
     */
    public Person getAuthPerson() {
        return authPerson;
    }

    /**
     * Return the doctor as a {@link Person person} for the Authenticated Person
     *
     * @return The doctor of the {@link Person person}
     */
    public Person getDoctor() {
        return doctor;
    }

    /**
     * Return a {@link List list} of {@link Doctor doctors} represented as {@link Person persons} filtered
     * by the living {@link Province province} and {@link Doctor doctor's} history of the Authenticated {@link Person person}
     *
     * @return A {@link List list} of available {@link Doctor doctors} with the same {@link Province province}
     * and not in the {@link Doctor doctor's} history of the Authenticated {@link Person person}
     */
    public List<Person> getDoctors() {
        List<Person> doctors = Collections.emptyList();

        if (authPerson != null) {
            try {
                doctors = doctorDAO.getAllAvailableByPatientId(authPerson.getId());
            } catch (DAOException ex) {
                LOGGER.log(Level.SEVERE, "Unable to get the List of Doctors filtered by Province id and history");
            }
        }

        return doctors;
    }

    /**
     * Return the {@link LocalDateTime date} since the Authenticated {@link Person person} is a patient of the current {@link Doctor doctor}
     * If the {@link Person person} hasn't a {@link Doctor doctor} null will be returned
     *
     * @return The {@link LocalDateTime date} since the Authenticated {@link Person person} is a patient
     */
    public LocalDateTime getDoctorSince() {
        LocalDateTime since = null;

        if (authPerson != null && doctor != null) {
            try {
                since = doctorDAO.getSinceByPatientId(authPerson.getId());
            } catch (DAOException ex) {
                LOGGER.log(Level.SEVERE, "Unable to get Since for the authenticated Person");
            }
        }

        return since;
    }

    /**
     * Return the {@link List list} of all {@link PersonAvatar avatars} that the {@link Person person} used
     *
     * @return The {@link List list} of {@link PersonAvatar avatars}
     */
    public List<PersonAvatar> getAvatarHistory() {
        List<PersonAvatar> history = Collections.emptyList();

        if (authPerson != null && personAvatarDAO != null) {
            try {
                history = personAvatarDAO.getAllByPersonId(authPerson.getId());
            } catch (DAOException ex) {
                LOGGER.log(Level.SEVERE, "Unable to get the List of Avatar History fot the authenticated Person");
            }
        }

        return history;
    }

    /**
     * Return the {@link List list} of all {@link PrescriptionMedicine prescribed} {@link Medicine medicines} for the authenticated {@link Person person}
     *
     * @return The {@link List list} of prescribed {@link Medicine medicines}
     */
    public List<PrescriptionMedicine> getPrescribedMedicines() {
        List<PrescriptionMedicine> medicines = Collections.emptyList();

        if (authPerson != null && prescriptionMedicineDAO != null) {
            try {
                medicines = prescriptionMedicineDAO.getAllByPersonId(authPerson.getId());
            } catch (DAOException ex) {
                LOGGER.log(Level.SEVERE, "Unable to get the List of Prescribed Medicines for the authenticated Person");
            }
        }

        return medicines;
    }

    /**
     * Return the {@link List list} of all {@link PrescriptionExam prescribed} {@link Exam exams} for the authenticated {@link Person person}
     *
     * @return The {@link List list} of prescribed {@link Exam exams}
     */
    public List<PrescriptionExam> getPrescribedExams() {
        List<PrescriptionExam> exams = Collections.emptyList();

        if (authPerson != null && prescriptionExamDAO != null) {
            try {
                exams = prescriptionExamDAO.getAllByPersonId(authPerson.getId());
            } catch (DAOException ex) {
                LOGGER.log(Level.SEVERE, "Unable to get the List of Prescribed Exams for the authenticated Person");
            }
        }

        return exams;
    }

    /**
     * Return the Number of {@link Medicine medicines} prescribed for the Authenticated {@link Person person}
     *
     * @return Number of {@link Medicine medicines} prescribed
     */
    public long getPrescriptionMedicineCount() {
        long count = 0L;

        if (authPerson != null && prescriptionMedicineDAO != null) {
            try {
                count = prescriptionMedicineDAO.getCountByPersonId(authPerson.getId());
            } catch (DAOException ex) {
                LOGGER.log(Level.SEVERE, "Unable to count the Prescription Medicine", ex);
            }
        }

        return count;
    }

    /**
     * Return the Number of {@link Exam exams} prescribed for the Authenticated {@link Person person}
     *
     * @return Number of {@link Exam exams} prescribed
     */
    public Long getPrescriptionExamCount() {
        long count = 0L;

        if (authPerson != null && prescriptionExamDAO != null) {
            try {
                count = prescriptionExamDAO.getCountByPersonId(authPerson.getId());
            } catch (DAOException ex) {
                LOGGER.log(Level.SEVERE, "Unable to count the Prescription Exam", ex);
            }
        }

        return count;
    }
}
