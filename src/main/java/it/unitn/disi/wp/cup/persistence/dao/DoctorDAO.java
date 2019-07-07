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
     * @param doctorId The id of the {@link Doctor doctor}
     * @return The {@link List list} of {@link Person patients}
     * @throws DAOException If an error occurred during the information retrieving
     */
    List<Person> getPatientsByDoctorId(Long doctorId) throws DAOException;

    /**
     * Return the last {@link Doctor doctor} of the Person
     *
     * @param patientId The {@link Person patient} id to get the {@link Doctor doctor}
     * @return The {@link Doctor doctor} of the {@link Person patient}, null otherwise
     * @throws DAOException If an error occurred during the information retrieving
     */
    Doctor getDoctorByPatientId(Long patientId) throws DAOException;
}
