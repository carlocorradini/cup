package it.unitn.disi.wp.cup.persistence.entity;

import java.time.OffsetDateTime;

/**
 * Entity Doctor Visit
 *
 * @author Carlo Corradini
 */
public class DoctorVisit {
    private Long personId;
    private Long doctorId;
    private OffsetDateTime date;
    private String comment;

    /**
     * Return the id of the Person
     *
     * @return Person id
     */
    public Long getPersonId() {
        return personId;
    }

    /**
     * Set the Person id
     *
     * @param personId The Person id
     */
    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    /**
     * Return the id of the Doctor
     *
     * @return Doctor id
     */
    public Long getDoctorId() {
        return doctorId;
    }

    /**
     * Set the Doctor id
     *
     * @param doctorId The Doctor id
     */
    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    /**
     * Return the {@link OffsetDateTime date} of the Visit
     *
     * @return Visit date
     */
    public OffsetDateTime getDate() {
        return date;
    }

    /**
     * Set the {@link OffsetDateTime date} of the visit
     *
     * @param date The Visit date
     */
    public void setDate(OffsetDateTime date) {
        this.date = date;
    }

    /**
     * Return the comment of the Doctor
     * If the {@code date} has not been reached {@code comment} must be null
     *
     * @return The Visit comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * Set the comment of the Visit
     * If the {@code date} has not been reached {@code comment} must be null
     *
     * @param comment The comment of the Visit
     */
    public void setComment(String comment) {
        if (date != null && date.isBefore(OffsetDateTime.now()))
            this.comment = comment;
        else this.comment = null;
    }

    /**
     * Check if the visit has been already made
     *
     * @return True if this visit has already made, false otherwise
     */
    public boolean isMade() {
        if (date != null)
            return date.isBefore(OffsetDateTime.now());
        else return false;
    }
}
