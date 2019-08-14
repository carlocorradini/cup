package it.unitn.disi.wp.cup.persistence.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Entity Prescription Exam
 *
 * @author Carlo Corradini
 */
public class PrescriptionExam {
    private Long id;
    private Long personId;
    private Long doctorId;
    private Long specialistId;
    private LocalDateTime dateTime;
    private LocalDateTime dateTimeRegistration;
    private Exam exam;
    private Report report;
    private Boolean paid;
    private Boolean read;

    /**
     * Return the id of the Prescription Exam
     *
     * @return Prescription Exam id
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the id of the Prescription Exam
     *
     * @param id Prescription Exam id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Return the {@link Person person} id of the Prescription Exam
     *
     * @return Prescription Exam {@link Person person} id
     */
    public Long getPersonId() {
        return personId;
    }

    /**
     * Set the {@link Person person} id of the Prescription Exam
     *
     * @param personId Prescription Exam {@link Person person} id
     */
    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    /**
     * Return the {@link Doctor doctor} id of the Prescription Exam
     *
     * @return Prescription Exam {@link Doctor doctor} id
     */
    public Long getDoctorId() {
        return doctorId;
    }

    /**
     * Set the {@link Doctor doctor} id of the Prescription Exam
     *
     * @param doctorId Prescription Exam {@link Doctor doctor} id
     */
    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    /**
     * Return the {@link DoctorSpecialist doctorSpecialist} id of the Prescription Exam
     *
     * @return Prescription Exam {@link DoctorSpecialist doctorSpecialist} id
     */
    public Long getSpecialistId() {
        return specialistId;
    }

    /**
     * Set the {@link DoctorSpecialist doctorSpecialist} id of the Prescription exam
     *
     * @param specialistId Prescription Exam {@link DoctorSpecialist doctorSpecialist} id
     */
    public void setSpecialistId(Long specialistId) {
        this.specialistId = specialistId;
    }

    /**
     * Return the Date and Time of the Prescription Exam
     *
     * @return Prescription Exam Date and Time
     */
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    /**
     * Return the {@code dateTime} as {@link LocalDate date}
     *
     * @return {@code dateTime} as {@link LocalDate date}
     */
    public LocalDate getDate() {
        if (dateTime != null)
            return dateTime.toLocalDate();
        return null;
    }

    /**
     * Return the {@code dateTime} as a {@link LocalTime time}
     *
     * @return {@code dateTime} as a {@link LocalTime time}
     */
    public LocalTime getTime() {
        if (dateTime != null)
            return dateTime.toLocalTime();
        return null;
    }

    /**
     * Set the Date and Time of the Prescription Exam
     *
     * @param dateTime Prescription Exam Date and Time
     */
    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * Return the Registration Date and Time of the Prescription Exam
     *
     * @return Prescription Exam Registration Date and Time
     */
    public LocalDateTime getDateTimeRegistration() {
        return dateTimeRegistration;
    }

    /**
     * Return the {@code dateTimeRegistration} as {@link LocalDate date}
     *
     * @return {@code dateTimeRegistration} as {@link LocalDate date}
     */
    public LocalDate getDateRegistration() {
        if (dateTimeRegistration != null)
            return dateTimeRegistration.toLocalDate();
        return null;
    }

    /**
     * Return the {@code dateTimeRegistration} as a {@link LocalTime time}
     *
     * @return {@code dateTimeRegistration} as a {@link LocalTime time}
     */
    public LocalTime getTimeRegistration() {
        if (dateTimeRegistration != null)
            return dateTimeRegistration.toLocalTime();
        return null;
    }

    /**
     * Set the Registration Date and Time of the Prescription Exam
     *
     * @param dateTimeRegistration Prescription Exam Registration Date and Time
     */
    public void setDateTimeRegistration(LocalDateTime dateTimeRegistration) {
        this.dateTimeRegistration = dateTimeRegistration;
    }

    /**
     * Return the {@link Exam exam} of the Prescription Exam
     *
     * @return Prescription Exam exam
     */
    public Exam getExam() {
        return exam;
    }

    /**
     * Set the {@link Exam exam} of the Prescription Exam
     *
     * @param exam Prescription Exam exam
     */
    public void setExam(Exam exam) {
        this.exam = exam;
    }

    /**
     * Return the {@link Report report} of the Prescription Exam
     *
     * @return Prescription Exam report
     */
    public Report getReport() {
        return report;
    }

    /**
     * Set the {@link Report report} of the Prescription Exam
     *
     * @param report Prescription Exam report
     */
    public void setReport(Report report) {
        this.report = report;
    }

    /**
     * Return if the {@link PrescriptionExam prescription} has been paid
     *
     * @return True if paid, false otherwise
     */
    public Boolean getPaid() {
        return paid;
    }

    /**
     * Set if the {@link PrescriptionExam prescription} has been paid
     *
     * @param paid True if paid, false otherwise
     */
    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    /**
     * Return if the {@link PrescriptionExam prescription} has been read
     *
     * @return True if read, false otherwise
     */
    public Boolean getRead() {
        return read;
    }

    /**
     * Set if the {@link PrescriptionExam prescription} has been read
     *
     * @param read True if read, false otherwise
     */
    public void setRead(Boolean read) {
        this.read = read;
    }
}
