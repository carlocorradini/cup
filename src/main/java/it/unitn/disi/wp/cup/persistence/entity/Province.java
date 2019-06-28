package it.unitn.disi.wp.cup.persistence.entity;

/**
 * Entity Province
 *
 * @author Carlo Corradini
 */
public class Province {
    private Long id;
    private String nameLong;
    private String nameShort;
    private Region region;

    /**
     * Return the id of the Province
     *
     * @return Province id
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the Province id
     *
     * @param id Province id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Return the name long of the Province
     *
     * @return Province name long
     */
    public String getNameLong() {
        return nameLong;
    }

    /**
     * Set the Province name long
     *
     * @param nameLong Province name long
     */
    public void setNameLong(String nameLong) {
        this.nameLong = nameLong;
    }

    /**
     * Return the name short of the Province
     *
     * @return Province name short
     */
    public String getNameShort() {
        return nameShort;
    }

    /**
     * Set the Province name short
     *
     * @param nameShort Province name short
     */
    public void setNameShort(String nameShort) {
        this.nameShort = nameShort;
    }

    /**
     * Return the Region of the Province
     *
     * @return Province Region
     */
    public Region getRegion() {
        return region;
    }

    /**
     * Set the Province region
     *
     * @param region Province region
     */
    public void setRegion(Region region) {
        this.region = region;
    }
}
