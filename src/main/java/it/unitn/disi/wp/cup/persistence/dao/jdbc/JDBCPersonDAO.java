package it.unitn.disi.wp.cup.persistence.dao.jdbc;

import it.unitn.disi.wp.cup.persistence.dao.*;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOFactoryException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;
import it.unitn.disi.wp.cup.persistence.dao.factory.jdbc.JDBCDAO;
import it.unitn.disi.wp.cup.persistence.entity.Person;
import it.unitn.disi.wp.cup.util.PersonAvatarUtil;

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
    private static final String SQL_UPDATE = "UPDATE person SET email = ?, password = ?, name = ?, surname = ?, sex = ?, fiscal_code = ?, birth_date = ?, birth_city_id = ?, city_id = ? WHERE id = ?";

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
        PersonSexDAO personSexDAO;
        CityDAO cityDAO;
        PrescriptionExamDAO prescriptionExamDAO;
        if (rs == null) throw new DAOException("ResultSet cannot be null");

        try {
            personAvatarDAO = DAO_FACTORY.getDAO(PersonAvatarDAO.class);
            personSexDAO = DAO_FACTORY.getDAO(PersonSexDAO.class);
            cityDAO = DAO_FACTORY.getDAO(CityDAO.class);
            prescriptionExamDAO = DAO_FACTORY.getDAO(PrescriptionExamDAO.class);

            person = new Person();

            person.setId(rs.getLong("id"));
            person.setEmail(rs.getString("email"));
            person.setPassword(rs.getString("password"));
            person.setName(rs.getString("name"));
            person.setSurname(rs.getString("surname"));
            person.setSex(personSexDAO.getByPrimaryKey(rs.getString("sex").charAt(0)));
            person.setFiscalCode(rs.getString("fiscal_code"));
            person.setBirthDate(rs.getObject("birth_date", LocalDate.class));
            person.setBirthCity(cityDAO.getByPrimaryKey(rs.getLong("birth_city_id")));
            person.setCity(cityDAO.getByPrimaryKey(rs.getLong("city_id")));
            person.setAvatar(PersonAvatarUtil.checkPersonAvatar(personAvatarDAO.getCurrentByPersonId(person.getId()), person.getId(), person.getSex()));
            person.setAvatarHistory(personAvatarDAO.getAllByPersonId(person.getId()));
            person.setExams(prescriptionExamDAO.getAllByPersonId(person.getId()));
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
            pStmt.setString(1, person.getEmail());
            pStmt.setString(2, person.getPassword());
            pStmt.setString(3, person.getName());
            pStmt.setString(4, person.getSurname());
            pStmt.setString(5, Character.toString(person.getSex().getSex()));
            pStmt.setString(6, person.getFiscalCode());
            pStmt.setObject(7, person.getBirthDate());
            pStmt.setLong(8, person.getBirthCity().getId());
            pStmt.setLong(9, person.getCity().getId());
            pStmt.setLong(10, person.getId());

            if (pStmt.executeUpdate() == 1)
                toRtn = true;
        } catch (SQLException ex) {
            throw new DAOException("Impossible to update the Person", ex);
        }

        return toRtn;
    }
}
