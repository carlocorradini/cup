package it.unitn.disi.wp.cup.config;

import it.unitn.disi.wp.cup.config.exception.ConfigException;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Web App Configuration
 *
 * @author Carlo Corradini
 */
public final class AppConfig extends Config {
    private static final String FILE_NAME = "app.properties";
    private static final String CATEGORY = "app";
    private static final Logger LOGGER = Logger.getLogger(AppConfig.class.getName());
    private static AppConfig instance;

    private AppConfig() throws ConfigException {
        super(FILE_NAME, CATEGORY);
    }

    /**
     * Load the App Configuration
     *
     * @throws ConfigException If the instance has been already initialized
     */
    public static void load() throws ConfigException {
        if (instance == null) {
            instance = new AppConfig();
        } else throw new ConfigException("AppConfig has been already initialized");
    }

    private static void checkInstance() throws ConfigException {
        if (instance == null) throw new ConfigException("AppConfig has not been initialized");
    }

    /**
     * Return the name of the App
     *
     * @return The App name
     */
    public static String getName() {
        try {
            checkInstance();
        } catch (ConfigException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get App Name", ex);
        }
        return instance.getString("name");
    }

    /**
     * Return the domain of the App
     *
     * @return The App domain
     */
    public static String getDomain() {
        try {
            checkInstance();
        } catch (ConfigException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get App Domain", ex);
        }
        return instance.getString("domain");
    }

    /**
     * Return the hostname of the App
     *
     * @return The App hostname
     */
    public static String getHostname() {
        try {
            checkInstance();
        } catch (ConfigException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get App Hostname", ex);
        }
        return instance.getString("hostname");
    }

    /**
     * Return the author name of the App
     *
     * @return The App author Name
     */
    public static String getAuthorName() {
        try {
            checkInstance();
        } catch (ConfigException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get App Author Name", ex);
        }
        return instance.getString("author.name");
    }

    /**
     * Return the author List of the App
     *
     * @return The app author's List
     */
    public static List<String> getAuthorList() {
        try {
            checkInstance();
        } catch (ConfigException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get App Author List", ex);
        }
        return instance.getList("author.list");
    }

    /**
     * Return the color of the App
     *
     * @return The App color
     */
    public static String getColor() {
        try {
            checkInstance();
        } catch (ConfigException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get App Color", ex);
        }
        return instance.getString("color");
    }

    /**
     * Return the Since Day of the App
     *
     * @return The App Since Day
     */
    public static int getSinceDay() {
        try {
            checkInstance();
        } catch (ConfigException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get App Since Day", ex);
        }
        return instance.getInt("since.day");
    }

    /**
     * Return the Since Month of the App
     *
     * @return The App Since Month
     */
    public static int getSinceMonth() {
        try {
            checkInstance();
        } catch (ConfigException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get App Since Month", ex);
        }
        return instance.getInt("since.month");
    }

    /**
     * Return the Since Year of the App
     *
     * @return The App Since Year
     */
    public static int getSinceYear() {
        try {
            checkInstance();
        } catch (ConfigException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get App Since Year", ex);
        }
        return instance.getInt("since.year");
    }

    /**
     * Return the Info Email of the App
     *
     * @return The App Info Email
     */
    public static String getInfoEmail() {
        try {
            checkInstance();
        } catch (ConfigException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get App Info Email", ex);
        }
        return instance.getString("info.email");
    }

    /**
     * Return the Info Phone of the App
     *
     * @return The App Info Phone
     */
    public static String getInfoPhone() {
        try {
            checkInstance();
        } catch (ConfigException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get App Info Phone", ex);
        }
        return instance.getString("info.phone");
    }

    /**
     * Return the Info Place Address of the App
     *
     * @return The App Info Place Address
     */
    public static String getInfoPlaceAddress() {
        try {
            checkInstance();
        } catch (ConfigException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get App Info Place Address", ex);
        }
        return instance.getString("info.place.address");
    }

    /**
     * Return the Info Place Number of the App
     *
     * @return The App Info Place Number
     */
    public static String getInfoPlaceNumber() {
        try {
            checkInstance();
        } catch (ConfigException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get App Info Place Number", ex);
        }
        return instance.getString("info.place.number");
    }

    /**
     * Return the Info Place Cap of the App
     *
     * @return The App Info Place Cap
     */
    public static String getInfoPlaceCap() {
        try {
            checkInstance();
        } catch (ConfigException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get App Info Place Cap", ex);
        }
        return instance.getString("info.place.cap");
    }

    /**
     * Return the Info Place City of the App
     *
     * @return The App Info Place City
     */
    public static String getInfoPlaceCity() {
        try {
            checkInstance();
        } catch (ConfigException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get App Info Place City", ex);
        }
        return instance.getString("info.place.city");
    }

    /**
     * Return the Config Avatar Max File Size
     *
     * @return The App Config Avatar Max File Size
     */
    public static int getConfigAvatarMaxFileSize() {
        try {
            checkInstance();
        } catch (ConfigException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get Config Avatar MaxFileSize", ex);
        }
        return instance.getInt("config.avatar.maxFileSize");
    }

    /**
     * Return the Config Avatar Path
     *
     * @return The App Config Avatar Path
     */
    public static String getConfigAvatarPath() {
        try {
            checkInstance();
        } catch (ConfigException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get Config Avatar Path", ex);
        }
        return instance.getString("config.avatar.path");
    }

    /**
     * Return the Config Avatar Backup Path
     *
     * @return The App Config Avatar Backup Path
     */
    public static String getConfigAvatarBackupPath() {
        try {
            checkInstance();
        } catch (ConfigException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get Config Avatar Backup Path", ex);
        }
        return instance.getString("config.avatar.pathBackup");
    }

    /**
     * Return the Config Avatar Extension
     *
     * @return The App Config Avatar Extension
     */
    public static String getConfigAvatarExtension() {
        try {
            checkInstance();
        } catch (ConfigException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get Config Avatar Extension", ex);
        }
        return instance.getString("config.avatar.extension");
    }

    /**
     * Return the Config Avatar Resize Size
     *
     * @return The App Config Avatar Resize Size
     */
    public static int getConfigAvatarResizeSize() {
        try {
            checkInstance();
        } catch (ConfigException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get Config Avatar Resize Size", ex);
        }
        return instance.getInt("config.avatar.resizeSize");
    }
}
