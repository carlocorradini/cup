package it.unitn.disi.wp.cup.bean.dao;

import it.unitn.disi.wp.cup.persistence.dao.DoctorDAO;
import it.unitn.disi.wp.cup.persistence.dao.PrescriptionExamDAO;
import it.unitn.disi.wp.cup.persistence.dao.PrescriptionMedicineDAO;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOFactoryException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;
import it.unitn.disi.wp.cup.persistence.entity.Doctor;
import it.unitn.disi.wp.cup.persistence.entity.Exam;
import it.unitn.disi.wp.cup.persistence.entity.Medicine;
import it.unitn.disi.wp.cup.persistence.entity.Person;
import it.unitn.disi.wp.cup.util.AuthUtil;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
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
    private PrescriptionMedicineDAO prescriptionMedicineDAO = null;
    private PrescriptionExamDAO prescriptionExamDAO = null;
    private Person authPerson = null;

    /**
     * Initialize the {@link PersonDaoBean}
     */
    @PostConstruct
    public void init() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();

        if (context.getRequest() instanceof HttpServletRequest) {
            authPerson = AuthUtil.getAuthPerson((HttpServletRequest) context.getRequest());
            if (authPerson != null) {
                try {
                    prescriptionMedicineDAO = DAOFactory.getDAOFactory().getDAO(PrescriptionMedicineDAO.class);
                    prescriptionExamDAO = DAOFactory.getDAOFactory().getDAO(PrescriptionExamDAO.class);
                } catch (DAOFactoryException ex) {
                    LOGGER.log(Level.SEVERE, "Unable to get DAO Factory", ex);
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
     * Return the Number of {@link Medicine medicines} prescribed for the Authenticated {@link Person person}
     *
     * @return Number of {@link Medicine medicines} prescribed
     */
    public long getPrescriptionMedicineCount() {
        long count = 0L;

        if (authPerson != null) {
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

        if (authPerson != null) {
            try {
                count = prescriptionExamDAO.getCountByPersonId(authPerson.getId());
            } catch (DAOException ex) {
                LOGGER.log(Level.SEVERE, "Unable to count the Prescription Exam", ex);
            }
        }

        return count;
    }
}
