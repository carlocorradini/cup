package it.unitn.disi.wp.cup.bean.util;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
/**
 * Permits the access to web pages of the Calendar
 *
 * @author Carlo Corradini
 * @see Calendar
 */
@Named("calendar")
@RequestScoped
public final class CalendarUtilBean implements Serializable {

    private static final long serialVersionUID = 3267733896872815748L;
    private static Calendar calendar = Calendar.getInstance(Locale.getDefault());

    /**
     * Set the Calendar by the Client Locale after all dependencies and
     * resources has been loaded
     */
    @PostConstruct
    public void init() {
        changeLocale(FacesContext.getCurrentInstance().getExternalContext().getRequestLocale());
    }

    /**
     * Change the Calendar to the preferred Locale
     *
     * @param locale Locale region
     */
    private void changeLocale(Locale locale) {
        calendar = Calendar.getInstance(locale);
    }

    /**
     * Return the Calendar year
     *
     * @return Year
     */
    public int getYear() {
        return calendar.get(Calendar.YEAR);
    }

    /**
     * Return the Calendar month
     *
     * @return Month
     */
    public int getMonth() {
        return calendar.get(Calendar.MONTH);
    }

    /**
     * Return the Calendar day of the month
     *
     * @return Day of the Month
     */
    public int getDayOfMonth() {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Return the Calendar day of the week
     *
     * @return Day of the week
     */
    public int getDayOfWeek() {
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * Return the Calendar week of the year
     *
     * @return Week of the Year
     */
    public int getWeekOfYear() {
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * Return the Calendar week of the month
     *
     * @return Week of the month
     */
    public int getWeekOfMonth() {
        return calendar.get(Calendar.WEEK_OF_MONTH);
    }

    /**
     * Return the Calendar hour in 12h format
     *
     * @return hour in 12h format
     */
    public int getHour() {
        return calendar.get(Calendar.HOUR);
    }

    /**
     * Return the Calendar hour in 24h format
     *
     * @return hour in 24h format
     */
    public int getHourOfDay() {
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * Return the Calendar minute
     *
     * @return minute
     */
    public int getMinute() {
        return calendar.get(Calendar.MINUTE);
    }

    /**
     * Return the Calendar second
     *
     * @return second
     */
    public int getSecond() {
        return calendar.get(Calendar.SECOND);
    }

    /**
     * Return the Calendar millisecond
     *
     * @return millisecond
     */
    public int getMilliSecond() {
        return calendar.get(Calendar.MILLISECOND);
    }
}
