package it.unitn.disi.wp.cup.persistence.dao;

import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.entity.Doctor;
import it.unitn.disi.wp.cup.persistence.entity.Medicine;
import it.unitn.disi.wp.cup.persistence.entity.Person;
import it.unitn.disi.wp.cup.persistence.entity.PrescriptionMedicine;

import java.util.List;

/**
 * DAO interface of {@link PrescriptionMedicine}
 *
 * @author Carlo Corradini
 */
public interface PrescriptionMedicineDAO extends DAO<PrescriptionMedicine, Long> {
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
     * Add a new {@code PrescriptionMedicine} into the persistence system
     *
     * @param prescriptionMedicine The {@link PrescriptionMedicine} to add
     * @return The primary key of the inserted {@link PrescriptionMedicine}
     * @throws DAOException If an error occurred during the information retrieving
     */
    Long add(PrescriptionMedicine prescriptionMedicine) throws DAOException;

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
}
