package it.unitn.disi.wp.cup.persistence.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;

/**
 * Entity Prescription Medicine
 *
 * @author Carlo Corradini
 */
public class PrescriptionMedicine {
    private Long id;
    private Long personId;
    private Long DoctorId;
    private OffsetDateTime dateTime;
    private Medicine medicine;
    private Short quantity;

    /**
     * Return the id of the Prescription Medicine
     *
     * @return Prescription Medicine id
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the Prescription Medicine id
     *
     * @param id Prescription Medicine id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Return the {@link Person person} id of the Prescription Medicine
     *
     * @return Prescription Medicine {@link Person person} id
     */
    public Long getPersonId() {
        return personId;
    }

    /**
     * Set the {@link Person person} id of the Prescription Medicine
     *
     * @param personId Prescription Medicine {@link Person person} id
     */
    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    /**
     * Return the {@link Doctor doctor} id of the Prescription Medicine
     *
     * @return Prescription Medicine {@link Doctor doctor} id
     */
    public Long getDoctorId() {
        return DoctorId;
    }

    /**
     * Set the {@link Doctor doctor} id of the Prescription Medicine
     *
     * @param doctorId Prescription Medicine {@link Doctor doctor} id
     */
    public void setDoctorId(Long doctorId) {
        DoctorId = doctorId;
    }

    /**
     * Return the Date and Time of the Prescription Medicine
     *
     * @return Prescription Medicine Date and Time
     */
    public OffsetDateTime getDateTime() {
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
     * Set the Date and Time of the Prescription Medicine
     *
     * @param dateTime Prescription Medicine Date and Time
     */
    public void setDateTime(OffsetDateTime dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * Return the medicine of the Prescription Exam
     *
     * @return Prescription Exam Medicine
     */
    public Medicine getMedicine() {
        return medicine;
    }

    /**
     * Set the medicine of the Prescription Exam
     *
     * @param medicine Prescription Exam Medicine
     */
    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }

    /**
     * Return the quantity of the Medicine
     *
     * @return Prescription Exam quantity
     */
    public Short getQuantity() {
        return quantity;
    }

    /**
     * Set the quantity of the Prescription Exam
     *
     * @param quantity Prescription Exam quantity
     */
    public void setQuantity(Short quantity) {
        this.quantity = quantity;
    }
}
