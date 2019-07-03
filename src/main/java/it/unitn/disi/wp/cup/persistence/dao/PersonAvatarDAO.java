package it.unitn.disi.wp.cup.persistence.dao;

import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.entity.PersonAvatar;
import it.unitn.disi.wp.cup.persistence.entity.Person;

import java.util.List;

/**
 * DAO interface of {@link PersonAvatar}
 *
 * @author Carlo Corradini
 */
public interface PersonAvatarDAO extends DAO<PersonAvatar, Long> {

    /**
     * Insert a new {@code PersonAvatar} into the persistence system
     *
     * @param personAvatar The {@link PersonAvatar} to insert
     * @return The primary key of the inserted Person Avatar
     * @throws DAOException If an error occurred during the information retrieving
     */
    public Long insert(PersonAvatar personAvatar) throws DAOException;

    /**
     * Return the last {@link PersonAvatar} of the {@link Person} with {@code personId id}
     *
     * @param personId The {@link Person person} {@code personId id} to get the last {@link PersonAvatar avatar}
     * @return The last used {@link PersonAvatar avatar}, null if the {@link Person person} has no avatar
     * @throws DAOException If an error occurred during the information retrieving
     */
    public PersonAvatar getCurrentByPersonId(Long personId) throws DAOException;

    /**
     * Return the {@link List} of all {@link PersonAvatar} of the Person given its {@code personId id}
     * The {@link List} represent the Avatar history of the [@link {@link PersonAvatar}]
     *
     * @param personId The Person ID to get the Avatars
     * @return A {@link List} of all [@link {@link PersonAvatar}] of the [@link {@link PersonAvatar}]
     * @throws DAOException If an error occurred during the information retrieving
     */
    public List<PersonAvatar> getAllByPersonId(Long personId) throws DAOException;
}
