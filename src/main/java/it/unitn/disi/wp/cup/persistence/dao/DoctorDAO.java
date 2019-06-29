package it.unitn.disi.wp.cup.persistence.dao;

import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.entity.Doctor;
import it.unitn.disi.wp.cup.persistence.entity.Person;

import java.util.List;

/**
 * DAO interface of {@link Doctor}
 *
 * @author Carlo Corradini
 */
public interface DoctorDAO extends DAO<Doctor, Long> {

    /**
     * Return a {@link List list} of {@link Person patients} in care given a {@link Doctor doctor}
     *
     * @param doctor The {@link Doctor doctor} to get {@link Person patients} in care
     * @return The [@link {@link List list}] of {@link Person patients}
     * @throws DAOException If an error occurred during the information retrieving
     */
    List<Person> getPatients(Doctor doctor) throws DAOException;

    /**
     * Return the last {@link Doctor doctor} of the Person
     *
     * @param patient The patient to get the {@link Doctor doctor} for
     * @return The {@link Doctor doctor} of the Patient, null otherwise
     * @throws DAOException If an error occurred during the information retrieving
     */
    Doctor getByPatient(Person patient) throws DAOException;
}
