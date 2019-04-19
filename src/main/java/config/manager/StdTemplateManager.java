package config.manager;

/**
 * Manager Standard Template Path
 *
 * @author Carlo Corradini
 */
public class StdTemplateManager extends ConfigManager {

    private static final String FILE_NAME = "template.properties";
    private static final String CATEGORY = "std";

    /**
     * Crea uno Standard Template Manager
     */
    public StdTemplateManager() {
        super(FILE_NAME, CATEGORY);
    }

    /**
     * Ritorna il percorso del master layout
     *
     * @return Master Layout path
     */
    public String getMasterLayout() {
        return super.getString("masterLayout");
    }

    /**
     * Ritorna il percorso all'Header
     *
     * @return Header path
     */
    public String getHeader() {
        return super.getString("header");
    }

    /**
     * Ritorna il percorso al contenuto
     *
     * @return Content path
     */
    public String getContent() {
        return super.getString("content");
    }

    /**
     * Ritorna il percorso al footer
     *
     * @return Footer path
     */
    public String getFooter() {
        return super.getString("footer");
    }

    /**
     * Ritorna il percorso alle favicon
     *
     * @return Favicon path
     */
    public String getFavicon() {
        return super.getString("favicon");
    }

    /**
     * Ritorna il percorso al preloader
     *
     * @return Preloader path
     */
    public String getPreloader() {
        return super.getString("preloader");
    }

    /**
     * Ritorna il percorso a noscript
     *
     * @return Noscript path
     */
    public String getNoscript() {
        return super.getString("noscript");
    }

    /**
     * Ritorna il percorso ad error layout
     *
     * @return Error Layout path
     */
    public String getErrorLayout() {
        return super.getString("errorLayout");
    }
}
