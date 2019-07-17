package it.unitn.disi.wp.cup.i18n;

import org.apache.commons.collections.IteratorUtils;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 * Internalization i18n
 *
 * @author Carlo Corradini
 */
@ManagedBean(name = "i18nBean")
@SessionScoped
public final class I18nBean implements Serializable {

    private static final long serialVersionUID = -6265955168682758574L;
    private Locale locale;

    /**
     * Initialize the Class after all dependencies and resources has been loaded
     */
    @PostConstruct
    public void init() {
        locale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
    }

    /**
     * Return the Locale that represent a specific geographical, political or
     * cultural region
     *
     * @return The Locale of the current Session
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * Return the language code of the current Session
     *
     * @return The relative language code
     */
    public String getLanguage() {
        return locale.getLanguage();
    }

    /**
     * Change the current session language given the language code
     *
     * @param language Language code
     */
    public void setLanguage(String language) {
        locale = new Locale(language);
        FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale(language));
    }

    /**
     * Return the default Locale of the Application
     *
     * @return Default Locale
     */
    public Locale getDefaultLocale() {
        return FacesContext.getCurrentInstance().getApplication().getDefaultLocale();
    }

    /**
     * Return the list of supported languages
     *
     * @return The list of languages
     */
    public List<Locale> getSupportedLocale() {
        Iterator<Locale> iterator = FacesContext.getCurrentInstance().getApplication().getSupportedLocales();
        List<Locale> list = IteratorUtils.toList(iterator);
        list.add(getDefaultLocale());
        return list;
    }

}
