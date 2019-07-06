package it.unitn.disi.wp.cup.persistence.entity;

import it.unitn.disi.wp.cup.util.StringUtil;

/**
 * Entity Region
 *
 * @author Carlo Corradini
 */
public class Region {
    private Long id;
    private String name;

    /**
     * Return the id of the Region
     *
     * @return Region id
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the Region id
     *
     * @param id Region id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Return the name of the Region
     *
     * @return Region name
     */
    public String getName() {
        return name;
    }

    /**
     * Return the name of the Region capitalized
     *
     * @return Region name capitalized
     */
    public String getNameCapitalized() {
        return StringUtil.capitalize(name);
    }

    /**
     * Set the Region name
     * *
     *
     * @param name Region name
     */
    public void setName(String name) {
        this.name = name;
    }
}
