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
    private List<DoctorVisit> doctorVisits;

    /**
     * Return the id of the Doctor
     *
     * @return Doctor id
     */
    public Long getId() {
        return id;
    }

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

    /**
     * Return the {@link List list} of the visits
     *
     * @return Doctor visits
     */
    public List<DoctorVisit> getDoctorVisits() {
        return doctorVisits;
    }

    /**
     * Set the Doctor visits {@link List list}
     *
     * @param doctorVisits The {@link List list} of visits
     */
    public void setDoctorVisits(List<DoctorVisit> doctorVisits) {
        this.doctorVisits = doctorVisits;
    }
}
