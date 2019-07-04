package it.unitn.disi.wp.cup.persistence.entity;

import java.util.List;

/**
 * Entity Person Sex
 *
 * @author Carlo Corradini
 */
public class PersonSex {
    /**
     * Unknown Sex
     */
    public static final char UNKNOWN = 'U';
    /**
     * Male Sex
     */
    public static final char MALE = 'M';
    /**
     * Female Sex
     */
    public static final char FEMALE = 'F';

    /**
     * A {@link List list} of supported sex
     */
    public static final List<Character> SUPPORTED_SEX = List.of(MALE, FEMALE);

    private char sex;

    /**
     * Return the sex {@link Character character}
     *
     * @return Sex {@link Character character}
     */
    public char getSex() {
        return sex;
    }

    /**
     * Set the sex
     * If {@code sex} is not present in {@code SUPPORTED_SEX} an {@code UNKNOWN} sex will be set
     *
     * @param sex The {@link Character character} sex
     */
    public void setSex(char sex) {
        if (SUPPORTED_SEX.contains(sex))
            this.sex = sex;
        else this.sex = UNKNOWN;
    }
}
