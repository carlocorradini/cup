package it.unitn.disi.wp.cup.persistence.dao.jdbc;

import it.unitn.disi.wp.cup.persistence.dao.ExamDAO;
import it.unitn.disi.wp.cup.persistence.dao.MedicineDAO;
import it.unitn.disi.wp.cup.persistence.dao.ReportDAO;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOFactoryException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;
import it.unitn.disi.wp.cup.persistence.dao.factory.jdbc.JDBCDAO;
import it.unitn.disi.wp.cup.persistence.entity.Exam;
import it.unitn.disi.wp.cup.persistence.entity.Medicine;
import it.unitn.disi.wp.cup.persistence.entity.Report;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of {@link Report} interface
 *
 * @author Carlo Corradini
 */
public class JDBCReportDAO extends JDBCDAO<Report, Long> implements ReportDAO {
    private static final String SQL_GET_COUNT = "SELECT COUNT(*) FROM report";
    private static final String SQL_GET_BY_PRIMARY_KEY = "SELECT * FROM report WHERE id = ? LIMIT 1";
    private static final String SQL_GET_ALL = "SELECT * FROM report";
    private static final String SQL_GET_REPORT_EXAMS_BY_REPORT_ID = "SELECT * FROM report_exam WHERE report_id = ?";
    private static final String SQL_GET_REPORT_MEDICINES_BY_REPORT_ID = "SELECT * FROM report_medicine WHERE report_id = ?";

    /**
     * The default constructor of the class
     *
     * @param connection The Connection to the persistence system
     * @param daoFactory The DAOFactory to get DAOs
     */
    public JDBCReportDAO(Connection connection, DAOFactory daoFactory) {
        super(connection, daoFactory);
    }

    @Override
    public Report setAndGetDAO(ResultSet rs) throws DAOException {
        Report report;
        if (rs == null) throw new DAOException("ResultSet cannot be null");

        try {
            report = new Report();

            report.setId(rs.getLong("id"));
            report.setDateTime(rs.getObject("report_date", LocalDateTime.class));
            report.setContent(rs.getString("content"));
            report.setExams(getReportExamsByReportId(report.getId()));
            report.setMedicines(getReportMedicineByReportId(report.getId()));
        } catch (SQLException ex) {
            throw new DAOException("Impossible to set Report by ResultSet", ex);
        }
        return report;
    }

    /**
     * Return the number of {@link Report reports} stored in the persistence system
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
            throw new DAOException("Impossible to count Report", ex);
        }

        return count;
    }

    /**
     * Return the {@link Report report} with the primary key equals to {@code primaryKey}
     *
     * @param primaryKey The primary key used to obtain the obj instance
     * @return The {@link Report report} with {@code primaryKey}
     * @throws DAOException If an error occurred during the information retrieving
     */
    @Override
    public Report getByPrimaryKey(Long primaryKey) throws DAOException {
        Report report = null;
        if (primaryKey == null)
            throw new DAOException("Primary key is null");

        try (PreparedStatement pStmt = CONNECTION.prepareStatement(SQL_GET_BY_PRIMARY_KEY)) {
            pStmt.setLong(1, primaryKey);
            try (ResultSet rs = pStmt.executeQuery()) {
                if (rs.next()) {
                    report = setAndGetDAO(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get Report for the passed primary key", ex);
        }

        return report;
    }

    /**
     * Return a list of all {@link Report report} in the persistence system
     *
     * @return A list of all saved {@link Report report} in the persistence system
     * @throws DAOException If an error occurred during the information retrieving
     */
    @Override
    public List<Report> getAll() throws DAOException {
        List<Report> reports = new ArrayList<>();

        try (Statement stmt = CONNECTION.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(SQL_GET_ALL)) {
                while (rs.next()) {
                    reports.add(setAndGetDAO(rs));
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the list of Report", ex);
        }

        return reports;
    }

    /**
     * Return the List of suggested {@link Exam exams} for the {@link Report report} with {@code reportId id}
     *
     * @param reportId The {@link Report report} id
     * @return The List of {@link Exam exams}
     * @throws DAOException If an error occurred during the information retrieving
     */
    private List<Exam> getReportExamsByReportId(Long reportId) throws DAOException {
        List<Exam> exams = new ArrayList<>();
        ExamDAO examDAO;
        if (reportId == null)
            throw new DAOException("Report id is mandatory");

        try (PreparedStatement pStmt = CONNECTION.prepareStatement(SQL_GET_REPORT_EXAMS_BY_REPORT_ID)) {
            examDAO = DAO_FACTORY.getDAO(ExamDAO.class);
            pStmt.setLong(1, reportId);

            try (ResultSet rs = pStmt.executeQuery()) {
                if (rs.next()) {
                    exams.add(examDAO.getByPrimaryKey(rs.getLong("exam_id")));
                }
            }
        } catch (SQLException | DAOFactoryException ex) {
            throw new DAOException("Impossible to get the List of Exams for Report");
        }

        return exams;
    }

    /**
     * Return the List of suggested {@link Medicine medicines} for the {@link Report report} with {@code reportId id}
     *
     * @param reportId The {@link Report report} id
     * @return The List of {@link Medicine medicines}
     * @throws DAOException If an error occurred during the information retrieving
     */
    private List<Medicine> getReportMedicineByReportId(Long reportId) throws DAOException {
        List<Medicine> medicines = new ArrayList<>();
        MedicineDAO medicineDAO;
        if (reportId == null)
            throw new DAOException("Report id is mandatory");

        try (PreparedStatement pStmt = CONNECTION.prepareStatement(SQL_GET_REPORT_MEDICINES_BY_REPORT_ID)) {
            medicineDAO = DAO_FACTORY.getDAO(MedicineDAO.class);
            pStmt.setLong(1, reportId);

            try (ResultSet rs = pStmt.executeQuery()) {
                if (rs.next()) {
                    medicines.add(medicineDAO.getByPrimaryKey(rs.getLong("medicine_id")));
                }
            }
        } catch (SQLException | DAOFactoryException ex) {
            throw new DAOException("Impossible to get the List of Medicines for Report");
        }

        return medicines;
    }
}
