package it.unitn.disi.wp.cup.util;

import it.unitn.disi.wp.cup.config.AuthConfig;
import it.unitn.disi.wp.cup.persistence.dao.DoctorDAO;
import it.unitn.disi.wp.cup.persistence.dao.DoctorSpecialistDAO;
import it.unitn.disi.wp.cup.persistence.dao.HealthServiceDAO;
import it.unitn.disi.wp.cup.persistence.dao.PersonDAO;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOFactoryException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;
import it.unitn.disi.wp.cup.persistence.entity.Doctor;
import it.unitn.disi.wp.cup.persistence.entity.DoctorSpecialist;
import it.unitn.disi.wp.cup.persistence.entity.HealthService;
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
    private static DoctorDAO doctorDAO = null;
    private static DoctorSpecialistDAO doctorSpecialistDAO = null;
    private static HealthServiceDAO healthServiceDAO = null;

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
            doctorDAO = daoFactory.getDAO(DoctorDAO.class);
            doctorSpecialistDAO = daoFactory.getDAO(DoctorSpecialistDAO.class);
            healthServiceDAO = daoFactory.getDAO(HealthServiceDAO.class);
        } catch (DAOFactoryException | NullPointerException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get from DAOFactory", ex);
        }
    }

    private static void isConfigured() throws NullPointerException {
        if (personDAO == null || doctorDAO == null || doctorSpecialistDAO == null || healthServiceDAO == null)
            throw new NullPointerException("AuthUtil has not been configured");
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
                    person = (Person) session.getAttribute(AuthConfig.getSessionPersonName());
                }
            }
        } catch (NullPointerException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get Current Auth Person", ex);
        }

        return person;
    }

    /**
     * Return the current authenticated Doctor
     *
     * @param req The ServletRequest
     * @return The auth Doctor, null otherwise
     */
    public static Doctor getAuthDoctor(HttpServletRequest req) {
        Doctor doctor = null;
        HttpSession session;

        try {
            isConfigured();
            if (req != null) {
                session = req.getSession(false);
                if (session != null) {
                    doctor = (Doctor) session.getAttribute(AuthConfig.getSessionDoctorName());
                }
            }
        } catch (NullPointerException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get Current Auth Doctor", ex);
        }

        return doctor;
    }

    /**
     * Return the current authenticated Doctor Specialist
     *
     * @param req The ServletRequest
     * @return The auth Doctor Specialist, null otherwise
     */
    public static DoctorSpecialist getAuthDoctorSpecialist(HttpServletRequest req) {
        DoctorSpecialist doctorSpecialist = null;
        HttpSession session;

        try {
            isConfigured();
            if (req != null) {
                session = req.getSession(false);
                if (session != null) {
                    doctorSpecialist = (DoctorSpecialist) session.getAttribute(AuthConfig.getSessionDoctorSpecialistName());
                }
            }
        } catch (NullPointerException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get Current Auth Doctor Specialist", ex);
        }

        return doctorSpecialist;
    }

    /**
     * Return the current authenticated Health Service
     *
     * @param req The ServletRequest
     * @return The auth Health Service, null otherwise
     */
    public static HealthService getAuthHealthService(HttpServletRequest req) {
        HealthService healthService = null;
        HttpSession session;

        try {
            isConfigured();
            if (req != null) {
                session = req.getSession(false);
                if (session != null) {
                    healthService = (HealthService) session.getAttribute(AuthConfig.getSessionHealthServiceName());
                }
            }
        } catch (NullPointerException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get current Auth Health service", ex);
        }

        return healthService;
    }

    /**
     * Perform a Sign In
     *
     * @param email    Email of the Person
     * @param password Password of the Person
     * @param remember Set to true for 'remember me'
     * @param req      The request
     * @return The authenticated Person, null otherwise
     */
    public static Person signIn(String email, String password, boolean remember, HttpServletRequest req) {
        Person person = null;
        Doctor doctor;
        DoctorSpecialist doctorSpecialist;
        HttpSession session;

        try {
            isConfigured();
            if (email != null && password != null && req != null) {
                Person noAuthPerson = personDAO.getByEmail(email);

                if (noAuthPerson != null && CryptUtil.validate(password, noAuthPerson.getPassword()) && getAuthPerson(req) == null) {
                    session = req.getSession(true);
                    person = noAuthPerson;
                    doctor = doctorDAO.getByPrimaryKey(person.getId());
                    doctorSpecialist = doctorSpecialistDAO.getByPrimaryKey(person.getId());

                    // Add Person to the current session
                    session.setAttribute(AuthConfig.getSessionPersonName(), person);
                    if (doctor != null) {
                        // The Person is a Doctor
                        session.setAttribute(AuthConfig.getSessionDoctorName(), doctor);
                    }
                    if (doctorSpecialist != null) {
                        // The Person is a Doctor Specialist
                        session.setAttribute(AuthConfig.getSessionDoctorSpecialistName(), doctorSpecialist);
                    }

                    // === REMEMBER ME
                    if (remember) {
                        // Change session Timeout
                        session.setMaxInactiveInterval(AuthConfig.getCookieRememberMaxAge());
                    }
                }
            }
        } catch (DAOException | NullPointerException ex) {
            LOGGER.log(Level.SEVERE, "Unable to Sign In", ex);
        }

        return person;
    }

    /**
     * Perform a Sign In for {@link HealthService Health Service} users only
     *
     * @param id       The id to identify the {@link HealthService Health Service}
     * @param password The password to validate the {@link HealthService Health Service} represented by {@code id}
     * @param remember Set to true for 'remember me'
     * @param req      The request
     * @return The authenticated Health Service, null otherwise
     */
    public static HealthService signInHealthService(Long id, String password, boolean remember, HttpServletRequest req) {
        HealthService healthService = null;
        HttpSession session;

        try {
            isConfigured();
            if (id != null && password != null && req != null) {
                HealthService noAuthHealthService = healthServiceDAO.getByPrimaryKey(id);

                if (noAuthHealthService != null && CryptUtil.validate(password, noAuthHealthService.getPassword()) && getAuthHealthService(req) == null) {
                    session = req.getSession(true);
                    healthService = noAuthHealthService;

                    // Add Health Service to the current session
                    session.setAttribute(AuthConfig.getSessionHealthServiceName(), healthService);

                    // === REMEMBER ME
                    if (remember) {
                        // Change session Timeout
                        session.setMaxInactiveInterval(AuthConfig.getCookieRememberMaxAge());
                    }
                }
            }
        } catch (DAOException | NullPointerException ex) {
            LOGGER.log(Level.SEVERE, "Unable to Sign In with Health Service", ex);
        }

        return healthService;
    }

    /**
     * Perform a Sign Out for all
     *
     * @param req The request
     * @return The old authenticated Person, null otherwise
     */
    public static boolean signOut(HttpServletRequest req) {
        boolean out = false;
        HttpSession session;

        try {
            isConfigured();
            if (req != null) {
                session = req.getSession(false);
                if (session != null) {
                    session.removeAttribute(AuthConfig.getSessionPersonName());
                    session.removeAttribute(AuthConfig.getSessionDoctorName());
                    session.removeAttribute(AuthConfig.getSessionDoctorSpecialistName());
                    session.removeAttribute(AuthConfig.getSessionHealthServiceName());
                    session.invalidate();
                    out = true;
                }
            }
        } catch (NullPointerException ex) {
            LOGGER.log(Level.SEVERE, "Unable to Sign Out", ex);
        }

        return out;
    }
}
