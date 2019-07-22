package it.unitn.disi.wp.cup.persistence.dao.jdbc;

import it.unitn.disi.wp.cup.persistence.dao.DoctorDAO;
import it.unitn.disi.wp.cup.persistence.dao.PersonDAO;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOFactoryException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;
import it.unitn.disi.wp.cup.persistence.dao.factory.jdbc.JDBCDAO;
import it.unitn.disi.wp.cup.persistence.entity.Doctor;
import it.unitn.disi.wp.cup.persistence.entity.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of {@link Doctor} interface
 *
 * @author Carlo Corradini
 */
public class JDBCDoctorDAO extends JDBCDAO<Doctor, Long> implements DoctorDAO {

    private static final String SQL_GET_COUNT = "SELECT COUNT(*) FROM doctor";
    private static final String SQL_GET_BY_PRIMARY_KEY = "SELECT * FROM doctor WHERE id = ? LIMIT 1";
    private static final String SQL_GET_ALL = "SELECT * FROM doctor";
    private static final String SQL_GET_PATIENTS = "WITH history AS (SELECT person_id, MAX(since) AS max_since FROM is_patient GROUP BY person_id)" +
            "SELECT A.person_id, A.doctor_id, A.since FROM is_patient AS A INNER JOIN history AS B ON A.person_id = B.person_id AND A.since = B.max_since WHERE A.doctor_id = ? ORDER BY A.person_id";
    private static final String SQL_GET_BY_PATIENT = "WITH history AS (SELECT * FROM is_patient WHERE person_id = ?)" +
            "SELECT * FROM history WHERE history.since = (SELECT MAX(since) AS max_since FROM history) LIMIT 1";

    /**
     * The default constructor of the class
     *
     * @param connection The Connection to the persistence system
     * @param daoFactory The DAOFactory to get DAOs
     */
    public JDBCDoctorDAO(Connection connection, DAOFactory daoFactory) {
        super(connection, daoFactory);
    }

    @Override
    public Doctor setAndGetDAO(ResultSet rs) throws DAOException {
        Doctor doctor;
        if (rs == null) throw new DAOException("ResultSet cannot be null");

        try {
            doctor = new Doctor();

            doctor.setId(rs.getLong("id"));
            doctor.setPatients(getPatientsByDoctorId(doctor.getId()));
        } catch (SQLException ex) {
            throw new DAOException("Impossible to set Doctor by ResultSet", ex);
        }
        return doctor;
    }

    /**
     * Return the number of {@link Doctor doctors} stored in the persistence system
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
            throw new DAOException("Impossible to count Doctor", ex);
        }

        return count;
    }

    /**
     * Return the {@link Doctor doctor} with the primary key equals to {@code primaryKey}
     *
     * @param primaryKey The primary key used to obtain the obj instance
     * @return The {@link Doctor doctor} with {@code primaryKey}
     * @throws DAOException If an error occurred during the information retrieving
     */
    @Override
    public Doctor getByPrimaryKey(Long primaryKey) throws DAOException {
        Doctor doctor = null;
        if (primaryKey == null)
            throw new DAOException("Primary key is null");

        try (PreparedStatement pStmt = CONNECTION.prepareStatement(SQL_GET_BY_PRIMARY_KEY)) {
            pStmt.setLong(1, primaryKey);
            try (ResultSet rs = pStmt.executeQuery()) {
                if (rs.next()) {
                    doctor = setAndGetDAO(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get Doctor for the passed primary key", ex);
        }

        return doctor;
    }

    /**
     * Return a list of all {@link Doctor doctor} in the persistence system
     *
     * @return A list of all saved {@link Doctor doctor} in the persistence system
     * @throws DAOException If an error occurred during the information retrieving
     */
    @Override
    public List<Doctor> getAll() throws DAOException {
        List<Doctor> doctors = new ArrayList<>();

        try (Statement stmt = CONNECTION.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(SQL_GET_ALL)) {
                while (rs.next()) {
                    doctors.add(setAndGetDAO(rs));
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the list of Doctor", ex);
        }

        return doctors;
    }


    @Override
    public List<Person> getPatientsByDoctorId(Long doctorId) throws DAOException {
        List<Person> patients = new ArrayList<>();
        PersonDAO personDAO;
        if (doctorId == null)
            throw new DAOException("Doctor Id is a mandatory field", new NullPointerException("Doctor Id is null"));

        try (PreparedStatement pStmt = CONNECTION.prepareStatement(SQL_GET_PATIENTS)) {
            pStmt.setLong(1, doctorId);
            personDAO = DAO_FACTORY.getDAO(PersonDAO.class);

            try (ResultSet rs = pStmt.executeQuery()) {
                while (rs.next()) {
                    patients.add(personDAO.getByPrimaryKey(rs.getLong("person_id")));
                }
            }
        } catch (SQLException | DAOFactoryException ex) {
            throw new DAOException("Impossible to get the list of Patients", ex);
        }

        return patients;
    }

    @Override
    public Doctor getDoctorByPatientId(Long patientId) throws DAOException {
        Doctor doctor = null;
        if (patientId == null)
            throw new DAOException("Patient Id is a mandatory field", new NullPointerException("Patient Id is null"));

        try (PreparedStatement pStmt = CONNECTION.prepareStatement(SQL_GET_BY_PATIENT)) {
            pStmt.setLong(1, patientId);
            try (ResultSet rs = pStmt.executeQuery()) {
                if (rs.next()) {
                    doctor = getByPrimaryKey(rs.getLong("doctor_id"));
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the Doctor by Patient Id", ex);
        }

        return doctor;
    }

}
