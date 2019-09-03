package it.unitn.disi.wp.cup.service.model.obj;

import it.unitn.disi.wp.cup.service.model.Model;

import java.time.LocalDate;

/**
 * Date Model for managing {@link LocalDate}
 *
 * @author Carlo Corradini
 */
public class DateModel implements Model {
    private short day;
    private short month;
    private short year;

    DateModel() {
        day = -1;
        month = -1;
        year = -1;
    }

    /**
     * Set the day of the {@link LocalDate Date}
     *
     * @param day The day
     */
    public void setDay(short day) {
        this.day = day;
    }

    /**
     * Set the month of the {@link LocalDate Date}
     *
     * @param month The month
     */
    public void setMonth(short month) {
        this.month = month;
    }

    /**
     * Set the year of the {@link LocalDate Date}
     *
     * @param year The year
     */
    public void setYear(short year) {
        this.year = year;
    }

    /**
     * Return this {@link DateModel Date Model} as a {@link LocalDate}
     *
     * @return The current {@link DateModel Date} as {@link LocalDate}
     */
    public LocalDate toLocalDate() {
        return LocalDate.of(year, month, day);
    }

    @Override
    public boolean isValid() {
        return day != -1 && month != -1 && year != -1;
    }
}
