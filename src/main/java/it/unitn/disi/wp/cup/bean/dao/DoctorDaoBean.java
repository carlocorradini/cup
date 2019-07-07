package it.unitn.disi.wp.cup.bean.dao;

import it.unitn.disi.wp.cup.persistence.dao.DoctorDAO;
import it.unitn.disi.wp.cup.persistence.dao.PersonDAO;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOFactoryException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;
import it.unitn.disi.wp.cup.persistence.entity.Doctor;
import it.unitn.disi.wp.cup.persistence.entity.Person;
import it.unitn.disi.wp.cup.util.AuthUtil;
import org.apache.commons.lang3.math.NumberUtils;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Doctor Bean
 *
 * @author Carlo Corradini
 * @see Doctor
 */
@ManagedBean(name = "doctor")
@RequestScoped
public final class DoctorDaoBean implements Serializable {
    private static final long serialVersionUID = -4028930644505062207L;
    private static final Logger LOGGER = Logger.getLogger(DoctorDaoBean.class.getName());
    private static final String PARAM_PATIENT_ID = "patientId";
    private Doctor authDoctor = null;
    private Person patient = null;

    /**
     * Initialize the {@link DoctorDaoBean}
     * If {@code PARAM_PATIENT_ID Patient Id} is invalid {@code patient} is null
     * * If the {@link Person patient} is not in care under the {@code authDoctor Doctor} {@code patient} is null
     */
    @PostConstruct
    public void init() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        Long patientId;
        DoctorDAO doctorDAO;
        PersonDAO personDAO;

        if (context.getRequest() instanceof HttpServletRequest) {
            authDoctor = AuthUtil.getAuthDoctor((HttpServletRequest) context.getRequest());
            patientId = NumberUtils.toLong(context.getRequestParameterMap().get(PARAM_PATIENT_ID), Person.NOT_A_VALID_ID);
            if (authDoctor != null && !patientId.equals(Person.NOT_A_VALID_ID)) {
                try {
                    doctorDAO = DAOFactory.getDAOFactory().getDAO(DoctorDAO.class);
                    personDAO = DAOFactory.getDAOFactory().getDAO(PersonDAO.class);

                    if (authDoctor.equals(doctorDAO.getDoctorByPatientId(patientId))) {
                        patient = personDAO.getByPrimaryKey(patientId);
                    }
                } catch (DAOFactoryException | DAOException ex) {
                    LOGGER.log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Return the Authenticated doctor in the current session
     *
     * @return The Authenticated doctor in the current session, null otherwise
     */
    public Doctor getAuthDoctor() {
        return authDoctor;
    }

    /**
     * Return the requested {@link Person patient} as GET parameter identified by {@code PARAM_PATIENT_ID}
     *
     * @return The requested {@link Person patient}
     */
    public Person getPatient() {
        return patient;
    }
    
}
