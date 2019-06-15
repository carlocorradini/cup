package it.unitn.disi.wp.cup.util;


import org.mindrot.jbcrypt.BCrypt;

import java.util.logging.Logger;

/**
 * Crypto Utility Class
 *
 * @author Carlo Corradini
 */
public class CryptUtil {
    private static final int COST = 12;
    private static final Logger LOGGER = Logger.getLogger(CryptUtil.class.getName());

    /**
     * Hash the password
     *
     * @param password The password to hash
     * @return The hashed password
     */
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(COST));
    }

    /**
     * Validate a password with an hash
     *
     * @param password The password to check
     * @param hash     The hash to compare with
     * @return True if valid, false otherwise
     */
    public static boolean validate(String password, String hash) {
        return BCrypt.checkpw(password, hash);
    }
}
