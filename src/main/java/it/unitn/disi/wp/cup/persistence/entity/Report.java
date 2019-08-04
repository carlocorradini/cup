package it.unitn.disi.wp.cup.persistence.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    public List<Exam> getExams() {
        return exams;
    }

    public void setExams(List<Exam> exams) {
        this.exams = exams;
    }

    public List<Medicine> getMedicines() {
        return medicines;
    }

    public void setMedicines(List<Medicine> medicines) {
        this.medicines = medicines;
    }
}
