package it.unitn.disi.wp.cup.persistence.dao.jdbc;

import it.unitn.disi.wp.cup.persistence.dao.DoctorDAO;
import it.unitn.disi.wp.cup.persistence.dao.PersonDAO;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOFactoryException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;
import it.unitn.disi.wp.cup.persistence.dao.factory.jdbc.JDBCDAO;
import it.unitn.disi.wp.cup.persistence.entity.Doctor;
import it.unitn.disi.wp.cup.persistence.entity.Person;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.sql.*;
import java.time.LocalDateTime;
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
    private static final String SQL_GET_BY_PATIENT_ID = "WITH history AS (SELECT * FROM is_patient WHERE person_id = ?)" +
            "SELECT * FROM history WHERE history.since = (SELECT MAX(since) AS max_since FROM history) LIMIT 1";
    private static final String SQL_GET_SINCE_BY_PATIENT_ID = "WITH history AS (SELECT * FROM is_patient WHERE person_id = ?)" +
            "SELECT since FROM history WHERE history.since = (SELECT MAX(since) AS max_since FROM history) LIMIT 1";
    private static final String SQL_GET_ALL_AVAILABLE_BY_PATIENT_ID = "WITH filtered_cities AS (SELECT city.id FROM city WHERE city.province_id = (SELECT city.province_id FROM city INNER JOIN person ON (person.city_id = city.id) WHERE person.id = ?))" +
            "SELECT doctor.id FROM doctor INNER JOIN person ON (doctor.id = person.id), filtered_cities" +
            " WHERE person.city_id = filtered_cities.id AND doctor.id NOT IN(SELECT is_patient.doctor_id FROM is_patient WHERE person_id=?) AND doctor.id !=?";
    private static final String SQL_ADD_PATIENT = "INSERT INTO is_patient(doctor_id, person_id) VALUES (?, ?)";
    private static final String SQL_GET_HISTORY_BY_PATIENT_ID = "SELECT * FROM is_patient WHERE person_id = ?";


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
            doctor.setSince(rs.getObject("since", LocalDateTime.class));
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

        try (PreparedStatement pStmt = CONNECTION.prepareStatement(SQL_GET_BY_PATIENT_ID)) {
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

    @Override
    public LocalDateTime getSinceByPatientId(Long patientId) throws DAOException {
        LocalDateTime since = null;
        if (patientId == null)
            throw new DAOException("Patient Id is a mandatory fields", new NullPointerException("Patient Id is null"));

        try (PreparedStatement pStmt = CONNECTION.prepareStatement(SQL_GET_SINCE_BY_PATIENT_ID)) {
            pStmt.setLong(1, patientId);
            try (ResultSet rs = pStmt.executeQuery()) {
                if (rs.next()) {
                    since = rs.getObject(1, LocalDateTime.class);
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get Since by Patient Id");
        }

        return since;
    }

    @Override
    public List<Person> getAllAvailableByPatientId(Long patientId) throws DAOException {
        List<Person> doctors = new ArrayList<>();
        PersonDAO personDAO;
        if (patientId == null)
            throw new DAOException("Patient Id is a mandatory fields", new NullPointerException("Patient Id is null"));

        try (PreparedStatement pStmt = CONNECTION.prepareStatement(SQL_GET_ALL_AVAILABLE_BY_PATIENT_ID)) {
            pStmt.setLong(1, patientId);
            pStmt.setLong(2, patientId);
            pStmt.setLong(3, patientId);
            personDAO = DAO_FACTORY.getDAO(PersonDAO.class);

            try (ResultSet rs = pStmt.executeQuery()) {
                while (rs.next()) {
                    doctors.add(personDAO.getByPrimaryKey(rs.getLong(1)));
                }
            }
        } catch (SQLException | DAOFactoryException ex) {
            throw new DAOException("Impossible to get the list of available Doctors by Patient Id", ex);
        }

        return doctors;
    }

    @Override
    public Pair<Long, Long> addPatient(Long doctorId, Long patientId) throws DAOException {
        Pair<Long, Long> record = null;
        if (doctorId == null || patientId == null)
            throw new DAOException("Doctor Id and Patient Id are a mandatory fields", new NullPointerException("Doctor Id or Patient Id is null"));

        try (PreparedStatement pStmt = CONNECTION.prepareStatement(SQL_ADD_PATIENT, Statement.RETURN_GENERATED_KEYS)) {
            pStmt.setLong(1, doctorId);
            pStmt.setLong(2, patientId);

            if (pStmt.executeUpdate() == 1) {
                ResultSet rs = pStmt.getGeneratedKeys();
                if (rs.next()) {
                    record = new ImmutablePair<>(rs.getLong("doctor_id"), rs.getLong("person_id"));
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to add a Patient to a Doctor", ex);
        }

        return record;
    }

    @Override
    public List<Doctor> getHistoryByPatientId(Long patientId) throws DAOException {
        List<Doctor> history = new ArrayList<>();
        if (patientId == null)
            throw new DAOException("Patient Id is a mandatory field", new NullPointerException("Patient Id is null"));

        try (PreparedStatement pStmt = CONNECTION.prepareStatement(SQL_GET_HISTORY_BY_PATIENT_ID)) {
            pStmt.setLong(1, patientId);

            try (ResultSet rs = pStmt.executeQuery()) {
                while (rs.next()) {
                    history.add(getByPrimaryKey(rs.getLong("doctor_id")));
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get history by Patient Id");
        }

        return history;
    }
}
