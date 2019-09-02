package it.unitn.disi.wp.cup.persistence.dao;

import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.entity.DoctorSpecialist;
import it.unitn.disi.wp.cup.persistence.entity.Person;
import it.unitn.disi.wp.cup.persistence.entity.Province;
import it.unitn.disi.wp.cup.persistence.entity.Exam;

import java.util.List;

/**
 * DAO interface of {@link DoctorSpecialist}
 *
 * @author Carlo Corradini
 */
public interface DoctorSpecialistDAO extends DAO<DoctorSpecialist, Long> {

    /**
     * Return the {@link List} of {@link DoctorSpecialist Doctor specialists} represented as {@link Person Person}
     * that live in the {@link Province Province} identified by {@code provinceId}
     * and is qualified for the {@link Exam Exam} identified by {@code examId}
     *
     * @param provinceId The {@link Province} id
     * @param examId     The {@link Exam} id
     * @return The {@link List} of qualified {@link DoctorSpecialist Doctor Specialists}
     * @throws DAOException If an error occurred during the information retrieving
     */
    List<Person> getAllQualifiedbyProvinceIdAndExamId(Long provinceId, Long examId) throws DAOException;
}
