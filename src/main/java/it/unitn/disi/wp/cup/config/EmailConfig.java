package it.unitn.disi.wp.cup.config;

import it.unitn.disi.wp.cup.config.exception.ConfigException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Email Configuration
 *
 * @author Carlo Corradini
 */
public final class EmailConfig extends Config {

    private static final String FILE_NAME = "email.properties";
    private static final String CATEGORY = "email";
    private static final Logger LOGGER = Logger.getLogger(EmailConfig.class.getName());
    private static EmailConfig instance;

    private EmailConfig() throws ConfigException {
        super(FILE_NAME, CATEGORY);
    }

    /**
     * Load the Email Configuration
     *
     * @throws ConfigException If the instance has been already initialized
     */
    public static void load() throws ConfigException {
        if (instance == null) {
            instance = new EmailConfig();
        } else throw new ConfigException("EmailConfig has been already initialized");
    }

    private static void checkInstance() throws ConfigException {
        if (instance == null) throw new ConfigException("EmailConfig has not been initialized");
    }

    /**
     * Return the email username
     *
     * @return Email username
     */
    public static String getUsername() {
        try {
            checkInstance();
        } catch (ConfigException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get Email Username", ex);
        }
        return instance.getString("username");
    }

    /**
     * Return the email password
     *
     * @return Email password
     */
    public static String getPassword() {
        try {
            checkInstance();
        } catch (ConfigException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get Email Password", ex);
        }
        return instance.getString("password");
    }

    /**
     * Return the email debug
     *
     * @return Email debug
     */
    public static boolean getDebug() {
        try {
            checkInstance();
        } catch (ConfigException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get Email Debug", ex);
        }
        return instance.getBoolean("debug");
    }

    /**
     * Return the email SMTP host
     *
     * @return Email SMTP host
     */
    public static String getSmtpHost() {
        try {
            checkInstance();
        } catch (ConfigException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get Email SMTP Host", ex);
        }
        return instance.getString("smtp.host");
    }

    /**
     * Return the email SMTP port
     *
     * @return Email SMTP port
     */
    public static int getSmtpPort() {
        try {
            checkInstance();
        } catch (ConfigException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get Email SMTP Port", ex);
        }
        return instance.getInt("smtp.port");
    }

    /**
     * Return the email SMTP auth
     *
     * @return Email SMTP auth
     */
    public static boolean getSmtpAuth() {
        try {
            checkInstance();
        } catch (ConfigException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get Email SMTP Auth", ex);
        }
        return instance.getBoolean("smtp.auth");
    }

    /**
     * Return the email SMTP tls
     *
     * @return Email SMTP tls
     */
    public static boolean getSmtpTls() {
        try {
            checkInstance();
        } catch (ConfigException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get Email SMTP TLS", ex);
        }
        return instance.getBoolean("smtp.tls");
    }
}

