package it.unitn.disi.wp.cup.persistence.dao.jdbc;

import it.unitn.disi.wp.cup.persistence.dao.ExamDAO;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;
import it.unitn.disi.wp.cup.persistence.dao.factory.jdbc.JDBCDAO;
import it.unitn.disi.wp.cup.persistence.entity.Exam;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of {@link Exam} interface
 *
 * @author Carlo Corradini
 */
public class JDBCExamDAO extends JDBCDAO<Exam, Long> implements ExamDAO {

    private static final String SQL_GET_COUNT = "SELECT COUNT(*) FROM exam";
    private static final String SQL_GET_BY_PRIMARY_KEY = "SELECT * FROM exam WHERE id = ? LIMIT 1";
    private static final String SQL_GET_ALL = "SELECT * FROM exam";

    /**
     * The default constructor of the class
     *
     * @param connection The Connection to the persistence system
     * @param daoFactory The DAOFactory to get DAOs
     */
    public JDBCExamDAO(Connection connection, DAOFactory daoFactory) {
        super(connection, daoFactory);
    }

    @Override
    public Exam setAndGetDAO(ResultSet rs) throws DAOException {
        Exam exam;
        if (rs == null) throw new DAOException("ResultSet cannot be null");

        try {
            exam = new Exam();

            exam.setId(rs.getLong("id"));
            exam.setName(rs.getString("name"));
            exam.setPrice(rs.getFloat("price"));
        } catch (SQLException ex) {
            throw new DAOException("Impossible to set Exam by ResultSet", ex);
        }
        return exam;
    }

    /**
     * Return the number of {@link Exam exams} stored in the persistence system
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
            throw new DAOException("Impossible to count Exam", ex);
        }

        return count;
    }

    /**
     * Return the {@link Exam exam} with the primary key equals to {@code primaryKey}
     *
     * @param primaryKey The primary key used to obtain the obj instance
     * @return The {@link Exam exam} with {@code primaryKey}
     * @throws DAOException If an error occurred during the information retrieving
     */
    @Override
    public Exam getByPrimaryKey(Long primaryKey) throws DAOException {
        Exam exam = null;
        if (primaryKey == null)
            throw new DAOException("Primary key is null");

        try (PreparedStatement pStmt = CONNECTION.prepareStatement(SQL_GET_BY_PRIMARY_KEY)) {
            pStmt.setLong(1, primaryKey);
            try (ResultSet rs = pStmt.executeQuery()) {
                if (rs.next()) {
                    exam = setAndGetDAO(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get Exam for the passed primary key", ex);
        }

        return exam;
    }

    /**
     * Return a list of all {@link Exam exams} in the persistence system
     *
     * @return A list of all saved {@link Exam exams} in the persistence system
     * @throws DAOException If an error occurred during the information retrieving
     */
    @Override
    public List<Exam> getAll() throws DAOException {
        List<Exam> exams = new ArrayList<>();

        try (Statement stmt = CONNECTION.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(SQL_GET_ALL)) {
                while (rs.next()) {
                    exams.add(setAndGetDAO(rs));
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the list of Exams", ex);
        }

        return exams;
    }
}
