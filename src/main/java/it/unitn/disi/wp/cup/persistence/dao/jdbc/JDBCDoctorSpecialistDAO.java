package it.unitn.disi.wp.cup.persistence.dao.jdbc;

import it.unitn.disi.wp.cup.persistence.dao.DoctorSpecialistDAO;
import it.unitn.disi.wp.cup.persistence.dao.PersonDAO;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOFactoryException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;
import it.unitn.disi.wp.cup.persistence.dao.factory.jdbc.JDBCDAO;
import it.unitn.disi.wp.cup.persistence.entity.DoctorSpecialist;
import it.unitn.disi.wp.cup.persistence.entity.Person;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of {@link DoctorSpecialist} interface
 *
 * @author Carlo Corradini
 */
public class JDBCDoctorSpecialistDAO extends JDBCDAO<DoctorSpecialist, Long> implements DoctorSpecialistDAO {
    private static final String SQL_GET_COUNT = "SELECT COUNT(*) FROM doctor_specialist";
    private static final String SQL_GET_BY_PRIMARY_KEY = "SELECT * FROM doctor_specialist WHERE id = ? LIMIT 1";
    private static final String SQL_GET_ALL = "SELECT * FROM doctor_specialist";
    private static final String SQL_GET_ALL_QUALIFIED_BY_PROVINCE_ID_AND_EXAM_ID = "WITH specialist_city AS (SELECT doctor_specialist.id, city_id FROM doctor_specialist INNER JOIN person ON (doctor_specialist.id = person.id))," +
            "specialist_exam AS (SELECT doctor_specialist.id FROM doctor_specialist INNER JOIN exam_qualification ON (doctor_specialist.id = exam_qualification.doctor_specialist_id) WHERE exam_qualification.exam_id = ?)" +
            " SELECT specialist_exam.id FROM specialist_exam, specialist_city INNER JOIN city ON (specialist_city.city_id = city.id) WHERE city.province_id = ? AND specialist_city.id = specialist_exam.id";

    /**
     * The default constructor of the class
     *
     * @param connection The Connection to the persistence system
     * @param daoFactory The DAOFactory to get DAOs
     */
    public JDBCDoctorSpecialistDAO(Connection connection, DAOFactory daoFactory) {
        super(connection, daoFactory);
    }

    @Override
    public DoctorSpecialist setAndGetDAO(ResultSet rs) throws DAOException {
        DoctorSpecialist doctorSpecialist;
        if (rs == null) throw new DAOException("ResultSet cannot be null");

        try {
            doctorSpecialist = new DoctorSpecialist();

            doctorSpecialist.setId(rs.getLong("id"));
            doctorSpecialist.setSince(rs.getObject("since", LocalDateTime.class));
        } catch (SQLException ex) {
            throw new DAOException("Impossible to set DoctorSpecialist by ResultSet", ex);
        }
        return doctorSpecialist;
    }

    /**
     * Return the number of {@link DoctorSpecialist doctorSpecialist} stored in the persistence system
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
            throw new DAOException("Impossible to count DoctorSpecialist", ex);
        }

        return count;
    }

    /**
     * Return the {@link DoctorSpecialist doctorSpecialist} with the primary key equals to {@code primaryKey}
     *
     * @param primaryKey The primary key used to obtain the obj instance
     * @return The {@link DoctorSpecialist doctorSpecialist} with {@code primaryKey}
     * @throws DAOException If an error occurred during the information retrieving
     */
    @Override
    public DoctorSpecialist getByPrimaryKey(Long primaryKey) throws DAOException {
        DoctorSpecialist doctorSpecialist = null;

        if (primaryKey == null)
            throw new DAOException("Primary key is null");

        try (PreparedStatement pStmt = CONNECTION.prepareStatement(SQL_GET_BY_PRIMARY_KEY)) {
            pStmt.setLong(1, primaryKey);
            try (ResultSet rs = pStmt.executeQuery()) {
                if (rs.next()) {
                    doctorSpecialist = setAndGetDAO(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get DoctorSpecialist for the passed primary key", ex);
        }

        return doctorSpecialist;
    }

    /**
     * Return a list of all {@link DoctorSpecialist doctorSpecialist} in the persistence system
     *
     * @return A list of all saved {@link DoctorSpecialist doctorSpecialist} in the persistence system
     * @throws DAOException If an error occurred during the information retrieving
     */
    @Override
    public List<DoctorSpecialist> getAll() throws DAOException {
        List<DoctorSpecialist> specialists = new ArrayList<>();

        try (Statement stmt = CONNECTION.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(SQL_GET_ALL)) {
                while (rs.next()) {
                    specialists.add(setAndGetDAO(rs));
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the list of Specialist", ex);
        }

        return specialists;
    }

    @Override
    public List<Person> getAllQualifiedbyProvinceIdAndExamId(Long provinceId, Long examId) throws DAOException {
        List<Person> qualified = new ArrayList<>();
        PersonDAO personDAO;
        if (provinceId == null || examId == null)
            throw new DAOException("Province Id and Exam Id are mandatory", new NullPointerException("Province Id and/or Exam Id is null"));
        try {
            personDAO = DAO_FACTORY.getDAO(PersonDAO.class);
        } catch (DAOFactoryException ex) {
            throw new DAOException("Unable to get Person DAO for qualification", ex);
        }

        try (PreparedStatement pStmt = CONNECTION.prepareStatement(SQL_GET_ALL_QUALIFIED_BY_PROVINCE_ID_AND_EXAM_ID)) {
            pStmt.setLong(1, examId);
            pStmt.setLong(2, provinceId);
            try (ResultSet rs = pStmt.executeQuery()) {
                while (rs.next()) {
                    qualified.add(personDAO.getByPrimaryKey(rs.getLong(1)));
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Unable to get the List of qualified Doctor Specialist by Province Id and Exam Id");
        }

        return qualified;
    }
}
