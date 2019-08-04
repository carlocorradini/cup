package it.unitn.disi.wp.cup.bean.util;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import it.unitn.disi.wp.cup.util.CalendarUtil;

/**
 * Permits the access to web pages of the Calendar Util
 *
 * @author Carlo Corradini
 * @see CalendarUtil
 */
@Named("calendar")
@RequestScoped
public final class CalendarUtilBean extends CalendarUtil implements Serializable {

    private static final long serialVersionUID = 3267733896872815748L;

    /**
     * Set the Calendar by the Client Locale after all dependencies and
     * resources has been loaded
     */
    @PostConstruct
    public void init() {
        super.setCalendarByLocale(FacesContext.getCurrentInstance().getExternalContext().getRequestLocale());
    }

}
