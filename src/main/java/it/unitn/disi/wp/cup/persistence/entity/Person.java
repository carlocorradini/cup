package it.unitn.disi.wp.cup.persistence.entity;

import java.time.LocalDate;
import java.util.List;

/**
 * Entity Person
 *
 * @author Carlo Corradini
 */
public class Person {
    private Long id;
    private String email;
    private String password;
    private String name;
    private String surname;
    private char sex;
    private String fiscalCode;
    private LocalDate birthDate;
    private City bithCity;
    private City city;
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
     * Return the fiscal code of the Person
     *
     * @return Person fiscal code
     */
    public String getFiscalCode() {
        return fiscalCode;
    }

    /**
     * Set the fiscal code of the Person
     *
     * @param fiscalCode Person fiscal code
     */
    public void setFiscalCode(String fiscalCode) {
        this.fiscalCode = fiscalCode;
    }

    /**
     * Return the birth date of the Person
     *
     * @return Person birth date
     */
    public LocalDate getBirthDate() {
        return birthDate;
    }

    /**
     * Set the birth date of the Person
     *
     * @param birthDate Person birth date
     */
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * Return the birth city of the Person
     *
     * @return Person birth city
     */
    public City getBithCity() {
        return bithCity;
    }

    /**
     * Set the birth city of the Person
     *
     * @param bithCity Person birth city
     */
    public void setBithCity(City bithCity) {
        this.bithCity = bithCity;
    }

    /**
     * Return the city of the Person
     *
     * @return Person city
     */
    public City getCity() {
        return city;
    }

    /**
     * Set the city of the Person
     *
     * @param city Person city
     */
    public void setCity(City city) {
        this.city = city;
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
