package it.unitn.disi.wp.cup.model.prescription;

/**
 * Prescription Exam Model
 *
 * @author Carlo Corradini
 */
public class PrescriptionExamModel implements Prescription {
    private long patientId;
    private long examId;

    PrescriptionExamModel() {
        patientId = 0L;
        examId = 0L;
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
     * Return the Exam id
     *
     * @return Exam id
     */
    public long getExamId() {
        return examId;
    }

    /**
     * Set the Exam id
     *
     * @param examId Exam id
     */
    public void setExamId(long examId) {
        this.examId = examId;
    }

    @Override
    public boolean isValid() {
        return patientId != 0L && examId != 0L;
    }
}
