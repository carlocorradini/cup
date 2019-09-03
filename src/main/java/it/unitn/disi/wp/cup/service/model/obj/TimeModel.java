package it.unitn.disi.wp.cup.service.model.obj;

import it.unitn.disi.wp.cup.service.model.Model;

import java.time.LocalTime;

/**
 * Time Model for managing {@link LocalTime}
 *
 * @author Carlo Corradini
 */
public class TimeModel implements Model {
    private short hour;
    private short minute;
    private short second;

    TimeModel() {
        hour = -1;
        minute = -1;
        second = 0;
    }

    /**
     * Set the hour of the {@link LocalTime Time}
     *
     * @param hour The hour
     */
    public void setHour(short hour) {
        this.hour = hour;
    }

    /**
     * Set the minute of the {@link LocalTime Time}
     *
     * @param minute The hour
     */
    public void setMinute(short minute) {
        this.minute = minute;
    }

    /**
     * Set the second of the {@link LocalTime Time}
     *
     * @param second The second
     */
    public void setSecond(short second) {
        this.second = second;
    }

    /**
     * Return this {@link TimeModel Time Model} as a {@link LocalTime}
     *
     * @return The current {@link TimeModel Time} as {@link LocalTime}
     */
    public LocalTime toLocalTime() {
        return LocalTime.of(hour, minute, second);
    }

    @Override
    public boolean isValid() {
        return hour != -1 && minute != -1;
    }
}
