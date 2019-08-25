package it.unitn.disi.wp.cup.persistence.dao;

import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.entity.DoctorSpecialist;
import it.unitn.disi.wp.cup.persistence.entity.Exam;

import java.util.List;

/**
 * DAO interface of {@link Exam}
 *
 * @author Carlo Corradini
 */
public interface ExamDAO extends DAO<Exam, Long> {
    /**
     * Return the {@link List} of {@link Exam exams} that the {@link DoctorSpecialist Doctor Specialist} identified by
     * {@code doctorSpecialistId} is qualified
     *
     * @param doctorSpecialistId The {@link DoctorSpecialist} id
     * @return The {@link List} of qualified {@link Exam exams}
     * @throws DAOException If an error occurred during the information retrieving
     */
    List<Exam> getAllQualifiedByDoctorSpecialistId(Long doctorSpecialistId) throws DAOException;
}
