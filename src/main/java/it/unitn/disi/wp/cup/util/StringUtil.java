package it.unitn.disi.wp.cup.util;

import java.security.SecureRandom;

public final class StringUtil {
    private static final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_!£$%&/()=?^*\\|";
    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * Generates random string of given length from {@code ALPHABET}
     *
     * @param length Length of the String to generate
     * @return Random string of given {@code length}
     */
    public static String generateRandom(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; ++i) {
            sb.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }

    /**
     * Return the {@code string} capitalized
     * If {@code string} is empty or null return the {@code string} itself
     *
     * @param string The String to Capitalize
     * @return The {@code string} capitalized
     */
    public static String capitalize(String string) {
        if (string == null || string.isEmpty()) return string;

        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }
}
