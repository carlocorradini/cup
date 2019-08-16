package it.unitn.disi.wp.cup.persistence.entity;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Entity Doctor
 *
 * @author Carlo Corradini
 */
public class Doctor {
    private Long id;
    private LocalDateTime since;
    private List<Person> patients;

    @Override
    public boolean equals(Object obj) {
        Doctor doctor;
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        doctor = (Doctor) obj;
        return id.equals(doctor.id);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        return prime + ((id == null) ? 0 : id.hashCode());
    }

    /**
     * Return the id of the Doctor
     *
     * @return Doctor id
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the id of the Doctor
     *
     * @param id Doctor id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Return the DateTime registration of the Doctor
     *
     * @return DateTime registration of the Doctor
     */
    public LocalDateTime getSince() {
        return since;
    }

    /**
     * Set the DateTime registration of Doctor
     *
     * @param since DateTime registration of the Doctor
     */
    public void setSince(LocalDateTime since) {
        this.since = since;
    }

    /**
     * Return the {@link List list} of patients
     *
     * @return Doctor patients
     */
    public List<Person> getPatients() {
        return patients;
    }

    /**
     * Set the Doctor patients {@link List list}
     *
     * @param patients The {@link List list} of patients
     */
    public void setPatients(List<Person> patients) {
        this.patients = patients;
    }
}
