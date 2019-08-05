package it.unitn.disi.wp.cup.model.prescription;

import it.unitn.disi.wp.cup.config.PrescriptionConfig;

public class PrescriptionMedicineModel implements Prescription {
    private long patientId;
    private long medicineId;
    private short quantity;

    PrescriptionMedicineModel() {
        patientId = 0L;
        medicineId = 0L;
        quantity = 0;
    }

    public long getPatientId() {
        return patientId;
    }

    public void setPatientId(long patientId) {
        this.patientId = patientId;
    }

    public long getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(long medicineId) {
        this.medicineId = medicineId;
    }

    public short getQuantity() {
        return quantity;
    }

    public void setQuantity(short quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean isValid() {
        return patientId != 0L && medicineId != 0L && quantity != 0
                && quantity >= PrescriptionConfig.getMedicineMinQuantity() && quantity <= PrescriptionConfig.getMedicineMaxQuantity();
    }
}
