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
     * Return the session parameter name of the authenticated Person
     *
     * @return Session Name
     */
    public static String getSessionName() {
        try {
            checkInstance();
        } catch (ConfigException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get Session Name", ex);
        }
        return instance.getString("session.name");
    }
}
