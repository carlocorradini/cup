package it.unitn.disi.wp.cup.bean.config;

import java.io.Serializable;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import it.unitn.disi.wp.cup.config.StdTemplateConfig;

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
    public String getMasterLayout() {
        return StdTemplateConfig.getMasterLayout();
    }

    /**
     * @see StdTemplateConfig#getHeader()
     */
    public String getHeader() {
        return StdTemplateConfig.getHeader();
    }

    /**
     * @see StdTemplateConfig#getSidebar()
     */
    public String getSidebar() {
        return StdTemplateConfig.getSidebar();
    }

    /**
     * @see StdTemplateConfig#getContent()
     */
    public String getContent() {
        return StdTemplateConfig.getContent();
    }

    /**
     * @see StdTemplateConfig#getFooter()
     */
    public String getFooter() {
        return StdTemplateConfig.getFooter();
    }

    /**
     * @see StdTemplateConfig#getFavicon()
     */
    public String getFavicon() {
        return StdTemplateConfig.getFavicon();
    }

    /**
     * @see StdTemplateConfig#getPreloader()
     */
    public String getPreloader() {
        return StdTemplateConfig.getPreloader();
    }

    /**
     * @see StdTemplateConfig#getNoScript()
     */
    public String getNoScript() {
        return StdTemplateConfig.getNoScript();
    }

    /**
     * @see StdTemplateConfig#getErrorLayout()
     */
    public String getErrorLayout() {
        return StdTemplateConfig.getErrorLayout();
    }
}
