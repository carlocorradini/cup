package it.unitn.disi.wp.cup.persistence.dao.jdbc;

import it.unitn.disi.wp.cup.persistence.dao.MedicineDAO;
import it.unitn.disi.wp.cup.persistence.dao.PrescriptionMedicineDAO;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOFactoryException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;
import it.unitn.disi.wp.cup.persistence.dao.factory.jdbc.JDBCDAO;
import it.unitn.disi.wp.cup.persistence.entity.PrescriptionMedicine;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of {@link PrescriptionMedicine} interface
 *
 * @author Carlo Corradini
 */
public class JDBCPrescriptionMedicineDAO extends JDBCDAO<PrescriptionMedicine, Long> implements PrescriptionMedicineDAO {
    private static final String SQL_GET_COUNT = "SELECT COUNT(*) FROM prescription_medicine";
    private static final String SQL_GET_BY_PRIMARY_KEY = "SELECT * FROM prescription_medicine WHERE id = ? LIMIT 1";
    private static final String SQL_GET_ALL = "SELECT * FROM prescription_medicine";
    private static final String SQL_GET_ALL_BY_PERSON_ID = "SELECT * FROM prescription_medicine WHERE person_id = ? ORDER BY prescription_date DESC";
    private static final String SQL_ADD = "INSERT INTO prescription_medicine(person_id, doctor_id, medicine_id, quantity) VALUES (?, ?, ?, ?)";
    private static final String SQL_GET_COUNT_BY_PERSON_ID = "SELECT COUNT(*) FROM prescription_medicine WHERE person_id = ?";
    private static final String SQL_GET_COUNT_BY_DOCTOR_ID = "SELECT COUNT(*) FROM prescription_medicine WHERE doctor_id = ?";

    /**
     * The default constructor of the class
     *
     * @param connection The Connection to the persistence system
     * @param daoFactory The DAOFactory to get DAOs
     */
    public JDBCPrescriptionMedicineDAO(Connection connection, DAOFactory daoFactory) {
        super(connection, daoFactory);
    }

    @Override
    public PrescriptionMedicine setAndGetDAO(ResultSet rs) throws DAOException {
        PrescriptionMedicine prescriptionMedicine;
        MedicineDAO medicineDAO;
        if (rs == null) throw new DAOException("ResultSet cannot be null");

        try {
            medicineDAO = DAO_FACTORY.getDAO(MedicineDAO.class);

            prescriptionMedicine = new PrescriptionMedicine();

            prescriptionMedicine.setId(rs.getLong("id"));
            prescriptionMedicine.setPersonId(rs.getLong("person_id"));
            prescriptionMedicine.setDoctorId(rs.getLong("doctor_id"));
            prescriptionMedicine.setDateTime(rs.getObject("prescription_date", LocalDateTime.class));
            prescriptionMedicine.setMedicine(medicineDAO.getByPrimaryKey(rs.getLong("medicine_id")));
            prescriptionMedicine.setQuantity(rs.getShort("quantity"));
        } catch (SQLException | DAOFactoryException ex) {
            throw new DAOException("Impossible to set Prescription Medicine by ResultSet", ex);
        }
        return prescriptionMedicine;
    }

