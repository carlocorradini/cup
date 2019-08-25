package it.unitn.disi.wp.cup.persistence.entity;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Entity Report
 *
 * @author Carlo Corradini
 */
public class Report {
    private Long id;
    private LocalDateTime dateTime;
    private String content;
    private List<Exam> exams;
    private List<Medicine> medicines;

    /**
     * Return the id of the Report
     *
     * @return Report id
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the id of the Report
     *
     * @param id Report id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Return the Date and Time of the Report
     *
     * @return Report Date and Time
     */
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    /**
     * Set the Date and Time of the Report
     *
     * @param dateTime Report Date and Time
     */
    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * Return the content of the Report
     *
     * @return Report content
     */
    public String getContent() {
        return content;
    }

    /**
     * Set the content of the Report
     *
     * @param content Report content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Return the suggested exams of the Report
     *
     * @return Suggested exams of the Report
     */
    public List<Exam> getExams() {
        return exams;
    }

    /**
     * Set the suggested exams of the Report
     *
     * @param exams Suggested exams of the Report
     */
    public void setExams(List<Exam> exams) {
        this.exams = exams;
    }

    /**
     * Return the suggested medicines of the Report
     *
     * @return Suggested medicines of the Report
     */
    public List<Medicine> getMedicines() {
        return medicines;
    }

    /**
     * Set the suggested medicines of the Report
     *
     * @param medicines Suggested medicines of the Report
     */
    public void setMedicines(List<Medicine> medicines) {
        this.medicines = medicines;
    }
}
