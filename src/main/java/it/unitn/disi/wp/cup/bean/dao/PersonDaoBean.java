package it.unitn.disi.wp.cup.bean.dao;

import it.unitn.disi.wp.cup.persistence.dao.PersonDAO;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOFactoryException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;
import it.unitn.disi.wp.cup.persistence.entity.Person;
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
 * Person Bean
 *
 * @author Carlo Corradini
 * @see it.unitn.disi.wp.cup.persistence.entity.Person
 * @see PersonDAO
 */
@ManagedBean(name = "person")
@RequestScoped
public final class PersonDaoBean implements Serializable {

    private static final long serialVersionUID = -4058980644509062209L;
    private final static Logger LOGGER = Logger.getLogger(PersonDaoBean.class.getName());
    private PersonDAO personDAO = null;
    private Person person = null;

    /**
     * Initialize the personDAO
     */
    @PostConstruct
    public void init() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        try {
            personDAO = DAOFactory.getDAOFactory().getDAO(PersonDAO.class);
            if (context.getRequest() instanceof HttpServletRequest) {
                person = AuthUtil.getAuthPerson((HttpServletRequest) context.getRequest());
            }
        } catch (DAOFactoryException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Return the PersonDAO
     *
     * @return The PersonDAO
     */
    public PersonDAO getPersonDAO() {
        return personDAO;
    }

    /**
     * Return the person in the current session
     *
     * @return The person in the current session, null otherwise
     */
    public Person getPerson() {
        return person;
    }
}
