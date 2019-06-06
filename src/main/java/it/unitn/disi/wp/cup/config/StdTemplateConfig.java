package it.unitn.disi.wp.cup.config;

import it.unitn.disi.wp.cup.config.exception.ConfigException;

/**
 * Standard Template Configuration
 *
 * @author Carlo Corradini
 */
public final class StdTemplateConfig extends Config {

    private static final String FILE_NAME = "template.properties";
    private static final String CATEGORY = "std";
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
     * @throws ConfigException If the instance has not been initialized
     */
    public static String getMasterLayout() throws ConfigException {
        checkInstance();
        return instance.getString("masterLayout");
    }

    /**
     * Return the header path
     *
     * @return Header path
     * @throws ConfigException If the instance has not been initialized
     */
    public static String getHeader() throws ConfigException {
        checkInstance();
        return instance.getString("header");
    }

    /**
     * Return the content path
     *
     * @return Content path
     * @throws ConfigException If the instance has not been initialized
     */
    public static String getContent() throws ConfigException {
        checkInstance();
        return instance.getString("content");
    }

    /**
     * Return the footer path
     *
     * @return Footer path
     * @throws ConfigException If the instance has not been initialized
     */
    public static String getFooter() throws ConfigException {
        checkInstance();
        return instance.getString("footer");
    }

    /**
     * Return the favicon path
     *
     * @return Favicon path
     * @throws ConfigException If the instance has not been initialized
     */
    public static String getFavicon() throws ConfigException {
        checkInstance();
        return instance.getString("favicon");
    }

    /**
     * Return the preloader path
     *
     * @return Preloader path
     * @throws ConfigException If the instance has not been initialized
     */
    public static String getPreloader() throws ConfigException {
        checkInstance();
        return instance.getString("preloader");
    }

    /**
     * Return the noscript path
     *
     * @return Noscript path
     * @throws ConfigException If the instance has not been initialized
     */
    public static String getNoscript() throws ConfigException {
        checkInstance();
        return instance.getString("noscript");
    }

    /**
     * Return the error layout path
     *
     * @return Error Layout path
     * @throws ConfigException If the instance has not been initialized
     */
    public static String getErrorLayout() throws ConfigException {
        checkInstance();
        return instance.getString("errorLayout");
    }
}
