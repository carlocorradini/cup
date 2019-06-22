package it.unitn.disi.wp.cup.persistence.dao;

import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.entity.Person;

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
     * Update and return a Person from the Database
     *
     * @param person The person to update
     * @return true if the Person has been updated, false otherwise
     * @throws DAOException If an error occurred during the information retrieving
     */
    boolean update(Person person) throws DAOException;
}
