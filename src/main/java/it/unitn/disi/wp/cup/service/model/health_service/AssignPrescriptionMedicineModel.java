package it.unitn.disi.wp.cup.service.model.health_service;

import it.unitn.disi.wp.cup.service.model.Model;
import it.unitn.disi.wp.cup.persistence.entity.Person;
import it.unitn.disi.wp.cup.persistence.entity.PrescriptionMedicine;

/**
 * Assign {@link PrescriptionMedicine} Model for assigning a {@link PrescriptionMedicine} to a {@link Person Person}
 *
 * @author Carlo Corradini
 */
public class AssignPrescriptionMedicineModel implements Model {
    private long prescriptionId;
    private boolean paid;

    AssignPrescriptionMedicineModel() {
        prescriptionId = 0L;
        paid = false;
    }

    /**
     * Set the {@link PrescriptionMedicine} id
     *
     * @param prescriptionId The {@link PrescriptionMedicine} id
     */
    public void setPrescriptionId(long prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    /**
     * Return the {@link PrescriptionMedicine} id
     *
     * @return The {@link PrescriptionMedicine} id
     */
    public long getPrescriptionId() {
        return prescriptionId;
    }

    /**
     * Return if the {@link PrescriptionMedicine Prescription Medicine} has been paid
     *
     * @return True if paid, false otherwise
     */
    public boolean isPaid() {
        return paid;
    }

    /**
     * Set if the {@link PrescriptionMedicine Prescription Medicine} has been paid
     *
     * @param paid True if paid, false otherwise
     */
    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    @Override
    public boolean isValid() {
        return prescriptionId != 0L && paid;
    }
}
