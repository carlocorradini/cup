package it.unitn.disi.wp.cup.bean.dao;

import it.unitn.disi.wp.cup.persistence.dao.MedicineDAO;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOException;
import it.unitn.disi.wp.cup.persistence.dao.exception.DAOFactoryException;
import it.unitn.disi.wp.cup.persistence.dao.factory.DAOFactory;
import it.unitn.disi.wp.cup.persistence.entity.Medicine;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Medicine Bean
 *
 * @author Carlo Corradini
 * @see Medicine
 */
@Named("medicine")
@RequestScoped
public final class MedicineDaoBean implements Serializable {
    private static final long serialVersionUID = 2028930324705062207L;
    private static final Logger LOGGER = Logger.getLogger(MedicineDaoBean.class.getName());
    private List<Medicine> medicines = Collections.emptyList();

    /**
     * Initialize the {@link MedicineDaoBean}
     */
    @PostConstruct
    public void init() {
        MedicineDAO medicineDAO;
        try {
            medicineDAO = DAOFactory.getDAOFactory().getDAO(MedicineDAO.class);
            medicines = medicineDAO.getAll();
        } catch (DAOFactoryException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get Medicine DAO", ex);
        } catch (DAOException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get the list of Medicines", ex);
        }
    }

    /**
     * Return the List of Medicines available for the prescription
     *
     * @return The List of Medicines
     */
    public List<Medicine> getMedicines() {
        return medicines;
    }
}
