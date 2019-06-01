package it.unitn.disi.wp.cup.config;

/**
 * Standard Template Configuration
 *
 * @author Carlo Corradini
 */
public class StdTemplateConfig extends ConfigManager {

    private static final String FILE_NAME = "template.properties";
    private static final String CATEGORY = "std";

    /**
     * Create a Standard Template Configuration
     */
    public StdTemplateConfig() {
        super(FILE_NAME, CATEGORY);
    }

    /**
     * Return the master layout path
     *
     * @return Master Layout path
     */
    public String getMasterLayout() {
        return super.getString("masterLayout");
    }

    /**
     * Return the header path
     *
     * @return Header path
     */
    public String getHeader() {
        return super.getString("header");
    }

    /**
     * Return the content path
     *
     * @return Content path
     */
    public String getContent() {
        return super.getString("content");
    }

    /**
     * Return the footer path
     *
     * @return Footer path
     */
    public String getFooter() {
        return super.getString("footer");
    }

    /**
     * Return the favicon path
     *
     * @return Favicon path
     */
    public String getFavicon() {
        return super.getString("favicon");
    }

    /**
     * Return the preloader path
     *
     * @return Preloader path
     */
    public String getPreloader() {
        return super.getString("preloader");
    }

    /**
     * Return the noscript path
     *
     * @return Noscript path
     */
    public String getNoscript() {
        return super.getString("noscript");
    }

    /**
     * Return the error layout path
     *
     * @return Error Layout path
     */
    public String getErrorLayout() {
        return super.getString("errorLayout");
    }
}
