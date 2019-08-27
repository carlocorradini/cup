package it.unitn.disi.wp.cup.persistence.dao.jdbc;

import it.unitn.disi.wp.cup.persistence.dao.HealthServiceDAO;
import it.unitn.disi.wp.cup.persistence.dao.ProvinceDAO;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOFactoryException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;
import it.unitn.disi.wp.cup.persistence.dao.factory.jdbc.JDBCDAO;
import it.unitn.disi.wp.cup.persistence.entity.HealthService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of {@link HealthService} interface
 *
 * @author Carlo Corradini
 */
public class JDBCHealthServiceDAO extends JDBCDAO<HealthService, Long> implements HealthServiceDAO {

    private static final String SQL_GET_COUNT = "SELECT COUNT(*) FROM health_service";
    private static final String SQL_GET_BY_PRIMARY_KEY = "SELECT * FROM health_service WHERE id = ? LIMIT 1";
    private static final String SQL_GET_ALL = "SELECT * FROM health_service";

    /**
     * The default constructor of the class
     *
     * @param connection The Connection to the persistence system
     * @param daoFactory The DAOFactory to get DAOs
     */
    public JDBCHealthServiceDAO(Connection connection, DAOFactory daoFactory) {
        super(connection, daoFactory);
    }

    @Override
    public HealthService setAndGetDAO(ResultSet rs) throws DAOException {
        HealthService healthService;
        ProvinceDAO provinceDAO;
        if (rs == null) throw new DAOException("ResultSet cannot be null");

        try {
            provinceDAO = DAO_FACTORY.getDAO(ProvinceDAO.class);
            healthService = new HealthService();

            healthService.setId(rs.getLong("id"));
            healthService.setPassword(rs.getString("password"));
            healthService.setEmail(rs.getString("email"));
            healthService.setCrest(rs.getString("crest"));
            healthService.setProvince(provinceDAO.getByPrimaryKey(healthService.getId()));
        } catch (SQLException | DAOFactoryException ex) {
            throw new DAOException("Impossible to set Health Service by ResultSet", ex);
        }
        return healthService;
    }

    /**
     * Return the number of {@link HealthService Health Service} stored in the persistence system
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
            throw new DAOException("Impossible to count Health Services", ex);
        }

        return count;
    }

    /**
     * Return the {@link HealthService Health Service} with the primary key equals to {@code primaryKey}
     *
     * @param primaryKey The primary key used to obtain the obj instance
     * @return The {@link HealthService Health Service} with {@code primaryKey}
     * @throws DAOException If an error occurred during the information retrieving
     */
    @Override
    public HealthService getByPrimaryKey(Long primaryKey) throws DAOException {
        HealthService healthService = null;
        if (primaryKey == null)
            throw new DAOException("Primary Key is mandatory", new NullPointerException("Primary Key is null"));

        try (PreparedStatement pStmt = CONNECTION.prepareStatement(SQL_GET_BY_PRIMARY_KEY)) {
            pStmt.setLong(1, primaryKey);
            try (ResultSet rs = pStmt.executeQuery()) {
                if (rs.next()) {
                    healthService = setAndGetDAO(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get Health Service for the passed primary key", ex);
        }

        return healthService;
    }

    /**
     * Return a list of all {@link HealthService Health Service} in the persistence system
     *
     * @return A list of all saved {@link HealthService Health Service} in the persistence system
     * @throws DAOException If an error occurred during the information retrieving
     */
    @Override
    public List<HealthService> getAll() throws DAOException {
        List<HealthService> healthServices = new ArrayList<>();

        try (Statement stmt = CONNECTION.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(SQL_GET_ALL)) {
                while (rs.next()) {
                    healthServices.add(setAndGetDAO(rs));
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the list of Health Services", ex);
        }

        return healthServices;
    }

}
