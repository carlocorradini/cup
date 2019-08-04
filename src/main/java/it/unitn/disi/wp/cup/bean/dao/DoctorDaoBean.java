package it.unitn.disi.wp.cup.bean.dao;

import it.unitn.disi.wp.cup.persistence.dao.DoctorDAO;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOFactoryException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;
import it.unitn.disi.wp.cup.persistence.entity.Doctor;
import it.unitn.disi.wp.cup.persistence.entity.Person;
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
    private Doctor authDoctor = null;
    private List<Person> patients = Collections.emptyList();

    /**
     * Initialize the {@link DoctorDaoBean}
     */
    @PostConstruct
    public void init() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        DoctorDAO doctorDAO;

        if (context.getRequest() instanceof HttpServletRequest) {
            authDoctor = AuthUtil.getAuthDoctor((HttpServletRequest) context.getRequest());
            if (authDoctor != null) {
                try {
                    doctorDAO = DAOFactory.getDAOFactory().getDAO(DoctorDAO.class);
                    patients = doctorDAO.getPatientsByDoctorId(authDoctor.getId());
                } catch (DAOFactoryException ex) {
                    LOGGER.log(Level.SEVERE, "Unable to get Doctor DAO", ex);
                } catch (DAOException ex) {
                    LOGGER.log(Level.SEVERE, "Unable to get Patients", ex);
                }
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
     * Return the List of patients for the authenticated Doctor
     *
     * @return The List of patients
     */
    public List<Person> getPatients() {
        return patients;
    }

}
