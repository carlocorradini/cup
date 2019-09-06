package it.unitn.disi.wp.cup.persistence.entity;

/**
 * Entity Medicine
 *
 * @author Carlo Corradini
 */
public class Medicine implements Cloneable {
    private Long id;
    private String name;
    private Short price;
    private String description;

    @Override
    public Object clone() {
        Medicine medicine;

        try {
            medicine = (Medicine) super.clone();
        } catch (CloneNotSupportedException ex) {
            medicine = new Medicine();
        }

        medicine.id = this.getId();
        medicine.name = this.getName();
        medicine.price = this.getPrice();
        medicine.description = this.getDescription();

        return medicine;
    }

    /**
     * Return the id of the Medicine
     *
     * @return Medicine id
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the id of the Medicine
     *
     * @param id Medicine id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Return the name of the Medicine
     *
     * @return Medicine name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the Medicine
     *
     * @param name Medicine name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Return the price of the Medicine
     *
     * @return Medicine price
     */
    public Short getPrice() {
        return price;
    }

    /**
     * Set the price of the Medicine
     *
     * @param price Medicine price
     */
    public void setPrice(Short price) {
        this.price = price;
    }

    /**
     * Return the description of the Medicine
     *
     * @return Medicine description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description of the Medicine
     *
     * @param description Medicine description
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
