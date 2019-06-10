package it.unitn.disi.wp.cup.bean.dao;

import it.unitn.disi.wp.cup.persistence.dao.PersonDAO;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOFactoryException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
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
public final class PersonBean implements Serializable {

    private static final long serialVersionUID = -4058980644509062209L;
    private PersonDAO personDAO;

    /**
     * Initialize the personDAO
     */
    @PostConstruct
    public void init() {
        try {
            personDAO = DAOFactory.getDAOFactory().getDAO(PersonDAO.class);
        } catch (DAOFactoryException ex) {
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
}
