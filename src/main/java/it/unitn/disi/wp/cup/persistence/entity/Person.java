package it.unitn.disi.wp.cup.persistence.entity;

import it.unitn.disi.wp.cup.util.StringUtil;

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
    private PersonSex sex;
    private String fiscalCode;
    private LocalDate birthDate;
    private City birthCity;
    private City city;
    private PersonAvatar avatar;
    private List<PersonAvatar> avatarHistory;
    private List<PrescriptionExam> exams;
    private List<PrescriptionMedicine> medicines;

    @Override
    public boolean equals(Object obj) {
        Person person;
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        person = (Person) obj;
        return id.equals(person.id);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        return prime + ((id == null) ? 0 : id.hashCode());
    }

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
     * Return the name of the Person capitalized
     *
     * @return Person name capitalized
     */
    public String getNameCapitalized() {
        return StringUtil.capitalize(name);
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
     * Return the surname of the Person capitalized
     *
     * @return Person surname capitalized
     */
    public String getSurnameCapitalized() {
        return StringUtil.capitalize(surname);
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
     * Return the Full Name of the Person
     *
     * @return Full Name of the Person
     */
    public String getFullName() {
        return getName() + " " + getSurname();
    }

    /**
     * Return the Full Name of the Person Capitalized
     *
     * @return Capitalized Full Name of the Person
     */
    public String getFullNameCapitalized() {
        return getNameCapitalized() + " " + getSurnameCapitalized();
    }

    /**
     * Return the sex of the Person
     *
     * @return Person sex
     */
    public PersonSex getSex() {
        return sex;
    }

    /**
     * Set the sex of the Person
     *
     * @param sex Person sex
     */
    public void setSex(PersonSex sex) {
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
    public City getBirthCity() {
        return birthCity;
    }

    /**
     * Set the birth city of the Person
     *
     * @param birthCity Person birth city
     */
    public void setBirthCity(City birthCity) {
        this.birthCity = birthCity;
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

    /**
     * Return the exams of the Person
     *
     * @return Person exams
     */
    public List<PrescriptionExam> getExams() {
        return exams;
    }

    /**
     * Set the exams of the Person
     *
     * @param exams Person exams
     */
    public void setExams(List<PrescriptionExam> exams) {
        this.exams = exams;
    }

    /**
     * Return the medicines of the Person
     *
     * @return Person medicines
     */
    public List<PrescriptionMedicine> getMedicines() {
        return medicines;
    }

    /**
     * Set the medicines of the Person
     *
     * @param medicines Person medicines
     */
    public void setMedicines(List<PrescriptionMedicine> medicines) {
        this.medicines = medicines;
    }
}
