package it.unitn.disi.wp.cup.persistence.dao.jdbc;

import it.unitn.disi.wp.cup.persistence.dao.RegionDAO;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;
import it.unitn.disi.wp.cup.persistence.dao.factory.jdbc.JDBCDAO;
import it.unitn.disi.wp.cup.persistence.entity.Region;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of {@link Region} interface
 *
 * @author Carlo Corradini
 */
public class JDBCRegionDAO extends JDBCDAO<Region, Long> implements RegionDAO {

    private static final String SQL_GET_COUNT = "SELECT COUNT(*) FROM region";
    private static final String SQL_GET_BY_PRIMARY_KEY = "SELECT * FROM region WHERE id = ? LIMIT 1";
    private static final String SQL_GET_ALL = "SELECT * FROM region";

    /**
     * The default constructor of the class
     *
     * @param connection The Connection to the persistence system
     * @param daoFactory The DAOFactory to get DAOs
     */
    public JDBCRegionDAO(Connection connection, DAOFactory daoFactory) {
        super(connection, daoFactory);
    }

    @Override
    public Region setAndGetDAO(ResultSet rs) throws DAOException {
        Region region;
        if (rs == null) throw new DAOException("ResultSet cannot be null");

        try {
            region = new Region();
            region.setId(rs.getLong("id"));
            region.setName(rs.getString("name"));
        } catch (SQLException ex) {
            throw new DAOException("Impossible to set Region by ResultSet", ex);
        }
        return region;
    }

    /**
     * Return the number of {@link Region regions} stored in the persistence system
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
            throw new DAOException("Impossible to count Region", ex);
        }

        return count;
    }

    /**
     * Return the {@link Region region} with the primary key equals to {@code primaryKey}
     *
     * @param primaryKey The primary key used to obtain the obj instance
     * @return The {@link Region person} with {@code primaryKey}
     * @throws DAOException If an error occurred during the information retrieving
     */
    @Override
    public Region getByPrimaryKey(Long primaryKey) throws DAOException {
        Region region = null;
        if (primaryKey == null)
            throw new DAOException("Primary key is null");

        try (PreparedStatement pStmt = CONNECTION.prepareStatement(SQL_GET_BY_PRIMARY_KEY)) {
            pStmt.setLong(1, primaryKey);
            try (ResultSet rs = pStmt.executeQuery()) {
                if (rs.next()) {
                    region = setAndGetDAO(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get Region for the passed primary key", ex);
        }

        return region;
    }

    /**
     * Return a list of all {@link Region region} in the persistence system
     *
     * @return A list of all saved {@link Region region} in the persistence system
     * @throws DAOException If an error occurred during the information retrieving
     */
    @Override
    public List<Region> getAll() throws DAOException {
        List<Region> regions = new ArrayList<>();

        try (Statement stmt = CONNECTION.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(SQL_GET_ALL)) {
                while (rs.next()) {
                    regions.add(setAndGetDAO(rs));
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the list of Region", ex);
        }

        return regions;
    }
}
