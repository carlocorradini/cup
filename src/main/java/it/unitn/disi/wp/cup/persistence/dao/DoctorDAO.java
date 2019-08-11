package it.unitn.disi.wp.cup.persistence.dao;

import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.entity.Doctor;
import it.unitn.disi.wp.cup.persistence.entity.Person;
import it.unitn.disi.wp.cup.persistence.entity.Province;
import org.apache.commons.lang3.tuple.Pair;

import java.time.LocalDateTime;
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

    /**
     * Return the {@link LocalDateTime date} since the {@link Person patient} has the current {@link Doctor doctor}
     * If the {@link Person patient} never was a patient null will be returned
     *
     * @param patientId The {@link Person patient} id
     * @return The Since {@link LocalDateTime date} the {@link Person patient} is a patient of the current {@link Doctor doctor}
     * @throws DAOException If an error occurred during the information retrieving
     */
    LocalDateTime getSinceByPatientId(Long patientId) throws DAOException;

    /**
     * Return a {@link List list} of {@link Doctor doctors} represented as {@link Person persons} filtered
     * by the living {@link Province province} and {@link Doctor doctor's} history
     *
     * @param patientId The {@link Person person} id
     * @return A {@link List list} of {@link Doctor doctors} as {@link Person person} filtered by {@code provinceId} and {@link Doctor doctor's} history
     * @throws DAOException If an error occurred during the information retrieving
     */
    List<Person> getAllAvailableByPatientId(Long patientId) throws DAOException;

    /**
     * Add a new record representing that {@link Person patient} with {@code patientId} became a patient of {@link Doctor doctor} with {@code doctorId}
     *
     * @param doctorId  The {@link Doctor doctor} id
     * @param patientId The {@link Person patient} id
     * @return A {@link Pair} representing the new record keys: <Doctor Id, Patient Id>
     * @throws DAOException If an error occurred during the information retrieving
     */
    Pair<Long, Long> addPatient(Long doctorId, Long patientId) throws DAOException;

    /**
     * Return a {@link List list} of {@link Doctor doctors} representing the {@link Doctor doctor's} history of the {@link Person patient}
     *
     * @param patientId The {@link Person patient} id
     * @return A {@link List list} of {@link Doctor doctors}
     * @throws DAOException If an error occurred during the information retrieving
     */
    List<Doctor> getHistoryByPatientId(Long patientId) throws DAOException;
}
