package it.unitn.disi.wp.cup.config;

import it.unitn.disi.wp.cup.config.exception.ConfigException;

/**
 * Database Config for loading Database Configuration files
 *
 * @author Carlo Corradini
 */
public final class DatabaseConfig extends Config {

    private static final String FILE_NAME = "database.properties";
    private static final String CATEGORY = "db";
    private static DatabaseConfig instance;

    private DatabaseConfig() throws ConfigException {
        super(FILE_NAME, CATEGORY);
    }

    /**
     * Load the Database Configuration
     *
     * @throws ConfigException If the instance has been already initialized
     */
    public static void load() throws ConfigException {
        if (instance == null) {
            instance = new DatabaseConfig();
        } else throw new ConfigException("DatabaseConfig has been already initialized");
    }

    private static void checkInstance() throws ConfigException {
        if (instance == null) throw new ConfigException("DatabaseConfig has not been initialized");
    }

    /**
     * Return the formatted Database Url
     *
     * @return Url DB
     * @throws ConfigException If the instance has not been initialized
     */
    public static String getUrl() throws ConfigException {
        checkInstance();
        return String.format(instance.getString("url"),
                DatabaseConfig.getHost(),
                DatabaseConfig.getPort(),
                DatabaseConfig.getName(),
                DatabaseConfig.getUsername(),
                DatabaseConfig.getPassword(),
                DatabaseConfig.getSsl());
    }

    /**
     * Return the host of the Database
     *
     * @return Host DB
     * @throws ConfigException If the instance has not been initialized
     */
    public static String getHost() throws ConfigException {
        checkInstance();
        return instance.getString("host");
    }

    /**
     * Return the port of the Database
     *
     * @return Port DB
     * @throws ConfigException If the instance has not been initialized
     */
    public static int getPort() throws ConfigException {
        checkInstance();
        return instance.getInt("port");
    }

    /**
     * Return the name of the Database
     *
     * @return Name DB
     * @throws ConfigException If the instance has not been initialized
     */
    public static String getName() throws ConfigException {
        checkInstance();
        return instance.getString("name");
    }

    /**
     * Return the username of the Database
     *
     * @return Username DB
     * @throws ConfigException If the instance has not been initialized
     */
    public static String getUsername() throws ConfigException {
        checkInstance();
        return instance.getString("username");
    }

    /**
     * Return the password of the Database
     *
     * @return Password DB
     * @throws ConfigException If the instance has not been initialized
     */
    public static String getPassword() throws ConfigException {
        checkInstance();
        return instance.getString("password");
    }

    /**
     * Return the ssl boolean of the Database
     *
     * @return true if DB need SSL, false otherwise
     * @throws ConfigException If the instance has not been initialized
     */
    public static boolean getSsl() throws ConfigException {
        checkInstance();
        return instance.getBoolean("ssl");
    }
}
