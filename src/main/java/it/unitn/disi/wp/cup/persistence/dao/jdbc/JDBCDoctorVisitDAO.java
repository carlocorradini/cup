package it.unitn.disi.wp.cup.persistence.dao.jdbc;

import it.unitn.disi.wp.cup.persistence.dao.DoctorVisitDAO;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;
import it.unitn.disi.wp.cup.persistence.dao.factory.jdbc.JDBCDAO;
import it.unitn.disi.wp.cup.persistence.entity.DoctorVisit;
import org.apache.commons.lang3.tuple.Triple;

import java.sql.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of {@link DoctorVisit} interface
 *
 * @author Carlo Corradini
 */
public class JDBCDoctorVisitDAO extends JDBCDAO<DoctorVisit, Triple<Long, Long, OffsetDateTime>> implements DoctorVisitDAO {

    private static final String SQL_GET_COUNT = "SELECT COUNT(*) FROM doctor_visit";
    private static final String SQL_GET_BY_PRIMARY_KEY = "SELECT * FROM doctor_visit WHERE person_id = ? AND doctor_id = ? AND on = ? LIMIT 1";
    private static final String SQL_GET_ALL = "SELECT * FROM doctor_visit ORDER BY visit_date DESC";
    private static final String SQL_GET_ALL_BY_PERSON_ID = "SELECT * FROM doctor_visit WHERE  person_id = ? ORDER BY visit_date DESC";
    private static final String SQL_GET_ALL_BY_DOCTOR_ID = "SELECT * FROM doctor_visit WHERE  doctor_id = ? ORDER BY visit_date DESC";

    /**
     * The default constructor of the class
     *
     * @param connection The Connection to the persistence system
     * @param daoFactory The DAOFactory to get DAOs
     */
    public JDBCDoctorVisitDAO(Connection connection, DAOFactory daoFactory) {
        super(connection, daoFactory);
    }

    @Override
    public DoctorVisit setAndGetDAO(ResultSet rs) throws DAOException {
        DoctorVisit doctorVisit;
        if (rs == null) throw new DAOException("ResultSet cannot be null");

        try {
            doctorVisit = new DoctorVisit();

            doctorVisit.setPersonId(rs.getLong("person_id"));
            doctorVisit.setDoctorId(rs.getLong("doctor_id"));
            doctorVisit.setDate(rs.getObject("visit_date", OffsetDateTime.class));
            doctorVisit.setComment(rs.getString("comment"));
        } catch (SQLException ex) {
            throw new DAOException("Impossible to set Doctor Visit by ResultSet", ex);
        }
        return doctorVisit;
    }

    /**
     * Return the number of {@link DoctorVisit visits} stored in the persistence system
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
            throw new DAOException("Impossible to count Doctor Visit", ex);
        }

        return count;
    }

    /**
     * Return the {@link DoctorVisit visits} with the primary key equals to {@code primaryKey}
     *
     * @param primaryKey The primary key used to obtain the obj instance
     * @return The {@link DoctorVisit visits} with {@code primaryKey}
     * @throws DAOException If an error occurred during the information retrieving
     */
    @Override
    public DoctorVisit getByPrimaryKey(Triple<Long, Long, OffsetDateTime> primaryKey) throws DAOException {
        DoctorVisit doctorVisit = null;
        if (primaryKey == null)
            throw new DAOException("Primary key is null");

        try (PreparedStatement pStmt = CONNECTION.prepareStatement(SQL_GET_BY_PRIMARY_KEY)) {
            pStmt.setLong(1, primaryKey.getLeft());
            pStmt.setLong(2, primaryKey.getMiddle());
            pStmt.setObject(3, primaryKey.getRight());

            try (ResultSet rs = pStmt.executeQuery()) {
                if (rs.next()) {
                    doctorVisit = setAndGetDAO(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get Doctor Visit for the passed primary key", ex);
        }

        return doctorVisit;
    }

    /**
     * Return a list of all {@link DoctorVisit visits} in the persistence system
     *
     * @return A list of all saved {@link DoctorVisit visits} in the persistence system
     * @throws DAOException If an error occurred during the information retrieving
     */
    @Override
    public List<DoctorVisit> getAll() throws DAOException {
        List<DoctorVisit> allVisits = new ArrayList<>();

        try (Statement stmt = CONNECTION.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(SQL_GET_ALL)) {
                while (rs.next()) {
                    allVisits.add(setAndGetDAO(rs));
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the list of Doctor Visit", ex);
        }

        return allVisits;
    }

    @Override
    public List<DoctorVisit> getAllByPersonId(Long personId) throws DAOException {
        List<DoctorVisit> personVisits = new ArrayList<>();

        try (PreparedStatement pStmt = CONNECTION.prepareStatement(SQL_GET_ALL_BY_PERSON_ID)) {
            pStmt.setLong(1, personId);

            try (ResultSet rs = pStmt.executeQuery()) {
                while (rs.next()) {
                    personVisits.add(setAndGetDAO(rs));
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the list of Doctor Visit by Person Id", ex);
        }

        return personVisits;
    }

    @Override
    public List<DoctorVisit> getAllByDoctorId(Long doctorId) throws DAOException {
        List<DoctorVisit> doctorVisits = new ArrayList<>();

        try (PreparedStatement pStmt = CONNECTION.prepareStatement(SQL_GET_ALL_BY_DOCTOR_ID)) {
            pStmt.setLong(1, doctorId);

            try (ResultSet rs = pStmt.executeQuery()) {
                while (rs.next()) {
                    doctorVisits.add(setAndGetDAO(rs));
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the list of Doctor Visit by Doctor Id", ex);
        }

        return doctorVisits;
    }
}
