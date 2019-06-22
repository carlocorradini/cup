package it.unitn.disi.wp.cup.persistence.dao;

import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.entity.PersonAvatar;

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
     * Return the {@link List} of all {@link PersonAvatar} of the Person given its {@code personId id}
     * The {@link List} represent the Avatar history of the [@link {@link PersonAvatar}]
     *
     * @param personId The Person ID to get the Avatars
     * @return A {@link List} of all [@link {@link PersonAvatar}] of the [@link {@link PersonAvatar}]
     * @throws DAOException If an error occurred during the information retrieving
     */
    public List<PersonAvatar> getAllByPersonId(Long personId) throws DAOException;
}
