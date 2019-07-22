package it.unitn.disi.wp.cup.persistence.entity;

import java.util.List;

/**
 * Entity Doctor
 *
 * @author Carlo Corradini
 */
public class Doctor {
    private Long id;
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
