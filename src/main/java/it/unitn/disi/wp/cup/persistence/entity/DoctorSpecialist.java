package it.unitn.disi.wp.cup.persistence.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Entity Doctor Specialist
 *
 * @author Carlo Corradini
 */
public class DoctorSpecialist {
    private Long id;
    private LocalDateTime since;

    /**
     * Return the id of the Doctor Specialist
     *
     * @return Doctor Specialist id
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the id of the Doctor Specialist
     *
     * @param id Doctor Specialist id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Return the DateTime registration of the Doctor Specialist
     *
     * @return DateTime registration of the Doctor Specialist
     */
    public LocalDateTime getSince() {
        return since;
    }

    /**
     * Return the {@code since} as a {@link LocalDate date}
     *
     * @return {@code since} as a {@link LocalDate date}
     */
    public LocalDate getSinceDate() {
        if (since != null) {
            return since.toLocalDate();
        }
        return null;
    }

    /**
     * Return the {@code since} as a {@link LocalTime time}
     *
     * @return {@code since} as a {@link LocalTime time}
     */
    public LocalTime getSinceTime() {
        if (since != null) {
            return since.toLocalTime();
        }
        return null;
    }

    /**
     * Set the DateTime registration of Doctor Specialist
     *
     * @param since DateTime registration of the Doctor Specialist
     */
    public void setSince(LocalDateTime since) {
        this.since = since;
    }
}
