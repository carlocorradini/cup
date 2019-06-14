package it.unitn.disi.wp.cup.config;

import it.unitn.disi.wp.cup.config.exception.ConfigException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Database Config for loading Database Configuration files
 *
 * @author Carlo Corradini
 */
public final class DatabaseConfig extends Config {

    private static final String FILE_NAME = "database.properties";
    private static final String CATEGORY = "db";
    private static final Logger LOGGER = Logger.getLogger(DatabaseConfig.class.getName());
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
     * @see org.apache.commons.configuration2.PropertiesConfiguration
     */
    public static String getUrl() {
        try {
            checkInstance();
        } catch (ConfigException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get DB Url", ex);
        }
        return instance.getString("url");
    }

    /**
     * Return the formatted Database Driver
     *
     * @return Driver DB
     * @see org.apache.commons.configuration2.PropertiesConfiguration
     */
    public static String getDriver() {
        try {
            checkInstance();
        } catch (ConfigException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get DB Driver", ex);
        }
        return instance.getString("driver");
    }

    /**
     * Return the vendor of the Database
     *
     * @return Vendor DB
     */
    public static String getVendor() {
        try {
            checkInstance();
        } catch (ConfigException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get DB Vendor", ex);
        }
        return instance.getString("vendor");
    }

    /**
     * Return the host of the Database
     *
     * @return Host DB
     */
    public static String getHost() {
        try {
            checkInstance();
        } catch (ConfigException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get DB Host", ex);
        }
        return instance.getString("host");
    }

    /**
     * Return the port of the Database
     *
     * @return Port DB
     */
    public static int getPort() {
        try {
            checkInstance();
        } catch (ConfigException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get DB Port", ex);
        }
        return instance.getInt("port");
    }

    /**
     * Return the name of the Database
     *
     * @return Name DB
     */
    public static String getName() {
        try {
            checkInstance();
        } catch (ConfigException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get DB Name", ex);
        }
        return instance.getString("name");
    }

    /**
     * Return the username of the Database
     *
     * @return Username DB
     */
    public static String getUsername() {
        try {
            checkInstance();
        } catch (ConfigException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get DB Username", ex);
        }
        return instance.getString("username");
    }

    /**
     * Return the password of the Database
     *
     * @return Password DB
     */
    public static String getPassword() {
        try {
            checkInstance();
        } catch (ConfigException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get DB Password", ex);
        }
        return instance.getString("password");
    }

    /**
     * Return the ssl boolean of the Database
     *
     * @return true if DB need SSL, false otherwise
     */
    public static boolean getSsl() {
        try {
            checkInstance();
        } catch (ConfigException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get DB Ssl", ex);
        }
        return instance.getBoolean("ssl");
    }
}
