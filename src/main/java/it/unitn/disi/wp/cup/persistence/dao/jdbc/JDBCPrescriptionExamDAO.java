package it.unitn.disi.wp.cup.persistence.dao.jdbc;

import it.unitn.disi.wp.cup.persistence.dao.ExamDAO;
import it.unitn.disi.wp.cup.persistence.dao.PrescriptionExamDAO;
import it.unitn.disi.wp.cup.persistence.dao.ReportDAO;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOFactoryException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;
import it.unitn.disi.wp.cup.persistence.dao.factory.jdbc.JDBCDAO;
import it.unitn.disi.wp.cup.persistence.entity.PrescriptionExam;

import java.sql.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of {@link PrescriptionExam} interface
 *
 * @author Carlo Corradini
 */
public class JDBCPrescriptionExamDAO extends JDBCDAO<PrescriptionExam, Long> implements PrescriptionExamDAO {
    private static final String SQL_GET_COUNT = "SELECT COUNT(*) FROM prescription_exam";
    private static final String SQL_GET_BY_PRIMARY_KEY = "SELECT * FROM prescription_exam WHERE id = ? LIMIT 1";
    private static final String SQL_GET_ALL = "SELECT * FROM prescription_exam";
    private static final String SQL_GET_ALL_BY_PERSON_ID = "SELECT * FROM prescription_exam WHERE person_id = ? ORDER BY prescription_date DESC";

    /**
     * The default constructor of the class
     *
     * @param connection The Connection to the persistence system
     * @param daoFactory The DAOFactory to get DAOs
     */
    public JDBCPrescriptionExamDAO(Connection connection, DAOFactory daoFactory) {
        super(connection, daoFactory);
    }

    @Override
    public PrescriptionExam setAndGetDAO(ResultSet rs) throws DAOException {
        PrescriptionExam prescriptionExam;
        ExamDAO examDAO;
        ReportDAO reportDAO;
        if (rs == null) throw new DAOException("ResultSet cannot be null");

        try {
            examDAO = DAO_FACTORY.getDAO(ExamDAO.class);
            reportDAO = DAO_FACTORY.getDAO(ReportDAO.class);

            prescriptionExam = new PrescriptionExam();

            prescriptionExam.setId(rs.getLong("id"));
            prescriptionExam.setPersonId(rs.getLong("person_id"));
            prescriptionExam.setDoctorId(rs.getLong("doctor_id"));
            prescriptionExam.setSpecialistId(rs.getLong("doctor_specialist_id"));
            prescriptionExam.setDateTime(rs.getObject("prescription_date", OffsetDateTime.class));
            prescriptionExam.setDateTimeRegistration(rs.getObject("prescription_date_registration", OffsetDateTime.class));
            prescriptionExam.setExam(examDAO.getByPrimaryKey(rs.getLong("exam_id")));
            prescriptionExam.setReport(reportDAO.getByPrimaryKey(rs.getLong("report_id")));
        } catch (SQLException | DAOFactoryException ex) {
            throw new DAOException("Impossible to set Prescription Exam by ResultSet", ex);
        }
        return prescriptionExam;
    }

    /**
     * Return the number of {@link PrescriptionExam prescription Exam} stored in the persistence system
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
            throw new DAOException("Impossible to count Prescription Exam", ex);
        }

        return count;
    }

    /**
     * Return the {@link PrescriptionExam prescription Exam} with the primary key equals to {@code primaryKey}
     *
     * @param primaryKey The primary key used to obtain the obj instance
     * @return The {@link PrescriptionExam prescription Exam} with {@code primaryKey}
     * @throws DAOException If an error occurred during the information retrieving
     */
    @Override
    public PrescriptionExam getByPrimaryKey(Long primaryKey) throws DAOException {
        PrescriptionExam prescriptionExam = null;
        if (primaryKey == null)
            throw new DAOException("Primary key is null");

        try (PreparedStatement pStmt = CONNECTION.prepareStatement(SQL_GET_BY_PRIMARY_KEY)) {
            pStmt.setLong(1, primaryKey);
            try (ResultSet rs = pStmt.executeQuery()) {
                if (rs.next()) {
                    prescriptionExam = setAndGetDAO(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get PrescriptionExam for the passed primary key", ex);
        }

        return prescriptionExam;
    }

    /**
     * Return a list of all {@link PrescriptionExam prescriptionExam} in the persistence system
     *
     * @return A list of all saved {@link PrescriptionExam prescriptionExam} in the persistence system
     * @throws DAOException If an error occurred during the information retrieving
     */
    @Override
    public List<PrescriptionExam> getAll() throws DAOException {
        List<PrescriptionExam> prescriptionExams = new ArrayList<>();

        try (Statement stmt = CONNECTION.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(SQL_GET_ALL)) {
                while (rs.next()) {
                    prescriptionExams.add(setAndGetDAO(rs));
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the list of Prescription Exam", ex);
        }

        return prescriptionExams;
    }

    @Override
    public List<PrescriptionExam> getAllByPersonId(Long personId) throws DAOException {
        List<PrescriptionExam> exams = new ArrayList<>();
        if (personId == null)
            throw new DAOException("Person id is mandatory");

        try (PreparedStatement pStmt = CONNECTION.prepareStatement(SQL_GET_ALL_BY_PERSON_ID)) {
            pStmt.setLong(1, personId);
            try (ResultSet rs = pStmt.executeQuery()) {
                while (rs.next()) {
                    exams.add(setAndGetDAO(rs));
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the List of Prescription Exams for the passed Person ID", ex);
        }

        return exams;
    }
}
