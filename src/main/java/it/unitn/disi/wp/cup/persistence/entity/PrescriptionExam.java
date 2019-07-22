package it.unitn.disi.wp.cup.persistence.entity;

import java.time.OffsetDateTime;

public class PrescriptionExam {
    private Long id;
    private Long personId;
    private Long doctorId;
    private Long specialistId;
    private OffsetDateTime date;
    private OffsetDateTime dateRegistration;
    private Exam exam;
    private Report report;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public Long getSpecialistId() {
        return specialistId;
    }

    public void setSpecialistId(Long specialistId) {
        this.specialistId = specialistId;
    }

    public OffsetDateTime getDate() {
        return date;
    }

    public void setDate(OffsetDateTime date) {
        this.date = date;
    }

    public OffsetDateTime getDateRegistration() {
        return dateRegistration;
    }

    public void setDateRegistration(OffsetDateTime dateRegistration) {
        this.dateRegistration = dateRegistration;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }
}
