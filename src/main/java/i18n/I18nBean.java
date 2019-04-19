package i18n;

import java.io.Serializable;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 * Gestione Internalizzazione in base alla richiesta da parte del Client
 *
 * @author Carlo Corradini
 */
@ManagedBean(name = "i18nBean")
@SessionScoped
public final class I18nBean implements Serializable {

    private static final long serialVersionUID = -6265955168682758574L;
    private Locale locale;

    /**
     * Inizializzazione dopo avere caricato tutte le dipendenze e le risorse
     */
    @PostConstruct
    public void init() {
        locale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
    }

    /**
     * Ritorna un object Locale che rappresenta una specifica regione
     * geografica, politica o culturale
     *
     * @return La regione (Locale) della sessione attuale
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * Ritorna il codice lingua della sessione attuale
     *
     * @return Il Codice lingua relativo
     */
    public String getLanguage() {
        return locale.getLanguage();
    }

    /**
     * Cambia la lingua della sessione attuale in base al codice
     *
     * @param language Codice lingua
     */
    public void setLanguage(String language) {
        locale = new Locale(language);
        FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale(language));
    }

}
