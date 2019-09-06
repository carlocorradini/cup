package it.unitn.disi.wp.cup.persistence.dao;

import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.entity.Doctor;
import it.unitn.disi.wp.cup.persistence.entity.Medicine;
import it.unitn.disi.wp.cup.persistence.entity.Person;
import it.unitn.disi.wp.cup.persistence.entity.PrescriptionMedicine;
import it.unitn.disi.wp.cup.persistence.entity.HealthService;
import it.unitn.disi.wp.cup.persistence.entity.Province;

import java.time.LocalDate;
import java.util.List;
import java.time.LocalDateTime;

/**
 * DAO interface of {@link PrescriptionMedicine}
 *
 * @author Carlo Corradini
 */
public interface PrescriptionMedicineDAO extends DAO<PrescriptionMedicine, Long> {
    /**
     * Add a new {@code PrescriptionMedicine} into the persistence system
     *
     * @param prescriptionMedicine The {@link PrescriptionMedicine} to add
     * @return The primary key of the inserted {@link PrescriptionMedicine}
     * @throws DAOException If an error occurred during the information retrieving
     */
    Long add(PrescriptionMedicine prescriptionMedicine) throws DAOException;

    /**
     * Update a {@link PrescriptionMedicine prescriptionMedicine} from the Database and return if updated
     *
     * @param prescriptionMedicine The prescriptionMedicine to update
     * @return true if the {@link PrescriptionMedicine} has been updated, false otherwise
     * @throws DAOException If an error occurred during the information retrieving
     */
    boolean update(PrescriptionMedicine prescriptionMedicine) throws DAOException;

    /**
     * Return the {@link List} of all {@link PrescriptionMedicine} of the Person given its {@code personId id}
     * The {@link List} represent the exam history of the {@link Person}
     *
     * @param personId The Person ID to get the Medicines
     * @return A {@link List} of all {@link PrescriptionMedicine medicines} of the {@link Person}
     * @throws DAOException If an error occurred during the information retrieving
     */
    List<PrescriptionMedicine> getAllByPersonId(Long personId) throws DAOException;

    /**
     * Return the {@link List list} of all {@link PrescriptionMedicine Presctiption Medicine} that has been created
     * by a {@link Doctor Doctor} for a {@link Person Patient} but has not been provided.
     * {@link LocalDateTime Date and Time provide} is missing.
     * Paid is false.
     *
     * @param healthServiceId The {@link HealthService Health Service} id
     * @return The {@link List List} of all created but not provided {@link PrescriptionMedicine Prescription Medicine}
     * @throws DAOException If an error occurred during the information retrieving
     */
    List<PrescriptionMedicine> getAllToAssignByHealthServiceId(Long healthServiceId) throws DAOException;

    /**
     * Return the {@link List list} of all {@link PrescriptionMedicine Prescription Medicine} that has been made
     * in the {@link Province Province} identified by {@code healthServiceId} in a certain {@link LocalDate Date}
     *
     * @param healthServiceId The {@link HealthService Health Service} id
     * @param date            The {@link LocalDate Date} for filtering
     * @return The {@link List} of {@link PrescriptionMedicine} done in a certain {@link LocalDate Date}
     * @throws DAOException If an error occurred during the information retrieving
     */
    List<PrescriptionMedicine> getAllDoneByHealthServiceIdAndDate(Long healthServiceId, LocalDate date) throws DAOException;

    /**
     * Return the Number of {@link Medicine medicines} prescribed for a {@link Person person} given its {@code personId id}
     *
     * @return The number of {@link Medicine medicines} prescribed for a {@link Person person}
     * @throws DAOException If an error occurred during the information retrieving
     */
    Long getCountByPersonId(Long personId) throws DAOException;

    /**
     * Return the Number of {@link Medicine medicines} prescribed by a {@link Doctor doctor} given its {@code doctorId id}
     *
     * @return The number of {@link Medicine medicines} prescribed by a {@link Doctor doctor}
     * @throws DAOException If an error occurred during the information retrieving
     */
    Long getCountByDoctorId(Long doctorId) throws DAOException;

    /**
     * Return the Number of all {@link PrescriptionMedicine Presctiption Medicine} that has been created
     * by a {@link Doctor Doctor} for a {@link Person Patient} but has not been provided.
     * {@link LocalDateTime Date and Time provide} is missing.
     * Paid is false.
     *
     * @param healthServiceId The {@link HealthService Health Service} id
     * @return The Number of {@link PrescriptionMedicine} that has NOT been provided
     * @throws DAOException If an error occurred during the information retrieving
     */
    Long getCountToAssignByHealthServiceId(Long healthServiceId) throws DAOException;
}
