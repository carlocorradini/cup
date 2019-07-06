package it.unitn.disi.wp.cup.persistence.entity;

import it.unitn.disi.wp.cup.util.StringUtil;

/**
 * Entity City
 *
 * @author Carlo Corradini
 */
public class City {
    private Long id;
    private String name;
    private Province province;

    /**
     * Return the id of the City
     *
     * @return City id
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the City id
     *
     * @param id City id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Return the name of the City
     *
     * @return City name
     */
    public String getName() {
        return name;
    }

    /**
     * Return the name of the City capitalized
     *
     * @return City name capitalized
     */
    public String getNameCapitalized() {
        return StringUtil.capitalize(name);
    }

    /**
     * Set the City name
     *
     * @param name City name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Return the Province of the City
     *
     * @return City Province
     */
    public Province getProvince() {
        return province;
    }

    /**
     * Set the City Province
     *
     * @param province City Province
     */
    public void setProvince(Province province) {
        this.province = province;
    }
}
