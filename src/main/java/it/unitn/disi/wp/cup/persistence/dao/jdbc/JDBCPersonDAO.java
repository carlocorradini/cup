package it.unitn.disi.wp.cup.persistence.dao.jdbc;

import it.unitn.disi.wp.cup.persistence.dao.CityDAO;
import it.unitn.disi.wp.cup.persistence.dao.PersonAvatarDAO;
import it.unitn.disi.wp.cup.persistence.dao.PersonDAO;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOFactoryException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;
import it.unitn.disi.wp.cup.persistence.dao.factory.jdbc.JDBCDAO;
import it.unitn.disi.wp.cup.persistence.entity.Person;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of {@link Person} interface
 *
 * @author Carlo Corradini
 */
public class JDBCPersonDAO extends JDBCDAO<Person, Long> implements PersonDAO {

    private static final String SQL_GET_COUNT = "SELECT COUNT(*) FROM person";
    private static final String SQL_GET_BY_PRIMARY_KEY = "SELECT * FROM person WHERE id = ? LIMIT 1";
    private static final String SQL_GET_ALL = "SELECT * FROM person";
    private static final String SQL_GET_BY_EMAIL = "SELECT * FROM person WHERE email = ? LIMIT 1";
    private static final String SQL_UPDATE = "UPDATE person SET name = ?, surname = ?, email = ?, password = ?, sex = ?, avatar_id = ? WHERE id = ?";

    /**
     * The default constructor of the class
     *
     * @param connection The Connection to the persistence system
     * @param daoFactory The DAOFactory to get DAOs
     */
    public JDBCPersonDAO(Connection connection, DAOFactory daoFactory) {
        super(connection, daoFactory);
    }

    @Override
    public Person setAndGetDAO(ResultSet rs) throws DAOException {
        Person person;
        PersonAvatarDAO personAvatarDAO;
        CityDAO cityDAO;
        DoctorDAO doctorDAO;
        if (rs == null) throw new DAOException("ResultSet cannot be null");

        try {
            personAvatarDAO = DAO_FACTORY.getDAO(PersonAvatarDAO.class);
            cityDAO = DAO_FACTORY.getDAO(CityDAO.class);
            doctorDAO = DAO_FACTORY.getDAO(DoctorDAO.class);

            person = new Person();

            person.setId(rs.getLong("id"));
            person.setEmail(rs.getString("email"));
            person.setPassword(rs.getString("password"));
            person.setName(rs.getString("name"));
            person.setSurname(rs.getString("surname"));
            person.setSex(rs.getString("sex").charAt(0));
            person.setFiscalCode(rs.getString("fiscal_code"));
            person.setBirthDate(rs.getObject("birth_date", LocalDate.class));
            person.setBithCity(cityDAO.getByPrimaryKey(rs.getLong("birth_city_id")));
            person.setCity(cityDAO.getByPrimaryKey(rs.getLong("city_id")));
            person.setDoctor(doctorDAO.getByPrimaryKey(rs.getLong("doctor_id")));
            person.setAvatar(personAvatarDAO.getByPrimaryKey(rs.getLong("avatar_id")));
            person.setAvatarHistory(personAvatarDAO.getAllByPersonId(person.getId()));
        } catch (SQLException | DAOFactoryException ex) {
            throw new DAOException("Impossible to set Person by ResultSet", ex);
        }
        return person;
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
            throw new DAOException("Impossible to count Person", ex);
        }

        return count;
    }

    /**
     * Return the {@link Person person} with the primary key equals to {@code primaryKey}
     *
     * @param primaryKey The primary key used to obtain the obj instance
     * @return The {@link Person person} with {@code primaryKey}
     * @throws DAOException If an error occurred during the information retrieving
     */
    @Override
    public Person getByPrimaryKey(Long primaryKey) throws DAOException {
        Person person = null;
        if (primaryKey == null)
            throw new DAOException("Primary key is null");

        try (PreparedStatement pStmt = CONNECTION.prepareStatement(SQL_GET_BY_PRIMARY_KEY)) {
            pStmt.setLong(1, primaryKey);
            try (ResultSet rs = pStmt.executeQuery()) {
                if (rs.next()) {
                    person = setAndGetDAO(rs);
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
                    persons.add(setAndGetDAO(rs));
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the list of Person", ex);
        }

        return persons;
    }

    @Override
    public Person getByEmail(String email) throws DAOException {
        Person person = null;
        if (email == null)
            throw new DAOException("Email is a mandatory fields", new NullPointerException("Email is null"));

        try (PreparedStatement pStmt = CONNECTION.prepareStatement(SQL_GET_BY_EMAIL)) {
            pStmt.setString(1, email);
            try (ResultSet rs = pStmt.executeQuery()) {
                if (rs.next()) {
                    person = setAndGetDAO(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the Person", ex);
        }

        return person;
    }

    @Override
    public boolean update(Person person) throws DAOException {
        boolean toRtn = false;
        if (person == null)
            throw new DAOException("Person is a mandatory field", new NullPointerException("Person is null"));

        try (PreparedStatement pStmt = CONNECTION.prepareStatement(SQL_UPDATE)) {
            pStmt.setString(1, person.getName());
            pStmt.setString(2, person.getSurname());
            pStmt.setString(3, person.getEmail());
            pStmt.setString(4, person.getPassword());
            pStmt.setString(5, Character.toString(person.getSex()));
            pStmt.setLong(6, person.getAvatar().getId());
            pStmt.setLong(7, person.getId());

            if (pStmt.executeUpdate() == 1)
                toRtn = true;
        } catch (SQLException ex) {
            throw new DAOException("Impossible to update the Person", ex);
        }

        return toRtn;
    }
}