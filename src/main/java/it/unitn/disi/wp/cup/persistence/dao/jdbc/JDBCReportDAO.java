package it.unitn.disi.wp.cup.persistence.dao.jdbc;

import it.unitn.disi.wp.cup.persistence.dao.ReportDAO;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;
import it.unitn.disi.wp.cup.persistence.dao.factory.jdbc.JDBCDAO;
import it.unitn.disi.wp.cup.persistence.entity.Report;

import java.sql.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of {@link Report} interface
 *
 * @author Carlo Corradini
 */
public class JDBCReportDAO extends JDBCDAO<Report, Long> implements ReportDAO {
    private static final String SQL_GET_COUNT = "SELECT COUNT(*) FROM report";
    private static final String SQL_GET_BY_PRIMARY_KEY = "SELECT * FROM report WHERE id = ? LIMIT 1";
    private static final String SQL_GET_ALL = "SELECT * FROM report";

    /**
     * The default constructor of the class
     *
     * @param connection The Connection to the persistence system
     * @param daoFactory The DAOFactory to get DAOs
     */
    public JDBCReportDAO(Connection connection, DAOFactory daoFactory) {
        super(connection, daoFactory);
    }

    @Override
    public Report setAndGetDAO(ResultSet rs) throws DAOException {
        Report report;
        if (rs == null) throw new DAOException("ResultSet cannot be null");

        try {
            report = new Report();

            report.setId(rs.getLong("id"));
            report.setDate(rs.getObject("report_date", OffsetDateTime.class));
            report.setContent(rs.getString("content"));
        } catch (SQLException ex) {
            throw new DAOException("Impossible to set Report by ResultSet", ex);
        }
        return report;
    }

    /**
     * Return the number of {@link Report reports} stored in the persistence system
     *
     * @return The number of records present in the persistence system
     * @throws DAOException If an error occurred during the information retrieving
     */
    @Override
    public Long getCount() throws DAOException {
        long count = 0L;

        try (Statement stmt = CONNECTION.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(SQL_GET_COUNT)) {
                if (rs.next()) {
                    count = rs.getLong(1);
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to count Report", ex);
        }

        return count;
    }

    /**
     * Return the {@link Report report} with the primary key equals to {@code primaryKey}
     *
     * @param primaryKey The primary key used to obtain the obj instance
     * @return The {@link Report report} with {@code primaryKey}
     * @throws DAOException If an error occurred during the information retrieving
     */
    @Override
    public Report getByPrimaryKey(Long primaryKey) throws DAOException {
        Report report = null;
        if (primaryKey == null)
            throw new DAOException("Primary key is null");

        try (PreparedStatement pStmt = CONNECTION.prepareStatement(SQL_GET_BY_PRIMARY_KEY)) {
            pStmt.setLong(1, primaryKey);
            try (ResultSet rs = pStmt.executeQuery()) {
                if (rs.next()) {
                    report = setAndGetDAO(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get Report for the passed primary key", ex);
        }

        return report;
    }

    /**
     * Return a list of all {@link Report report} in the persistence system
     *
     * @return A list of all saved {@link Report report} in the persistence system
     * @throws DAOException If an error occurred during the information retrieving
     */
    @Override
    public List<Report> getAll() throws DAOException {
        List<Report> reports = new ArrayList<>();

        try (Statement stmt = CONNECTION.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(SQL_GET_ALL)) {
                while (rs.next()) {
                    reports.add(setAndGetDAO(rs));
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the list of Report", ex);
        }

        return reports;
    }
}
