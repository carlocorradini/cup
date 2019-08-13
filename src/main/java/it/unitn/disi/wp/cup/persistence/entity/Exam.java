package it.unitn.disi.wp.cup.persistence.entity;

/**
 * Entity Exam
 *
 * @author Carlo Corradini
 */
public class Exam {
    private Long id;
    private String name;
    private Short price;

    /**
     * Return the id of the Exam
     *
     * @return Exam id
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the Exam id
     *
     * @param id Exam id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Return the name of the Exam
     *
     * @return Exam name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the Exam
     *
     * @param name Exam name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Return the price of the Exam
     *
     * @return Exam price
     */
    public Short getPrice() {
        return price;
    }

    /**
     * Set the price of the Exam
     *
     * @param price Exam price
     */
    public void setPrice(Short price) {
        this.price = price;
    }
}
