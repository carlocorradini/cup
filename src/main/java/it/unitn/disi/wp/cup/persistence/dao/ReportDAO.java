package it.unitn.disi.wp.cup.persistence.dao;

import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.entity.Report;

/**
 * DAO interface of {@link Report}
 *
 * @author Carlo Corradini
 */
public interface ReportDAO extends DAO<Report, Long> {
    /**
     * Add a new {@code Report} into the persistence system
     *
     * @param report The {@link Report report} to add
     * @return The primary key of the inserted {@link Report}
     * @throws DAOException If an error occurred during the information retrieving
     */
    Long add(Report report) throws DAOException;
}
