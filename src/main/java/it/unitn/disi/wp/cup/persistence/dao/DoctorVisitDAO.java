package it.unitn.disi.wp.cup.persistence.dao;

import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.entity.DoctorVisit;
import org.apache.commons.lang3.tuple.Triple;
import it.unitn.disi.wp.cup.persistence.entity.Person;
import it.unitn.disi.wp.cup.persistence.entity.Doctor;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * DAO interface of {@link DoctorVisit}
 *
 * @author Carlo Corradini
 */
public interface DoctorVisitDAO extends DAO<DoctorVisit, Triple<Long, Long, OffsetDateTime>> {
    /**
     * Return a {@link List list} of {@link DoctorVisit visits} made by the {@link Person person} given the {@code personId}
     *
     * @param personId The {@link Person person} id
     * @return The {@link List list} of {@link DoctorVisit visits} made by the {@link Person person}
     * @throws DAOException If an error occurred during the information retrieving
     */
    List<DoctorVisit> getAllByPersonId(Long personId) throws DAOException;

    /**
     * Return a {@link List list} of {@link DoctorVisit visits} made by the {@link Doctor doctor} given the {@code doctorId}
     *
     * @param doctorId The {@link Doctor doctor} id
     * @return The {@link List list} of {@link DoctorVisit visits} made by the {@link Doctor doctor}
     * @throws DAOException If an error occurred during the information retrieving
     */
    List<DoctorVisit> getAllByDoctorId(Long doctorId) throws DAOException;
}
