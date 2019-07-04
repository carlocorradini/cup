package it.unitn.disi.wp.cup.persistence.dao.jdbc;

import it.unitn.disi.wp.cup.persistence.dao.PersonDAO;
import it.unitn.disi.wp.cup.persistence.dao.PersonSexDAO;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;
import it.unitn.disi.wp.cup.persistence.dao.factory.jdbc.JDBCDAO;
import it.unitn.disi.wp.cup.persistence.entity.PersonSex;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of {@link PersonDAO} interface
 *
 * @author Carlo Corradini
 */
public class JDBCPersonSexDAO extends JDBCDAO<PersonSex, Character> implements PersonSexDAO {

    private static final String SQL_GET_COUNT = "SELECT COUNT(*) FROM person_sex";
    private static final String SQL_GET_BY_PRIMARY_KEY = "SELECT * FROM person WHERE sex = ? LIMIT 1";
    private static final String SQL_GET_ALL = "SELECT * FROM person_sex";

    /**
     * The default constructor of the class
     *
     * @param connection The Connection to the persistence system
     * @param daoFactory The DAOFactory to get DAOs
     */
    public JDBCPersonSexDAO(Connection connection, DAOFactory daoFactory) {
        super(connection, daoFactory);
    }

    @Override
    public PersonSex setAndGetDAO(ResultSet rs) throws DAOException {
        PersonSex personSex;
        if (rs == null) throw new DAOException("ResultSet cannot be null");

        try {
            personSex = new PersonSex();

            personSex.setSex(rs.getString("sex").charAt(0));
        } catch (SQLException ex) {
            throw new DAOException("Impossible to set PersonSex by ResultSet", ex);
        }
        return personSex;
    }

    /**
     * Return the number of {@link PersonSex personSex} stored in the persistence system
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
            throw new DAOException("Impossible to count PersonSex", ex);
        }

        return count;
    }

    /**
     * Return the {@link PersonSex personSex} with the primary key equals to {@code primaryKey}
     *
     * @param primaryKey The primary key used to obtain the obj instance
     * @return The {@link PersonSex personSex} with {@code primaryKey}
     * @throws DAOException If an error occurred during the information retrieving
     */
    @Override
    public PersonSex getByPrimaryKey(Character primaryKey) throws DAOException {
        PersonSex personSex = null;
        if (primaryKey == null)
            throw new DAOException("Primary key is null");

        try (PreparedStatement pStmt = CONNECTION.prepareStatement(SQL_GET_BY_PRIMARY_KEY)) {
            pStmt.setString(1, Character.toString(primaryKey));
            try (ResultSet rs = pStmt.executeQuery()) {
                if (rs.next()) {
                    personSex = setAndGetDAO(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get PersonSex for the passed primary key", ex);
        }

        return personSex;
    }

    /**
     * Return a list of all {@link PersonSex personSex} in the persistence system
     *
     * @return A list of all saved {@link PersonSex personSex} in the persistence system
     * @throws DAOException If an error occurred during the information retrieving
     */
    @Override
    public List<PersonSex> getAll() throws DAOException {
        List<PersonSex> personSexes = new ArrayList<>();

        try (Statement stmt = CONNECTION.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(SQL_GET_ALL)) {
                while (rs.next()) {
                    personSexes.add(setAndGetDAO(rs));
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the list of PersonSex", ex);
        }

        return personSexes;
    }
}
