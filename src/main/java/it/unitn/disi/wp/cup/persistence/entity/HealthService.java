package it.unitn.disi.wp.cup.persistence.entity;

import it.unitn.disi.wp.cup.config.AppConfig;

/**
 * Entity Health Service
 *
 * @author Carlo Corradini
 */
public class HealthService {
    private Long id;
    private String password;
    private String email;
    private String crest;
    private Province province;

    @Override
    public boolean equals(Object obj) {
        HealthService healthService;
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        healthService = (HealthService) obj;
        return id.equals(healthService.id);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        return prime + ((id == null) ? 0 : id.hashCode());
    }

    /**
     * Return the id of the Health Service
     *
     * @return Health Service id
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the id of the Health Service
     *
     * @param id Health Service id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Return the password of the Health Service
     *
     * @return Health Service password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the password of the Health Service
     *
     * @param password Health Service password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Return the email of the Health service
     *
     * @return Health Service email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the email of the Health Service
     *
     * @param email Health Service email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Return the crest of the Health Service
     *
     * @return Health Service crest
     */
    public String getCrest() {
        return crest;
    }

    /**
     * Set the crest of the Health Service
     *
     * @param crest Health Service crest
     */
    public void setCrest(String crest) {
        this.crest = crest;
    }

    /**
     * Return the name of the {@code crest} for resource usage
     *
     * @return The crest as Resource
     */
    public String getCrestAsResource() {
        return AppConfig.getConfigImagePath() + getCrestAsImage();
    }

    /**
     * Return the name of the {@code crest} for Server Side usage
     *
     * @return The crest as Image
     */
    public String getCrestAsImage() {
        return AppConfig.getConfigCrestPath() + "/" + crest + AppConfig.getConfigCrestExtension();
    }

    /**
     * Return the {@link Province Province} of the Health Service
     *
     * @return Health Service {@link Province Province}
     */
    public Province getProvince() {
        return province;
    }

    /**
     * Set the {@link Province Province} of the Health Service
     *
     * @param province Health Service {@link Province Province}
     */
    public void setProvince(Province province) {
        this.province = province;
    }
}
