package it.unitn.disi.wp.cup.config;

import it.unitn.disi.wp.cup.config.exception.ConfigException;

/**
 * Database Config for loading Database Configuration files
 *
 * @author Carlo Corradini
 */
public final class AuthConfig extends Config {

    private static final String FILE_NAME = "auth.properties";
    private static final String CATEGORY = "auth";
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
        } else throw new ConfigException("DatabaseConfig has been already initialized");
    }

    private static void checkInstance() throws ConfigException {
        if (instance == null) throw new ConfigException("AuthConfig has not been initialized");
    }

    /**
     * Return the session parameter name of the authenticated Person
     *
     * @return Session Name
     * @throws ConfigException If the instance has not been initialized
     */
    public static String getSessionName() throws ConfigException {
        checkInstance();
        return instance.getString("session.name");
    }
}
