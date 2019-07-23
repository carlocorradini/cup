package it.unitn.disi.wp.cup.persistence.dao;

import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
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
    public List<PrescriptionMedicine> getAllByPersonId(Long personId) throws DAOException;
}
