package it.unitn.disi.wp.cup.service.model.health_service;

import it.unitn.disi.wp.cup.service.model.Model;
import it.unitn.disi.wp.cup.service.model.obj.DateTimeModel;
import it.unitn.disi.wp.cup.persistence.entity.PrescriptionExam;
import it.unitn.disi.wp.cup.persistence.entity.Exam;
import it.unitn.disi.wp.cup.persistence.entity.DoctorSpecialist;
import it.unitn.disi.wp.cup.persistence.entity.HealthService;

/**
 * Assign {@link PrescriptionExam} Model for assigning a {@link PrescriptionExam} to an executor
 *
 * @author Carlo Corradini
 */
public class AssignPrescriptionExamModel implements Model {
    private long prescriptionId;
    private long executorId;
    private DateTimeModel dateTime;

    AssignPrescriptionExamModel() {
        prescriptionId = 0L;
        executorId = 0L;
        dateTime = null;
    }

    /**
     * Set the {@link PrescriptionExam} id
     *
     * @param prescriptionId The {@link PrescriptionExam} id
     */
    public void setPrescriptionId(long prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    /**
     * Return the {@link PrescriptionExam} id
     *
     * @return The {@link PrescriptionExam} id
     */
    public long getPrescriptionId() {
        return prescriptionId;
    }

    /**
     * Set the Executor id
     * If the {@link Exam} is supported this must be a valid {@link DoctorSpecialist} id
     * else if it's not supported this must be a valid {@link HealthService} id equals to the current authenticated
     * {@link HealthService Health Service}
     *
     * @param executorId The Executor id
     */
    public void setExecutorId(long executorId) {
        this.executorId = executorId;
    }

    /**
     * Return the executor id
     *
     * @return The Executor id
     */
    public long getExecutorId() {
        return executorId;
    }

    /**
     * Set the {@link DateTimeModel Date Time Model} of the {@link PrescriptionExam Prescription Exam}
     *
     * @param dateTime The {@link DateTimeModel Date Time Model} of the {@link PrescriptionExam Prescription Exam}
     */
    public void setDateTime(DateTimeModel dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * Return the {@link DateTimeModel Date Time Model} of the {@link PrescriptionExam Prescription Exam}
     *
     * @return The {@link DateTimeModel Date Time Model} of the {@link PrescriptionExam Prescription Exam}
     */
    public DateTimeModel getDateTime() {
        return dateTime;
    }

    @Override
    public boolean isValid() {
        return prescriptionId != 0L && executorId != 0L && dateTime != null;
    }
}
