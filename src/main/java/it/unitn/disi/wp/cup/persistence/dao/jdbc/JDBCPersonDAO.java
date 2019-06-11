package it.unitn.disi.wp.cup.persistence.dao.jdbc;

import it.unitn.disi.wp.cup.persistence.dao.PersonDAO;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.dao.factory.jdbc.JDBCDAO;
import it.unitn.disi.wp.cup.persistence.entity.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of {@link Person} interface
 *
 * @author Carlo Corradini
 */
public class JDBCPersonDAO extends JDBCDAO<Person, Integer> implements PersonDAO {

    private static final String SQL_GET_COUNT = "SELECT COUNT(*) FROM person";
    private static final String SQL_GET_BY_PRIMARY_KEY = "SELECT * FROM person WHERE id = ? LIMIT 1";
    private static final String SQL_GET_ALL = "SELECT * FROM person";
    private static final String SQL_GET_BY_EMAIL_AND_PASSWORD = "SELECT * FROM person WHERE email = ? AND password = ? LIMIT 1";

    /**
     * The default constructor of the class
     *
     * @param connection The Connection to the persistence system
     */
    public JDBCPersonDAO(Connection connection) {
        super(connection);
    }

    /**
     * Return the number of {@link Person persons} stored in the persistence system
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
            throw new DAOException("Impossible to count PersonDaoBean", ex);
        }

        return count;
    }

    /**
     * Return the {@link Person person} with the primary key equals to @primaryKey
     *
     * @param primaryKey The primary key used to obtain the entity instance
     * @return The {@link Person person} with @primaryKey
     * @throws DAOException If an error occurred during the information retrieving
     */
    @Override
    public Person getByPrimaryKey(Integer primaryKey) throws DAOException {
        Person person = null;
        if (primaryKey == null)
            throw new DAOException("Primary key is null");

        try (PreparedStatement pStmt = CONNECTION.prepareStatement(SQL_GET_BY_PRIMARY_KEY)) {
            pStmt.setInt(1, primaryKey);
            try (ResultSet rs = pStmt.executeQuery()) {
                if (rs.next()) {
                    person = new Person();
                    person.setId(rs.getInt("id"));
                    person.setName(rs.getString("name"));
                    person.setSurname(rs.getString("surname"));
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get Person for the passed primary key", ex);
        }

        return person;
    }

    /**
     * Return a list of all {@link Person person} in the persistence system
     *
     * @return A list of all saved {@link Person person} in the persistence system
     * @throws DAOException If an error occurred during the information retrieving
     */
    @Override
    public List<Person> getAll() throws DAOException {
        List<Person> persons = new ArrayList<>();

        try (Statement stmt = CONNECTION.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(SQL_GET_ALL)) {
                while (rs.next()) {
                    Person person = new Person();
                    person.setId(rs.getInt("id"));
                    person.setName(rs.getString("name"));
                    person.setSurname(rs.getString("surname"));

                    persons.add(person);
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the list of Person", ex);
        }

        return persons;
    }

    @Override
    public Person getByEmailAndPassword(String email, String password) throws DAOException {
        Person person = null;
        if (email == null || password == null)
            throw new DAOException("Email & Password are mandatory fields", new NullPointerException("Email or password are null"));

        try (PreparedStatement pStmt = CONNECTION.prepareStatement(SQL_GET_BY_EMAIL_AND_PASSWORD)) {
            pStmt.setString(1, email);
            pStmt.setString(2, password);
            try (ResultSet rs = pStmt.executeQuery()) {
                if (rs.next()) {
                    person = new Person();
                    person.setId(rs.getInt("id"));
                    person.setEmail(rs.getString("email"));
                    person.setPassword(rs.getString("password"));
                    person.setName(rs.getString("name"));
                    person.setSurname(rs.getString("surname"));
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the Person", ex);
        }

        return person;
    }
}
