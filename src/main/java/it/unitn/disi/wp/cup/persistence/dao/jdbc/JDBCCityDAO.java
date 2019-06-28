package it.unitn.disi.wp.cup.persistence.dao.jdbc;

import it.unitn.disi.wp.cup.persistence.dao.CityDAO;
import it.unitn.disi.wp.cup.persistence.dao.ProvinceDAO;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOFactoryException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;
import it.unitn.disi.wp.cup.persistence.dao.factory.jdbc.JDBCDAO;
import it.unitn.disi.wp.cup.persistence.entity.City;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of {@link City} interface
 *
 * @author Carlo Corradini
 */
public class JDBCCityDAO extends JDBCDAO<City, Long> implements CityDAO {

    private static final String SQL_GET_COUNT = "SELECT COUNT(*) FROM city";
    private static final String SQL_GET_BY_PRIMARY_KEY = "SELECT * FROM city WHERE id = ? LIMIT 1";
    private static final String SQL_GET_ALL = "SELECT * FROM city";

    /**
     * The default constructor of the class
     *
     * @param connection The Connection to the persistence system
     * @param daoFactory The DAOFactory to get DAOs
     */
    public JDBCCityDAO(Connection connection, DAOFactory daoFactory) {
        super(connection, daoFactory);
    }

    @Override
    public City setAndGetDAO(ResultSet rs) throws DAOException {
        City city;
        ProvinceDAO provinceDAO;
        if (rs == null) throw new DAOException("ResultSet cannot be null");

        try {
            provinceDAO = DAO_FACTORY.getDAO(ProvinceDAO.class);
            city = new City();

            city.setId(rs.getLong("id"));
            city.setName(rs.getString("name"));
            city.setProvince(provinceDAO.getByPrimaryKey(rs.getLong("province_id")));
        } catch (SQLException | DAOFactoryException ex) {
            throw new DAOException("Impossible to set City by ResultSet", ex);
        }
        return city;
    }

    /**
     * Return the number of {@link City cities} stored in the persistence system
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
            throw new DAOException("Impossible to count City", ex);
        }

        return count;
    }

    /**
     * Return the {@link City city} with the primary key equals to {@code primaryKey}
     *
     * @param primaryKey The primary key used to obtain the obj instance
     * @return The {@link City city} with {@code primaryKey}
     * @throws DAOException If an error occurred during the information retrieving
     */
    @Override
    public City getByPrimaryKey(Long primaryKey) throws DAOException {
        City city = null;
        if (primaryKey == null)
            throw new DAOException("Primary key is null");

        try (PreparedStatement pStmt = CONNECTION.prepareStatement(SQL_GET_BY_PRIMARY_KEY)) {
            pStmt.setLong(1, primaryKey);
            try (ResultSet rs = pStmt.executeQuery()) {
                if (rs.next()) {
                    city = setAndGetDAO(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get City for the passed primary key", ex);
        }

        return city;
    }

    /**
     * Return a list of all {@link City city} in the persistence system
     *
     * @return A list of all saved {@link City city} in the persistence system
     * @throws DAOException If an error occurred during the information retrieving
     */
    @Override
    public List<City> getAll() throws DAOException {
        List<City> cities = new ArrayList<>();

        try (Statement stmt = CONNECTION.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(SQL_GET_ALL)) {
                while (rs.next()) {
                    cities.add(setAndGetDAO(rs));
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the list of City", ex);
        }

        return cities;
    }
}
