package it.unitn.disi.wp.cup.persistence.dao.jdbc;

import it.unitn.disi.wp.cup.persistence.dao.MedicineDAO;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;
import it.unitn.disi.wp.cup.persistence.dao.factory.jdbc.JDBCDAO;
import it.unitn.disi.wp.cup.persistence.entity.Medicine;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of {@link Medicine} interface
 *
 * @author Carlo Corradini
 */
public class JDBCMedicineDAO extends JDBCDAO<Medicine, Long> implements MedicineDAO {

    private static final String SQL_GET_COUNT = "SELECT COUNT(*) FROM medicine";
    private static final String SQL_GET_BY_PRIMARY_KEY = "SELECT * FROM medicine WHERE id = ? LIMIT 1";
    private static final String SQL_GET_ALL = "SELECT * FROM medicine";

    /**
     * The default constructor of the class
     *
     * @param connection The Connection to the persistence system
     * @param daoFactory The DAOFactory to get DAOs
     */
    public JDBCMedicineDAO(Connection connection, DAOFactory daoFactory) {
        super(connection, daoFactory);
    }

    @Override
    public Medicine setAndGetDAO(ResultSet rs) throws DAOException {
        Medicine medicine;
        if (rs == null) throw new DAOException("ResultSet cannot be null");

        try {
            medicine = new Medicine();

            medicine.setId(rs.getLong("id"));
            medicine.setName(rs.getString("name"));
            medicine.setPrice(rs.getFloat("price"));
        } catch (SQLException ex) {
            throw new DAOException("Impossible to set Medicine by ResultSet", ex);
        }
        return medicine;
    }

    /**
     * Return the number of {@link Medicine medicines} stored in the persistence system
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
            throw new DAOException("Impossible to count Medicine", ex);
        }

        return count;
    }

    /**
     * Return the {@link Medicine medicine} with the primary key equals to {@code primaryKey}
     *
     * @param primaryKey The primary key used to obtain the obj instance
     * @return The {@link Medicine medicine} with {@code primaryKey}
     * @throws DAOException If an error occurred during the information retrieving
     */
    @Override
    public Medicine getByPrimaryKey(Long primaryKey) throws DAOException {
        Medicine medicine = null;
        if (primaryKey == null)
            throw new DAOException("Primary key is null");

        try (PreparedStatement pStmt = CONNECTION.prepareStatement(SQL_GET_BY_PRIMARY_KEY)) {
            pStmt.setLong(1, primaryKey);
            try (ResultSet rs = pStmt.executeQuery()) {
                if (rs.next()) {
                    medicine = setAndGetDAO(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get Medicine for the passed primary key", ex);
        }

        return medicine;
    }

    /**
     * Return a list of all {@link Medicine medicines} in the persistence system
     *
     * @return A list of all saved {@link Medicine medicines} in the persistence system
     * @throws DAOException If an error occurred during the information retrieving
     */
    @Override
    public List<Medicine> getAll() throws DAOException {
        List<Medicine> medicines = new ArrayList<>();

        try (Statement stmt = CONNECTION.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(SQL_GET_ALL)) {
                while (rs.next()) {
                    medicines.add(setAndGetDAO(rs));
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the list of Medicines", ex);
        }

        return medicines;
    }
}
