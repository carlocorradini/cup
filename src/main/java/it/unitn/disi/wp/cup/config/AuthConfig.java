package it.unitn.disi.wp.cup.config;

import it.unitn.disi.wp.cup.config.exception.ConfigException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Authorization Configuration
 *
 * @author Carlo Corradini
 */
public final class AuthConfig extends Config {

    private static final String FILE_NAME = "auth.properties";
    private static final String CATEGORY = "auth";
    private static final Logger LOGGER = Logger.getLogger(AuthConfig.class.getName());
    private static AuthConfig instance;

    private AuthConfig() throws ConfigException {
        super(FILE_NAME, CATEGORY);
    }

    /**
     * Load the Auth Configuration
     *
     * @throws ConfigException If the instance has been already initialized
     */
    public static void load() throws ConfigException {
        if (instance == null) {
            instance = new AuthConfig();
        } else throw new ConfigException("AppConfig has been already initialized");
    }

    private static void checkInstance() throws ConfigException {
        if (instance == null) throw new ConfigException("AuthConfig has not been initialized");
    }

    /**
     * Return the session name of the authenticated Person
     *
     * @return Session Person name
     */
    public static String getSessionPersonName() {
        try {
            checkInstance();
        } catch (ConfigException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get Session Person Name", ex);
        }
        return instance.getString("session.person.name");
    }

    /**
     * Return the session name of the authenticated Doctor
     *
     * @return Session Doctor name
     */
    public static String getSessionDoctorName() {
        try {
            checkInstance();
        } catch (ConfigException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get Session Doctor Name", ex);
        }
        return instance.getString("session.doctor.name");
    }

    /**
     * Return the Cookie Remember Name
     *
     * @return Cookie Remember Name
     */
    public static String getCookieRememberName() {
        try {
            checkInstance();
        } catch (ConfigException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get Cookie Remember Name", ex);
        }
        return instance.getString("cookie.remember.name");
    }

    /**
     * Return the Cookie Remember Max Age
     *
     * @return Cookie Remember Max Age
     */
    public static int getCookieRememberMaxAge() {
        try {
            checkInstance();
        } catch (ConfigException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get Cookie Remember Max Age", ex);
        }
        return instance.getInt("cookie.remember.maxAge");
    }
}
