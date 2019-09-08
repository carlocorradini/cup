package it.unitn.disi.wp.cup.persistence.dao;

import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.entity.Person;
import it.unitn.disi.wp.cup.persistence.entity.Province;
import it.unitn.disi.wp.cup.persistence.entity.HealthService;

import java.util.List;

/**
 * DAO interface of {@link Person}
 *
 * @author Carlo Corradini
 */
public interface PersonDAO extends DAO<Person, Long> {
    /**
     * Return the {@link Person person} with the given {@code email}
     *
     * @param email The email of the Person to get
     * @return The {@link Person person} with the given {@code email}
     * @throws DAOException If an error occurred during the information retrieving
     */
    Person getByEmail(String email) throws DAOException;

    /**
     * Update a {@link Person person} from the Database and return if updated
     *
     * @param person The person to update
     * @return true if the {@link Person} has been updated, false otherwise
     * @throws DAOException If an error occurred during the information retrieving
     */
    boolean update(Person person) throws DAOException;

    /**
     * Return the {@link List} of all {@link Person Patients} that live in the same {@link Province}
     * managed by the {@link HealthService Health Service} identified by {@code healthServiceId}
     *
     * @param healthServiceId The {@link HealthService Health Service} id
     * @return The {@link List} of {@link Person Patients}
     * @throws DAOException If an error occurred during the information retrieving
     */
    List<Person> getAllByHealthServiceId(Long healthServiceId) throws DAOException;
}