    /**
     * Return the number of {@link PrescriptionMedicine prescription Medicine} stored in the persistence system
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
            throw new DAOException("Impossible to count Prescription Medicine", ex);
        }

        return count;
    }

    /**
     * Return the {@link PrescriptionMedicine prescription Medicine} with the primary key equals to {@code primaryKey}
     *
     * @param primaryKey The primary key used to obtain the obj instance
     * @return The {@link PrescriptionMedicine prescription Medicine} with {@code primaryKey}
     * @throws DAOException If an error occurred during the information retrieving
     */
    @Override
    public PrescriptionMedicine getByPrimaryKey(Long primaryKey) throws DAOException {
        PrescriptionMedicine prescriptionMedicine = null;
        if (primaryKey == null)
            throw new DAOException("Primary key is null");

        try (PreparedStatement pStmt = CONNECTION.prepareStatement(SQL_GET_BY_PRIMARY_KEY)) {
            pStmt.setLong(1, primaryKey);
            try (ResultSet rs = pStmt.executeQuery()) {
                if (rs.next()) {
                    prescriptionMedicine = setAndGetDAO(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get PrescriptionMedicine for the passed primary key", ex);
        }

        return prescriptionMedicine;
    }

    /**
     * Return a list of all {@link PrescriptionMedicine prescriptionMedicine} in the persistence system
     *
     * @return A list of all saved {@link PrescriptionMedicine prescriptionMedicine} in the persistence system
     * @throws DAOException If an error occurred during the information retrieving
     */
    @Override
    public List<PrescriptionMedicine> getAll() throws DAOException {
        List<PrescriptionMedicine> prescriptionMedicines = new ArrayList<>();

        try (Statement stmt = CONNECTION.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(SQL_GET_ALL)) {
                while (rs.next()) {
                    prescriptionMedicines.add(setAndGetDAO(rs));
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the list of Prescription Medicines", ex);
        }

        return prescriptionMedicines;
    }

    @Override
    public List<PrescriptionMedicine> getAllByPersonId(Long personId) throws DAOException {
        List<PrescriptionMedicine> medicines = new ArrayList<>();
        if (personId == null)
            throw new DAOException("Person id is mandatory");

        try (PreparedStatement pStmt = CONNECTION.prepareStatement(SQL_GET_ALL_BY_PERSON_ID)) {
            pStmt.setLong(1, personId);
            try (ResultSet rs = pStmt.executeQuery()) {
                while (rs.next()) {
                    medicines.add(setAndGetDAO(rs));
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to get the List of Prescription Medicines for the passed Person ID", ex);
        }

        return medicines;
    }

    @Override
    public Long add(PrescriptionMedicine prescriptionMedicine) throws DAOException {
        Long id = null;
        if (prescriptionMedicine == null)
            throw new DAOException("Prescription Medicine is mandatory", new NullPointerException());

        try (PreparedStatement pStmt = CONNECTION.prepareStatement(SQL_ADD, Statement.RETURN_GENERATED_KEYS)) {
            pStmt.setLong(1, prescriptionMedicine.getPersonId());
            pStmt.setLong(2, prescriptionMedicine.getDoctorId());
            pStmt.setLong(3, prescriptionMedicine.getMedicine().getId());
            pStmt.setShort(4, prescriptionMedicine.getQuantity());

            if (pStmt.executeUpdate() == 1) {
                ResultSet rs = pStmt.getGeneratedKeys();
                if (rs.next()) {
                    id = rs.getLong(1);
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to add the Prescription Medicine", ex);
        }

        return id;
    }

    @Override
    public Long getCountByPersonId(Long personId) throws DAOException {
        Long count = null;
        if (personId == null)
            throw new DAOException("Person id is mandatory", new NullPointerException());

        try (PreparedStatement pStmt = CONNECTION.prepareStatement(SQL_GET_COUNT_BY_PERSON_ID)) {
            pStmt.setLong(1, personId);
            try (ResultSet rs = pStmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getLong(1);
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to count the Prescription Medicine by Person Id");
        }

        return count;
    }

    @Override
    public Long getCountByDoctorId(Long doctorId) throws DAOException {
        Long count = null;
        if (doctorId == null)
            throw new DAOException("Doctor id is mandatory", new NullPointerException());

        try (PreparedStatement pStmt = CONNECTION.prepareStatement(SQL_GET_COUNT_BY_DOCTOR_ID)) {
            pStmt.setLong(1, doctorId);
            try (ResultSet rs = pStmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getLong(1);
                }
            }
        } catch (SQLException ex) {
            throw new DAOException("Impossible to count the Prescription Medicine by Doctor Id");
        }

        return count;
    }
}
