package it.unitn.disi.wp.cup.persistence.dao;

import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOFactoryException;

import java.sql.ResultSet;
import java.util.List;

/**
 * The basic DAO interface that all DAOs must implement
 *
 * @param <ENTITY_CLASS>      The class of the entity to handle
 * @param <PRIMARY_KEY_CLASS> The class of the primary key of the entity the DAO
 *                            handle
 * @author Carlo Corradini
 */
public interface DAO<ENTITY_CLASS, PRIMARY_KEY_CLASS> {

    /**
     * Set And Get the DAO using the ResultSet from the Database
     *
     * @param rs The resultSet to get from
     * @return The new {@code ENTITY_CLASS DAO} initialized using ResultSet
     * @throws DAOException If rs is null or something goes wrong in data retrieving
     */
    public ENTITY_CLASS setAndGetDAO(ResultSet rs) throws DAOException;

    /**
     * Returns the number of records of {@code ENTITY_CLASS} stored on the
     * persistence system of the application
     *
     * @return The number of records present into the storage system
     * @throws DAOException if an error occurred during the information
     *                      retrieving
     */
    public Long getCount() throws DAOException;

    /**
     * Returns the {@code ENTITY_CLASS} instance of the storage system record
     * with the primary key equals to the one passed as parameter
     *
     * @param primaryKey The primary key used to obtain the entity instance
     * @return the {@code ENTITY_CLASS} instance of the storage system record
     * with the primary key equals to the one passed as parameter or
     * {@code null} if no entities with that primary key is present into the
     * storage system
     * @throws DAOException If an error occurred during the information
     *                      retrieving
     */
    public ENTITY_CLASS getByPrimaryKey(PRIMARY_KEY_CLASS primaryKey) throws DAOException;

    /**
     * Returns the list of all the valid entities of type {@code ENTITY_CLASS}
     * stored by the storage system
     *
     * @return The list of all the valid entities of type {@code ENTITY_CLASS}
     * @throws DAOException if an error occurred during the information
     *                      retrieving
     */
    public List<ENTITY_CLASS> getAll() throws DAOException;

    /**
     * If this DAO can interact with it, then returns the DAO of class passed as
     * parameter
     *
     * @param <DAO_CLASS> The class name of the DAO that can interact with this
     *                    DAO
     * @param daoClass    The class of the DAO that can interact with this DAO.
     * @return The instance of the DAO or null if no DAO of the type passed as
     * parameter can interact with this DAO
     * @throws DAOFactoryException If an error occurred
     */
    public <DAO_CLASS extends DAO> DAO_CLASS getDAO(Class<DAO_CLASS> daoClass) throws DAOFactoryException;
}
