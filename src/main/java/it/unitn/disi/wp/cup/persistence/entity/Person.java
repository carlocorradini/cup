package it.unitn.disi.wp.cup.persistence.entity;

import java.time.LocalDate;
import java.util.List;

/**
 * Entity Person
 *
 * @author Carlo Corradini
 */
public final class Person {
    private Long id;
    private String email;
    private String password;
    private String name;
    private String surname;
    private String CF;
    private char sex;
    private LocalDate birthDate;
    private String birthPlace;
    private String province;
    private String doctor;
    private PersonAvatar avatar;
    private List<PersonAvatar> avatarHistory;

    /**
     * Return the id of the Person
     *
     * @return Person id
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the Person id
     *
     * @param id Person id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Return the email of the Person
     *
     * @return Person email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the Person email
     *
     * @param email Person email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Return the password of the Person
     *
     * @return Person password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the Person password
     *
     * @param password Person password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Return the name of the Person
     *
     * @return Person name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the Person
     *
     * @param name Person name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Return the surname of the Person
     *
     * @return Person surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Set the surname of the Person
     *
     * @param surname Person surname
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Return the sex of the Person
     *
     * @return Person sex
     */
    public char getSex() {
        return sex;
    }

    /**
     * Set the sex of the Person
     *
     * @param sex Person sex
     */
    public void setSex(char sex) {
        this.sex = sex;
    }

    /**
     * Return the avatar of the Person
     *
     * @return Person avatar
     */
    public PersonAvatar getAvatar() {
        return avatar;
    }

    /**
     * Set the avatar of the Person
     *
     * @param avatar Person avatar
     */
    public void setAvatar(PersonAvatar avatar) {
        this.avatar = avatar;
    }

    /**
     * Return the avatar history of the Person
     *
     * @return Person avatar history
     */
    public List<PersonAvatar> getAvatarHistory() {
        return avatarHistory;
    }

    /**
     * Set the avatar history of the Person
     *
     * @param avatarHistory Person avatar history
     */
    public void setAvatarHistory(List<PersonAvatar> avatarHistory) {
        this.avatarHistory = avatarHistory;
    }
}
