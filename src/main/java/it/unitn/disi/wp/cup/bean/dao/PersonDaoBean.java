package it.unitn.disi.wp.cup.bean.dao;

import it.unitn.disi.wp.cup.config.AuthConfig;
import it.unitn.disi.wp.cup.config.exception.ConfigException;
import it.unitn.disi.wp.cup.persistence.dao.PersonDAO;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOFactoryException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;
import it.unitn.disi.wp.cup.persistence.entity.Person;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;

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
    private PersonDAO personDAO;
    private Person person;

    /**
     * Initialize the personDAO
     */
    @PostConstruct
    public void init() {
        try {
            personDAO = DAOFactory.getDAOFactory().getDAO(PersonDAO.class);
            person = (Person) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(AuthConfig.getSessionName());
        } catch (DAOFactoryException | ConfigException ex) {
            throw new RuntimeException(ex);
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
