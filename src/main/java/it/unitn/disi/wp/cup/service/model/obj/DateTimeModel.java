package it.unitn.disi.wp.cup.service.model.obj;

import it.unitn.disi.wp.cup.service.model.Model;

import java.time.LocalDateTime;

/**
 * DateTime Model for managing {@link LocalDateTime}
 *
 * @author Carlo Corradini
 */
public class DateTimeModel implements Model {
    private DateModel date;
    private TimeModel time;

    DateTimeModel() {
        date = null;
        time = null;
    }

    /**
     * Set the {@link DateModel Date} of the {@link LocalDateTime}
     *
     * @param date The Date
     */
    public void setDate(DateModel date) {
        this.date = date;
    }

    /**
     * Set the {@link TimeModel Time} of the {@link LocalDateTime}
     *
     * @param time The Time
     */
    public void setTime(TimeModel time) {
        this.time = time;
    }

    /**
     * Return this {@link DateTimeModel Date Time Model} as a {@link LocalDateTime}
     *
     * @return The current {@link DateTimeModel Date Time} as {@link LocalDateTime}
     */
    public LocalDateTime toLocalDateTime() {
        return LocalDateTime.of(date.toLocalDate(), time.toLocalTime());
    }

    @Override
    public boolean isValid() {
        return date != null && time != null;
    }
}
