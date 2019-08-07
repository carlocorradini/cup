package it.unitn.disi.wp.cup.persistence.dao.jdbc;

import it.unitn.disi.wp.cup.persistence.dao.PersonAvatarDAO;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;
import it.unitn.disi.wp.cup.persistence.dao.factory.jdbc.JDBCDAO;
import it.unitn.disi.wp.cup.persistence.entity.PersonAvatar;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of {@link PersonAvatar} interface
 *
 * @author Carlo Corradini
 */
public class JDBCPersonAvatarDAO extends JDBCDAO<PersonAvatar, Long> implements PersonAvatarDAO {

    private static final String SQL_GET_COUNT = "SELECT COUNT(*) FROM person_avatar";
    private static final String SQL_GET_BY_PRIMARY_KEY = "SELECT * FROM person_avatar WHERE id = ? LIMIT 1";
    private static final String SQL_GET_ALL = "SELECT * FROM person_avatar";
    private static final String SQL_ADD = "INSERT INTO person_avatar(person_id, name) VALUES(?, ?)";
    private static final String SQL_GET_CURRENT_BY_PERSON_ID_ = "WITH history AS (SELECT * FROM person_avatar WHERE person_id = ?)" +
            " SELECT * FROM history WHERE history.upload = (SELECT MAX(upload) AS max_upload FROM history) LIMIT 1";
    private static final String SQL_GET_ALL_BY_PERSON_ID = "SELECT * FROM person_avatar WHERE person_id = ? ORDER BY upload DESC";

    /**
     * The default constructor of the class
     *
     * @param connection The Connection to the persistence system
     * @param daoFactory The DAOFactory to get DAOs
     */
    public JDBCPersonAvatarDAO(Connection connection, DAOFactory daoFactory) {
        super(connection, daoFactory);
    }

    @Override
    public PersonAvatar setAndGetDAO(ResultSet rs) throws DAOException {
        PersonAvatar personAvatar;
        if (rs == null) throw new DAOException("ResultSet cannot be null");

        try {
            personAvatar = new PersonAvatar();
            personAvatar.setId(rs.getLong("id"));
            personAvatar.setPersonId(rs.getLong("person_id"));
            personAvatar.setName(rs.getString("name"));
            personAvatar.setUpload(rs.getObject("upload", LocalDateTime.class));
        } catch (SQLException ex) {
            throw new DAOException("Impossible to set PersonAvatar by ResultSet", ex);
        }
        return personAvatar;
    }

    /**
     * Return the number of {@link PersonAvatar person avatars} stored in the persistence system
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
            throw new DAOException("Impossible to count Person Avatar", ex);
        }

        return count;
    }

    /**
     * Return the {@link PersonAvatar personAvatar} with the primary key equals to {@code primaryKey}
     *
     * @param primaryKey The primary key used to obtain the obj instance
     * @return The {@link PersonAvatar personAvatar} with {@code primaryKey}
     * @throws DAOException If an error occurred during the information retrieving
     */
    @Override
    public PersonAvatar getByPrimaryKey(Long primaryKey) throws DAOException {
        PersonAvatar personAvatar = null;
        if (primaryKey == null)
            throw new DAOException("Primary key is null");

        try (PreparedStatement pStmt = CONNECTION.prepareStatement(SQL_GET_BY_PRIMARY_KEY)) {
            pStmt.setLong(1, primaryKey);
            try (ResultSet rs = pStmt.executeQuery()) {
                if (rs.next()) {
                    personAvatar = setAndGetDAO(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get Person Avatar for the passed primary key", ex);
        }

        return personAvatar;
    }

    /**
     * Return a list of all {@link PersonAvatar personAvatar} in the persistence system
     *
     * @return A list of all saved {@link PersonAvatar personAvatar} in the persistence system
     * @throws DAOException If an error occurred during the information retrieving
     */
    @Override
    public List<PersonAvatar> getAll() throws DAOException {
        List<PersonAvatar> avatars = new ArrayList<>();

        try (Statement stmt = CONNECTION.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(SQL_GET_ALL)) {
                while (rs.next()) {
                    avatars.add(setAndGetDAO(rs));
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the list of Person Avatar", ex);
        }

        return avatars;
    }

    @Override
    public Long add(PersonAvatar personAvatar) throws DAOException {
        Long id = null;
        if (personAvatar == null)
            throw new DAOException("Person Avatar is mandatory", new NullPointerException());

        try (PreparedStatement pStmt = CONNECTION.prepareStatement(SQL_ADD, Statement.RETURN_GENERATED_KEYS)) {
            pStmt.setLong(1, personAvatar.getPersonId());
            pStmt.setString(2, personAvatar.getName());

            if (pStmt.executeUpdate() == 1) {
                ResultSet rs = pStmt.getGeneratedKeys();
                if (rs.next()) {
                    id = rs.getLong(1);
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to add the Person Avatar", ex);
        }

        return id;
    }

    @Override
    public PersonAvatar getCurrentByPersonId(Long personId) throws DAOException {
        PersonAvatar personAvatar = null;
        if (personId == null)
            throw new DAOException("Person id is mandatory", new NullPointerException());

        try (PreparedStatement pStmt = CONNECTION.prepareStatement(SQL_GET_CURRENT_BY_PERSON_ID_)) {
            pStmt.setLong(1, personId);
            try (ResultSet rs = pStmt.executeQuery()) {
                if (rs.next()) {
                    personAvatar = setAndGetDAO(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the current PersonAvatar for the passed Person ID", ex);
        }

        return personAvatar;
    }

    @Override
    public List<PersonAvatar> getAllByPersonId(Long personId) throws DAOException {
        List<PersonAvatar> avatars = new ArrayList<>();
        if (personId == null)
            throw new DAOException("Person id is mandatory", new NullPointerException());

        try (PreparedStatement pStmt = CONNECTION.prepareStatement(SQL_GET_ALL_BY_PERSON_ID)) {
            pStmt.setLong(1, personId);
            try (ResultSet rs = pStmt.executeQuery()) {
                while (rs.next()) {
                    avatars.add(setAndGetDAO(rs));
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the List of Person Avatar for the passed Person ID", ex);
        }

        return avatars;
    }
}
