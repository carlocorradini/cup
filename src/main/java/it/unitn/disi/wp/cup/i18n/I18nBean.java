package it.unitn.disi.wp.cup.i18n;

import org.apache.commons.lang3.LocaleUtils;

import java.io.Serializable;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 * Internalization i18n
 *
 * @author Carlo Corradini
 */
@Named("i18nBean")
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
     * Return the country of the current language if supported
     *
     * @return Country of the current language
     */
    public String getCountry() {
        String country = "";

        if (locale.getLanguage().equals(Locale.ITALIAN.getLanguage())) {
            country = "italy";
        } else if (locale.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
            country = "america";
        } else if (locale.getLanguage().equals(Locale.GERMAN.getLanguage())) {
            country = "germany";
        } else if (locale.getLanguage().equals(Locale.FRENCH.getLanguage())) {
            country = "france";
        } else if (locale.getLanguage().equals("es")) {
            country = "spain";
        }

        return country;
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

}
