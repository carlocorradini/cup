package it.unitn.disi.wp.cup.persistence.entity;

import it.unitn.disi.wp.cup.util.StringUtil;

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

    @Override
    public boolean equals(Object obj) {
        Province province;
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        province = (Province) obj;
        return id.equals(province.id);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        return prime + ((id == null) ? 0 : id.hashCode());
    }

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
     * Return the name long of the Province capitalized
     *
     * @return Province name long capitalized
     */
    public String getNameLongCapitalized() {
        return StringUtil.capitalize(nameLong);
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
     * The name short is always in Upper Case
     *
     * @return Province name short
     */
    public String getNameShort() {
        return nameShort.toUpperCase();
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
