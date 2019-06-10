package it.unitn.disi.wp.cup.persistence.entity;

import java.time.LocalDate;

/**
 * Entity PersonBeanbirthdate
 *
 * @author Carlo Corradini
 */
public final class Person {
    private Integer id;
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
    private String avatar;

    /**
     * Return the id of the Person
     *
     * @return Person id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Set the Person id
     *
     * @param id Person id
     */
    public void setId(Integer id) {
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
}
