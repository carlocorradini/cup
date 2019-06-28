package it.unitn.disi.wp.cup.persistence.dao.jdbc;

import it.unitn.disi.wp.cup.persistence.dao.ProvinceDAO;
import it.unitn.disi.wp.cup.persistence.dao.RegionDAO;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOFactoryException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;
import it.unitn.disi.wp.cup.persistence.dao.factory.jdbc.JDBCDAO;
import it.unitn.disi.wp.cup.persistence.entity.Province;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of {@link Province} interface
 *
 * @author Carlo Corradini
 */
public class JDBCProvinceDAO extends JDBCDAO<Province, Long> implements ProvinceDAO {

    private static final String SQL_GET_COUNT = "SELECT COUNT(*) FROM province";
    private static final String SQL_GET_BY_PRIMARY_KEY = "SELECT * FROM province WHERE id = ? LIMIT 1";
    private static final String SQL_GET_ALL = "SELECT * FROM province";

    /**
     * The default constructor of the class
     *
     * @param connection The Connection to the persistence system
     * @param daoFactory The DAOFactory to get DAOs
     */
    public JDBCProvinceDAO(Connection connection, DAOFactory daoFactory) {
        super(connection, daoFactory);
    }

    @Override
    public Province setAndGetDAO(ResultSet rs) throws DAOException {
        Province province;
        RegionDAO regionDAO;
        if (rs == null) throw new DAOException("ResultSet cannot be null");

        try {
            regionDAO = DAO_FACTORY.getDAO(RegionDAO.class);
            province = new Province();

            province.setId(rs.getLong("id"));
            province.setNameLong(rs.getString("name_long"));
            province.setNameShort(rs.getString("name_short"));
            province.setRegion(regionDAO.getByPrimaryKey(rs.getLong("region_id")));
        } catch (SQLException | DAOFactoryException ex) {
            throw new DAOException("Impossible to set Province by ResultSet", ex);
        }
        return province;
    }

    /**
     * Return the number of {@link Province provinces} stored in the persistence system
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
            throw new DAOException("Impossible to count Provinces", ex);
        }

        return count;
    }

    /**
     * Return the {@link Province province} with the primary key equals to {@code primaryKey}
     *
     * @param primaryKey The primary key used to obtain the obj instance
     * @return The {@link Province province} with {@code primaryKey}
     * @throws DAOException If an error occurred during the information retrieving
     */
    @Override
    public Province getByPrimaryKey(Long primaryKey) throws DAOException {
        Province province = null;
        if (primaryKey == null)
            throw new DAOException("Primary key is null");

        try (PreparedStatement pStmt = CONNECTION.prepareStatement(SQL_GET_BY_PRIMARY_KEY)) {
            pStmt.setLong(1, primaryKey);
            try (ResultSet rs = pStmt.executeQuery()) {
                if (rs.next()) {
                    province = setAndGetDAO(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get Province for the passed primary key", ex);
        }

        return province;
    }

    /**
     * Return a list of all {@link Province province} in the persistence system
     *
     * @return A list of all saved {@link Province province} in the persistence system
     * @throws DAOException If an error occurred during the information retrieving
     */
    @Override
    public List<Province> getAll() throws DAOException {
        List<Province> provinces = new ArrayList<>();

        try (Statement stmt = CONNECTION.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(SQL_GET_ALL)) {
                while (rs.next()) {
                    provinces.add(setAndGetDAO(rs));
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the list of Province", ex);
        }

        return provinces;
    }

}
