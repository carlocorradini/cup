package it.unitn.disi.wp.cup.bean.dao;

import it.unitn.disi.wp.cup.persistence.dao.DoctorDAO;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOFactoryException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;
import it.unitn.disi.wp.cup.persistence.entity.Doctor;
import it.unitn.disi.wp.cup.util.AuthUtil;

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
    private final static Logger LOGGER = Logger.getLogger(DoctorDaoBean.class.getName());
    private DoctorDAO doctorDAO = null;
    private Doctor authDoctor = null;

    /**
     * Initialize the {@link DoctorDaoBean}
     */
    @PostConstruct
    public void init() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        try {
            doctorDAO = DAOFactory.getDAOFactory().getDAO(DoctorDAO.class);
            if (context.getRequest() instanceof HttpServletRequest) {
                authDoctor = AuthUtil.getAuthDoctor((HttpServletRequest) context.getRequest());
            }
        } catch (DAOFactoryException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Return the DoctorDAO
     *
     * @return The DoctorDAO
     */
    public DoctorDAO getDoctorDAO() {
        return doctorDAO;
    }

    /**
     * Return the Authenticated doctor in the current session
     *
     * @return The Authenticated doctor in the current session, null otherwise
     */
    public Doctor getAuthDoctor() {
        return authDoctor;
    }
}
