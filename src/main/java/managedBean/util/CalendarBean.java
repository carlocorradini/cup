package managedBean.util;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import util.CalendarUtil;

/**
 * Permette l'accesso tramite ManagedBean al Calendario
 *
 * @see CalendarUtil
 * @author Carlo Corradini
 */
@ManagedBean(name = "calendar")
@RequestScoped
public class CalendarBean extends CalendarUtil implements Serializable {

    private static final long serialVersionUID = 3267733896872815748L;

    /**
     * Inizializzazione dopo avere caricato tutte le dipendenze e le risorse
     */
    @PostConstruct
    public void init() {
        super.setCalendarByLocale(FacesContext.getCurrentInstance().getExternalContext().getRequestLocale());
    }

}
