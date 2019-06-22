package it.unitn.disi.wp.cup.persistence.entity;

import java.time.OffsetDateTime;

/**
 * Entity Person Avatar
 *
 * @author Carlo Corradini
 */
public final class PersonAvatar {
    private Long id;
    private Long personId;
    private String name;
    private OffsetDateTime upload;

    /**
     * Return the id of the Person Avatar
     *
     * @return Person Avatar id
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the Person Avatar id
     *
     * @param id Person Avatar id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Return the Person id of the Person Avatar
     *
     * @return Person Avatar id
     */
    public Long getPersonId() {
        return personId;
    }

    /**
     * Set the Person Avatar id
     *
     * @param personId Person Avatar id
     */
    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    /**
     * Return the name of the Person Avatar
     *
     * @return Person Avatar Name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the Person Avatar name
     *
     * @param name Person Avatar name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Return the OffsetDateTime of the Person Avatar
     *
     * @return OffsetDateTime Person Avatar
     */
    public OffsetDateTime getUpload() {
        return upload;
    }

    /**
     * Set the OffsetDateTime of the Person Avatar
     *
     * @param upload OffsetDateTime of the Person Avatar
     */
    public void setUpload(OffsetDateTime upload) {
        this.upload = upload;
    }
}
