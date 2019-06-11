package it.unitn.disi.wp.cup.bean.config;

import java.io.Serializable;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import it.unitn.disi.wp.cup.config.StdTemplateConfig;
import it.unitn.disi.wp.cup.config.exception.ConfigException;

/**
 * Permits the access to web pages of the Standard Template Configuration
 *
 * @author Carlo Corradini
 * @see StdTemplateConfig
 */
@ManagedBean(name = "stdTemplate")
@ApplicationScoped
public final class StdTemplateConfigBean implements Serializable {

    private static final long serialVersionUID = 7987304702812982518L;

    /**
     * @see StdTemplateConfig#getMasterLayout()
     */
    public String getMasterLayout() throws ConfigException {
        return StdTemplateConfig.getMasterLayout();
    }

    /**
     * @see StdTemplateConfig#getHeader()
     */
    public String getHeader() throws ConfigException {
        return StdTemplateConfig.getHeader();
    }

    /**
     * @see StdTemplateConfig#getContent()
     */
    public String getContent() throws ConfigException {
        return StdTemplateConfig.getContent();
    }

    /**
     * @see StdTemplateConfig#getFooter()
     */
    public String getFooter() throws ConfigException {
        return StdTemplateConfig.getFooter();
    }

    /**
     * @see StdTemplateConfig#getFavicon()
     */
    public String getFavicon() throws ConfigException {
        return StdTemplateConfig.getFavicon();
    }

    /**
     * @see StdTemplateConfig#getPreloader()
     */
    public String getPreloader() throws ConfigException {
        return StdTemplateConfig.getPreloader();
    }

    /**
     * @see StdTemplateConfig#getNoscript()
     */
    public String getNoscript() throws ConfigException {
        return StdTemplateConfig.getNoscript();
    }

    /**
     * @see StdTemplateConfig#getErrorLayout()
     */
    public String getErrorLayout() throws ConfigException {
        return StdTemplateConfig.getErrorLayout();
    }
}
