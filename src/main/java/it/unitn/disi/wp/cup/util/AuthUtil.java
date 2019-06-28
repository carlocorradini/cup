package it.unitn.disi.wp.cup.util;

import it.unitn.disi.wp.cup.config.AuthConfig;
import it.unitn.disi.wp.cup.persistence.dao.PersonDAO;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOFactoryException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;
import it.unitn.disi.wp.cup.persistence.entity.Person;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Util class for Authentication process
 *
 * @author Carlo Corradini
 */
public final class AuthUtil {
    private static final Logger LOGGER = Logger.getLogger(AuthUtil.class.getName());
    private static PersonDAO personDAO = null;

    /**
     * Configure the class
     *
     * @param daoFactory The daoFactory to get information from
     * @throws NullPointerException If DAOFactory is null
     */
    public static void configure(final DAOFactory daoFactory) throws NullPointerException {
        if (daoFactory == null)
            throw new NullPointerException("DAOFactory cannot be null");
        try {
            personDAO = daoFactory.getDAO(PersonDAO.class);
        } catch (DAOFactoryException | NullPointerException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get DAOFactory", ex);
        }
    }

    private static void isConfigured() throws NullPointerException {
        if (personDAO == null) throw new NullPointerException("PersonDAO cannot be null");
    }

    /**
     * Return the current authenticated Person
     *
     * @param req The ServletRequest
     * @return The auth Person, null otherwise
     */
    public static Person getAuthPerson(HttpServletRequest req) {
        Person person = null;
        HttpSession session;

        try {
            isConfigured();
            if (req != null) {
                session = req.getSession(false);
                if (session != null) {
                    person = (Person) session.getAttribute(AuthConfig.getSessionName());
                }
            }
        } catch (NullPointerException ex) {
            LOGGER.log(Level.SEVERE, "Unable to Current Auth Person", ex);
        }

        return person;
    }

    /**
     * Perform a Sign In
     *
     * @param email    Email of the Person
     * @param password Password of the Person
     * @param remember set to true for 'remember me'
     * @param req      The request
     * @param resp     The response
     * @return The authenticated Person, null otherwise
     */
    public static Person signIn(String email, String password, boolean remember, HttpServletRequest req, HttpServletResponse resp) {
        Person person = null;

        try {
            isConfigured();
            if (email != null && password != null && req != null) {
                Person noAuthPerson = personDAO.getByEmail(email);
                if (noAuthPerson != null && CryptUtil.validate(password, noAuthPerson.getPassword()) && getAuthPerson(req) == null) {
                    person = noAuthPerson;
                    req.getSession().setAttribute(AuthConfig.getSessionName(), person);
                }
            }
        } catch (DAOException | NullPointerException ex) {
            LOGGER.log(Level.SEVERE, "Unable to Sign In", ex);
        }

        return person;
    }

    /**
     * Perform a Sign Out
     *
     * @param req  The request
     * @param resp The response
     * @return The old authenticated Person, null otherwise
     */
    public static Person signOut(HttpServletRequest req, HttpServletResponse resp) {
        Person person = null;
        HttpSession session;

        try {
            isConfigured();
            if (req != null) {
                session = req.getSession(false);
                if (session != null) {
                    person = getAuthPerson(req);
                    if (person != null) {
                        session.removeAttribute(AuthConfig.getSessionName());
                        session.invalidate();
                    }
                }
            }
        } catch (NullPointerException ex) {
            LOGGER.log(Level.SEVERE, "Unable to Sign Out", ex);
        }

        return person;
    }
}