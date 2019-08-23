package it.unitn.disi.wp.cup.model.prescription;

import it.unitn.disi.wp.cup.config.PrescriptionConfig;
import it.unitn.disi.wp.cup.persistence.entity.PrescriptionExam;
import it.unitn.disi.wp.cup.persistence.entity.Report;
import it.unitn.disi.wp.cup.persistence.entity.Exam;
import it.unitn.disi.wp.cup.persistence.entity.Medicine;

import java.util.Collections;
import java.util.List;

/**
 * Prescription Report Model
 *
 * @author Carlo Corradini
 */
public class PrescriptionReportModel implements PrescriptionModel {
    private long prescriptionId;
    private String content;
    private boolean paid;
    private List<Long> exams;
    private List<Long> medicines;

    PrescriptionReportModel() {
        prescriptionId = 0L;
        content = "";
        paid = false;
        exams = Collections.emptyList();
        medicines = Collections.emptyList();
    }

    /**
     * Return the {@link PrescriptionExam Prescription Exam} id
     *
     * @return {@link PrescriptionExam Prescription Exam} id
     */
    public long getPrescriptionId() {
        return prescriptionId;
    }

    /**
     * Set the {@link PrescriptionExam Prescription Exam} id
     *
     * @param prescriptionId {@link PrescriptionExam Prescription Exam} id
     */
    public void setPrescriptionId(long prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    /**
     * Return the content of the {@link Report report}
     *
     * @return {@link Report report} content
     */
    public String getContent() {
        return content;
    }

    /**
     * Set the {@link Report report} content
     *
     * @param content The {@link Report content}
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Return true if the {@link PrescriptionExam Prescription Exam} has been paid, false otherwise
     *
     * @return If is paid or not
     */
    public boolean isPaid() {
        return paid;
    }

    /**
     * Set true if the {@link PrescriptionExam Prescription Exam} has been paid, false otherwise
     *
     * @param paid If is paid or not
     */
    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    /**
     * Return the {@link List list} of suggested {@link Exam exams} id.
     * This {@link List list} can be empty if no {@link Exam exams} are suggested.
     *
     * @return The {@link List list} of suggested {@link Exam exams} id
     */
    public List<Long> getExams() {
        return exams;
    }

    /**
     * Set the {@link List list} of suggested {@link Exam exams} id.
     *
     * @param exams The {@link List list} of suggested {@link Exam exams} id
     */
    public void setExams(List<Long> exams) {
        this.exams = exams;
    }

    /**
     * Return the {@link List list} of suggested {@link Medicine medicines} id.
     * This {@link List list} can be empty if no {@link Medicine medicines} are suggested.
     *
     * @return The {@link List list} of suggested {@link Medicine medicines} id
     */
    public List<Long> getMedicines() {
        return medicines;
    }

    /**
     * Set the {@link List list} of suggested {@link Medicine medicines} id.
     *
     * @param medicines The {@link List list} of suggested {@link Medicine medicines} id
     */
    public void setMedicines(List<Long> medicines) {
        this.medicines = medicines;
    }

    @Override
    public boolean isValid() {
        return prescriptionId != 0L && !content.isEmpty()
                && paid
                && content.length() >= PrescriptionConfig.getExamReportContentMinLength()
                && content.length() <= PrescriptionConfig.getExamReportContentMaxLength();
    }
}
