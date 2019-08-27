package it.unitn.disi.wp.cup.persistence.dao;

import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.entity.PrescriptionExam;
import it.unitn.disi.wp.cup.persistence.entity.Person;
import it.unitn.disi.wp.cup.persistence.entity.Exam;
import it.unitn.disi.wp.cup.persistence.entity.Doctor;
import it.unitn.disi.wp.cup.persistence.entity.DoctorSpecialist;
import it.unitn.disi.wp.cup.persistence.entity.HealthService;
import it.unitn.disi.wp.cup.persistence.entity.Report;

import java.util.List;

/**
 * DAO interface of {@link PrescriptionExam}
 *
 * @author Carlo Corradini
 */
public interface PrescriptionExamDAO extends DAO<PrescriptionExam, Long> {

    /**
     * Add a new {@code prescriptionExam} into the persistence system
     *
     * @param prescriptionExam The {@link PrescriptionExam} to add
     * @return The primary key of the inserted {@link PrescriptionExam}
     * @throws DAOException If an error occurred during the information retrieving
     */
    Long add(PrescriptionExam prescriptionExam) throws DAOException;

    /**
     * Update a {@link PrescriptionExam prescriptionExam} from the Database and return if updated
     *
     * @param prescriptionExam The prescriptionExam to update
     * @return true if the {@link PrescriptionExam} has been updated, false otherwise
     * @throws DAOException If an error occurred during the information retrieving
     */
    boolean update(PrescriptionExam prescriptionExam) throws DAOException;

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
     * Return a {@link List list} of all {@link PrescriptionExam Prescription Exam} that has been assigned to the
     * {@link DoctorSpecialist} identified by {@code doctorSpecialistId}
     *
     * @param doctorSpecialistId The {@link DoctorSpecialist Doctor Specialist} id
     * @return A {@link List} of all assigned {@link PrescriptionExam Prescription Exam}
     * @throws DAOException If an error occurred during the information retrieving
     */
    List<PrescriptionExam> getAllByDoctorSpecialistId(Long doctorSpecialistId) throws DAOException;

    /**
     * Return a {@link List list} of all {@link PrescriptionExam Prescription Exam} that has been assigned to the
     * {@link HealthService} identified by {@code healthServiceId}
     *
     * @param healthServiceId The {@link HealthService Health Service} id
     * @return A {@link List} of all assigned {@link PrescriptionExam Prescription Exam}
     * @throws DAOException If an error occurred during the information retrieving
     */
    List<PrescriptionExam> getAllByHealthServiceId(Long healthServiceId) throws DAOException;

    /**
     * Return the {@link List} of all {@link PrescriptionExam} with a {@link Report report} that has not been read
     * by the {@link Person person}
     *
     * @param personId The {@link Person person} id
     * @return The {@link List} of {@link PrescriptionExam} with a {@link Report report} that has not been read
     * @throws DAOException If an error occurred during the information retrieving
     */
    List<PrescriptionExam> getAllNotReadByPersonId(Long personId) throws DAOException;

    /**
     * Return the {@link List} of all {@link PrescriptionExam} that has been assigned to the {@link DoctorSpecialist}
     * identified by {@code doctorSpecialistId} but that has not been performed
     *
     * @param doctorSpecialistId The {@link DoctorSpecialist} id
     * @return The {@link List} of {@link PrescriptionExam} assigned but not performed
     * @throws DAOException If an error occurred during the information retrieving
     */
    List<PrescriptionExam> getAllToDoByDoctorSpecialistId(Long doctorSpecialistId) throws DAOException;

    /**
     * Return the {@link List} of all {@link PrescriptionExam} that has been assigned to the {@link HealthService}
     * identified by {@code healthServiceId} but that has not been performed
     *
     * @param healthServiceId The {@link HealthService} id
     * @return The {@link List} of {@link PrescriptionExam} assigned but not performed
     * @throws DAOException If an error occurred during the information retrieving
     */
    List<PrescriptionExam> getAllToDoByHealthServiceId(Long healthServiceId) throws DAOException;

    /**
     * Return the Number of {@link Exam exams} prescribed for a {@link Person person} given its {@code personId id}
     *
     * @param personId The {@link Person} id
     * @return The number of {@link Exam exams} prescribed for a {@link Person person}
     * @throws DAOException If an error occurred during the information retrieving
     */
    Long getCountByPersonId(Long personId) throws DAOException;

    /**
     * Return the Number of {@link Exam exams} prescribed by a {@link Doctor doctor} given its {@code doctorId id}
     *
     * @param doctorId The {@link Doctor} id
     * @return The number of {@link Exam exams} prescribed by a {@link Doctor doctor}
     * @throws DAOException If an error occurred during the information retrieving
     */
    Long getCountByDoctorId(Long doctorId) throws DAOException;

    /**
     * Return the Number of {@link Exam exams} assigned to a {@link DoctorSpecialist doctor specialist}
     * given its {@code doctorSpecialistId id}
     *
     * @param doctorSpecialistId The {@link DoctorSpecialist} id
     * @return The number of {@link Exam exams} assigned to a {@link DoctorSpecialist doctor specialist}
     * @throws DAOException If an error occurred during the information retrieving
     */
    Long getCountByDoctorSpecialistId(Long doctorSpecialistId) throws DAOException;

    /**
     * Return the Number of {@link Exam exams} assigned to a {@link HealthService Health Service}
     * given its {@code doctorSpecialistId id}
     *
     * @param healthServiceId The {@link HealthService} id
     * @return The number of {@link Exam exams} assigned to a {@link HealthService Health Service}
     * @throws DAOException If an error occurred during the information retrieving
     */
    Long getCountByHealthServiceId(Long healthServiceId) throws DAOException;

    /**
     * Return the Number of {@link PrescriptionExam} with a {@link Report report} that has not been read
     * by the {@link Person person}
     *
     * @param personId The {@link Person person} id
     * @return The number of {@link PrescriptionExam exams} with a {@link Report report} that has not been read
     * @throws DAOException If an error occurred during the information retrieving
     */
    Long getCountNotReadByPersonId(Long personId) throws DAOException;

    /**
     * Return the Number of {@link PrescriptionExam} that has not been performed, assigned to a {@link DoctorSpecialist Doctor Specialist}
     * identified by {@code doctorSpecialistId}
     *
     * @param doctorSpecialistId The {@link DoctorSpecialist} id
     * @return The Number of {@link PrescriptionExam} not performed
     * @throws DAOException If an error occurred during the information retrieving
     */
    Long getCountToDoByDoctorSpecialistId(Long doctorSpecialistId) throws DAOException;

    /**
     * Return the Number of {@link PrescriptionExam} that has not been performed, assigned to a {@link HealthService Health Service}
     * identified by {@code healthServiceId}
     *
     * @param healthServiceId The {@link HealthService} id
     * @return The Number of {@link PrescriptionExam} not performed
     * @throws DAOException If an error occurred during the information retrieving
     */
    Long getCountToDoByHealthServiceId(Long healthServiceId) throws DAOException;
}
