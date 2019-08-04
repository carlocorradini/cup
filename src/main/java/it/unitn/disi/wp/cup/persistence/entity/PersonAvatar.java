package it.unitn.disi.wp.cup.persistence.entity;

import it.unitn.disi.wp.cup.config.AppConfig;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

/**
 * Entity Person Avatar
 *
 * @author Carlo Corradini
 */
public class PersonAvatar {
    /**
     * {@link Map map} of {@link PersonSex sex} associated with a default avatar
     * See {@link PersonSex} for more information
     */
    public static final Map<Character, String> DEFAULT_AVATARS = Map.ofEntries(
            Map.entry(PersonSex.UNKNOWN, "default_U"),
            Map.entry(PersonSex.MALE, "default_M"),
            Map.entry(PersonSex.FEMALE, "default_F")
    );

    /**
     * The 'fake' id of a default {@link PersonAvatar avatar}
     */
    public static final Long NOT_A_VALID_ID = -1L;

    private Long id;
    private Long personId;
    private String name;
    private LocalDateTime upload;

    @Override
    public boolean equals(Object obj) {
        PersonAvatar personAvatar;
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        personAvatar = (PersonAvatar) obj;
        return id.equals(personAvatar.id);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        return prime + ((id == null) ? 0 : id.hashCode());
    }

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
    public LocalDateTime getUpload() {
        return upload;
    }

    /**
     * Return the {@code upload} as a {@link LocalDate date}
     *
     * @return {@code upload} as a {@link LocalDate date}
     */
    public LocalDate getUploadDate() {
        if (upload != null)
            return upload.toLocalDate();
        return null;
    }

    /**
     * Return the {@code upload} as a {@link LocalTime time}
     *
     * @return {@code upload} as a {@link LocalTime time}
     */
    public LocalTime getUploadTime() {
        if (upload != null)
            return upload.toLocalTime();
        return null;
    }

    /**
     * Set the OffsetDateTime of the Person Avatar
     *
     * @param upload OffsetDateTime of the Person Avatar
     */
    public void setUpload(LocalDateTime upload) {
        this.upload = upload;
    }

    /**
     * Return the name of the {@link PersonAvatar avatar} for resource usage
     *
     * @return The avatar name as Resource
     */
    public String getNameAsResource() {
        return AppConfig.getConfigAvatarPath() + name + AppConfig.getConfigAvatarExtension();
    }
}
