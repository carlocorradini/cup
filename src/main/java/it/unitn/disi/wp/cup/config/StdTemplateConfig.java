package it.unitn.disi.wp.cup.config;

import it.unitn.disi.wp.cup.config.exception.ConfigException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Standard Template Configuration
 *
 * @author Carlo Corradini
 */
public final class StdTemplateConfig extends Config {

    private static final String FILE_NAME = "template.properties";
    private static final String CATEGORY = "std";
    private static final Logger LOGGER = Logger.getLogger(StdTemplateConfig.class.getName());
    private static StdTemplateConfig instance;

    private StdTemplateConfig() throws ConfigException {
        super(FILE_NAME, CATEGORY);
    }

    /**
     * Load the StdTemplate Configuration
     *
     * @throws ConfigException If the instance has been already initialized
     */
    public static void load() throws ConfigException {
        if (instance == null) {
            instance = new StdTemplateConfig();
        } else throw new ConfigException("StdTemplateConfig has been already initialized");
    }

    private static void checkInstance() throws ConfigException {
        if (instance == null) throw new ConfigException("StdTemplateConfig has not been initialized");
    }

    /**
     * Return the master layout path
     *
     * @return Master Layout path
     */
    public static String getMasterLayout() {
        try {
            checkInstance();
        } catch (ConfigException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get Master Layout path", ex);
        }
        return instance.getString("masterLayout");
    }

    /**
     * Return the header path
     *
     * @return Header path
     */
    public static String getHeader() {
        try {
            checkInstance();
        } catch (ConfigException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get Header path", ex);
        }
        return instance.getString("header");
    }

    /**
     * Return the sidebar path
     *
     * @return Sidebar path
     */
    public static String getSidebar() {
        try {
            checkInstance();
        } catch (ConfigException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get Sidebar path", ex);
        }
        return instance.getString("sidebar");
    }

    /**
     * Return the content path
     *
     * @return Content path
     */
    public static String getContent() {
        try {
            checkInstance();
        } catch (ConfigException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get Content path", ex);
        }
        return instance.getString("content");
    }

    /**
     * Return the footer path
     *
     * @return Footer path
     */
    public static String getFooter() {
        try {
            checkInstance();
        } catch (ConfigException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get Footer path", ex);
        }
        return instance.getString("footer");
    }

    /**
     * Return the favicon path
     *
     * @return Favicon path
     */
    public static String getFavicon() {
        try {
            checkInstance();
        } catch (ConfigException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get Favicon path", ex);
        }
        return instance.getString("favicon");
    }

    /**
     * Return the preloader path
     *
     * @return Preloader path
     */
    public static String getPreloader() {
        try {
            checkInstance();
        } catch (ConfigException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get Preloader path", ex);
        }
        return instance.getString("preloader");
    }

    /**
     * Return the noscript path
     *
     * @return NoScript path
     */
    public static String getNoScript() {
        try {
            checkInstance();
        } catch (ConfigException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get NoScript path", ex);
        }
        return instance.getString("noscript");
    }

    /**
     * Return the error layout path
     *
     * @return Error Layout path
     */
    public static String getErrorLayout() {
        try {
            checkInstance();
        } catch (ConfigException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get Error Layout path", ex);
        }
        return instance.getString("errorLayout");
    }
}
