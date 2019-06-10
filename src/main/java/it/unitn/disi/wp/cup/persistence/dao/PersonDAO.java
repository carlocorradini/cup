package it.unitn.disi.wp.cup.persistence.dao;

import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.entity.Person;

/**
 * DAO interface of {@link Person}
 *
 * @author Carlo Corradini
 */
public interface PersonDAO extends DAO<Person, Integer> {
    /**
     * Return the {@link Person person} with the given {@code email} and {@code password}
     *
     * @param email    The email of the Person to get
     * @param password The password of the Person to get
     * @return The {@link Person person} with the given {@code email} and {@code password}
     * @throws DAOException If an error occured during the information retrieving
     */
    Person getByEmailAndPassword(String email, String password) throws DAOException;
}
