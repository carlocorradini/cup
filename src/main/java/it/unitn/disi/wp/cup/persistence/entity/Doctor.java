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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Person> getPatients() {
        return patients;
    }

    public void setPatients(List<Person> patients) {
        this.patients = patients;
    }
}
