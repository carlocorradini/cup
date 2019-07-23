package it.unitn.disi.wp.cup.persistence.entity;

/**
 * Entity Medicine
 *
 * @author Carlo Corradini
 */
public class Medicine {
    private Long id;
    private String name;
    private Float price;

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
    public Float getPrice() {
        return price;
    }

    /**
     * Set the price of the Medicine
     *
     * @param price Medicine price
     */
    public void setPrice(Float price) {
        this.price = price;
    }
}
