package it.unitn.disi.wp.cup.persistence.dao;

import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.entity.PrescriptionExam;
import it.unitn.disi.wp.cup.persistence.entity.Person;

import java.util.List;

/**
 * DAO interface of {@link PrescriptionExam}
 *
 * @author Carlo Corradini
 */
public interface PrescriptionExamDAO extends DAO<PrescriptionExam, Long> {
    /**
     * Return the {@link List} of all {@link PrescriptionExam} of the Person given its {@code personId id}
     * The {@link List} represent the exam history of the {@link Person}
     *
     * @param personId The Person ID to get the Exams
     * @return A {@link List} of all {@link PrescriptionExam exams} of the {@link Person}
     * @throws DAOException If an error occurred during the information retrieving
     */
    public List<PrescriptionExam> getAllByPersonId(Long personId) throws DAOException;
}
