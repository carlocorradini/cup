package it.unitn.disi.wp.cup.persistence.entity;

import java.time.LocalDateTime;

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
     * Set the DateTime registration of Doctor Specialist
     *
     * @param since DateTime registration of the Doctor Specialist
     */
    public void setSince(LocalDateTime since) {
        this.since = since;
    }
}
