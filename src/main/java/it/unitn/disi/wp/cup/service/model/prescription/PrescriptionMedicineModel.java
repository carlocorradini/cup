package it.unitn.disi.wp.cup.service.model.prescription;

import it.unitn.disi.wp.cup.config.PrescriptionConfig;
import it.unitn.disi.wp.cup.service.model.Model;

/**
 * Prescription Medicine Model
 *
 * @author Carlo Corradini
 */
public class PrescriptionMedicineModel implements Model {
    private long patientId;
    private long medicineId;
    private short quantity;

    PrescriptionMedicineModel() {
        patientId = 0L;
        medicineId = 0L;
        quantity = 0;
    }

    /**
     * Return the Patient id
     *
     * @return patient id
     */
    public long getPatientId() {
        return patientId;
    }

    /**
     * Set the Patient id
     *
     * @param patientId Patient id
     */
    public void setPatientId(long patientId) {
        this.patientId = patientId;
    }

    /**
     * Return the Medicine id
     *
     * @return Medicine id
     */
    public long getMedicineId() {
        return medicineId;
    }

    /**
     * Set the Medicine id
     *
     * @param medicineId Medicine id
     */
    public void setMedicineId(long medicineId) {
        this.medicineId = medicineId;
    }

    /**
     * Return the Prescription Medicine quantity
     *
     * @return Prescription Medicine quantity
     */
    public short getQuantity() {
        return quantity;
    }

    /**
     * Set the Prescription Medicine quantity
     *
     * @param quantity Prescription Medicine quantity
     */
    public void setQuantity(short quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean isValid() {
        return patientId != 0L && medicineId != 0L && quantity != 0
                && quantity >= PrescriptionConfig.getMedicineMinQuantity() && quantity <= PrescriptionConfig.getMedicineMaxQuantity();
    }
}
