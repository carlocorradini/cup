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

    private MedicineDAO medicineDAO = null;

    /**
     * Initialize the {@link MedicineDaoBean}
     */
    @PostConstruct
    public void init() {
        try {
            medicineDAO = DAOFactory.getDAOFactory().getDAO(MedicineDAO.class);
        } catch (DAOFactoryException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get DAOs", ex);
        }
    }

    /**
     * Return the {@link List List} of all available {@link Medicine Medicines}
     *
     * @return The {@link List List} of {@link Medicine Medicines}
     */
    public List<Medicine> getMedicines() {
        List<Medicine> medicines = Collections.emptyList();

        if (medicineDAO != null) {
            try {
                medicines = medicineDAO.getAll();
            } catch (DAOException ex) {
                LOGGER.log(Level.SEVERE, "Unable to get the List of all available Medicines", ex);
            }
        }

        return medicines;
    }
}
