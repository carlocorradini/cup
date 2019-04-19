package util;

import java.util.Calendar;
import java.util.Locale;

/**
 * Classe di comodità gestione Calendario in base alla Locale regione
 *
 * @author Carlo Corradini
 */
public class CalendarUtil {

    private Calendar calendar;

    /**
     * Inizializza il calendario in base alla regione
     *
     * @param locale Regione locale
     */
    public CalendarUtil(Locale locale) {
        calendar = Calendar.getInstance(locale);
    }

    /**
     * Inizializza il calendario con la regione di default
     */
    public CalendarUtil() {
        this(Locale.getDefault());
    }

    /**
     * Permette di cambiare il calendario in base alla regione desiderata
     *
     * @param locale Regione locale desiderata
     */
    public void setCalendarByLocale(Locale locale) {
        calendar = Calendar.getInstance(locale);
    }

    /**
     * Ritorna l'anno in base alla regione
     *
     * @return Anno
     */
    public int getYear() {
        return calendar.get(Calendar.YEAR);
    }

    /**
     * Ritorna il mese in base alla regione
     *
     * @return Mese
     */
    public int getMonth() {
        return calendar.get(Calendar.MONTH);
    }

    /**
     * Ritorna il giorno del mese in base alla regione
     *
     * @return Giorno del mese
     */
    public int getDayOfMonth() {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Ritorna il giorno della settimana in base alla regione
     *
     * @return Giorno della settimana
     */
    public int getDayOfWeek() {
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * Ritorna il numero della settimana dell'anno in base alla regione
     *
     * @return Numero della settimana annuale
     */
    public int getWeekOfYear() {
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * Ritorna il numero della settimana del mese in base alla regione
     *
     * @return Numero settimana mensile
     */
    public int getWeekOfMonth() {
        return calendar.get(Calendar.WEEK_OF_MONTH);
    }

    /**
     * Ritorna l'ora in formato 12h in base alla regione
     *
     * @return Ora 12h
     */
    public int getHour() {
        return calendar.get(Calendar.HOUR);
    }

    /**
     * Ritorna l'ora in formato 24h in base alla regione
     *
     * @return Ora 24h
     */
    public int getHourOfDay() {
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * Ritorna i minuti in base alla regione
     *
     * @return Minuto
     */
    public int getMinute() {
        return calendar.get(Calendar.MINUTE);
    }

    /**
     * Ritorna i secondi in base alla regione
     *
     * @return Secondo
     */
    public int getSecond() {
        return calendar.get(Calendar.SECOND);
    }

    /**
     * Ritorna i millisecondi in base alla regione
     *
     * @return Millisecondo
     */
    public int getMilliSecond() {
        return calendar.get(Calendar.MILLISECOND);
    }

}
