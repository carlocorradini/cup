package it.unitn.disi.wp.cup.config.exception;

/**
 * Config Exception for handling Configuration exceptions
 *
 * @author Carlo Corradini
 */
public class ConfigException extends Exception {

    /**
     * Create an empty ConfigException
     */
    public ConfigException() {
        super();
    }

    /**
     * Create a ConfigException with the message passed
     *
     * @param message The error message to print
     */
    public ConfigException(String message) {
        super(message);
    }

}
