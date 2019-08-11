package it.unitn.disi.wp.cup.persistence.dao;

import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.entity.PrescriptionExam;
import it.unitn.disi.wp.cup.persistence.entity.Person;
import it.unitn.disi.wp.cup.persistence.entity.Exam;
import it.unitn.disi.wp.cup.persistence.entity.Doctor;

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
    List<PrescriptionExam> getAllByPersonId(Long personId) throws DAOException;

    /**
     * Add a new {@code prescriptionExam} into the persistence system
     *
     * @param prescriptionExam The {@link PrescriptionExam} to add
     * @return The primary key of the inserted {@link PrescriptionExam}
     * @throws DAOException If an error occurred during the information retrieving
     */
    Long add(PrescriptionExam prescriptionExam) throws DAOException;

    /**
     * Return the Number of {@link Exam exams} prescribed for a {@link Person person} given its {@code personId id}
     *
     * @return The number of {@link Exam exams} prescribed for a {@link Person person}
     * @throws DAOException If an error occurred during the information retrieving
     */
    Long getCountByPersonId(Long personId) throws DAOException;

    /**
     * Return the Number of {@link Exam exams} prescribed by a {@link Doctor doctor} given its {@code doctorId id}
     *
     * @return The number of {@link Exam exams} prescribed by a {@link Doctor doctor}
     * @throws DAOException If an error occurred during the information retrieving
     */
    Long getCountByDoctorId(Long doctorId) throws DAOException;
}
