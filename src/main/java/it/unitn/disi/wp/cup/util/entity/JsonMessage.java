package it.unitn.disi.wp.cup.util.entity;

/**
 * Simple Json Message Response Entity
 *
 * @author Carlo Corradini
 */
public class JsonMessage {

    private final boolean error;
    private final int errorCode;
    private final String message;

    /**
     * Create a new Json Message
     *
     * @param error     True if is an error, false otherwise
     * @param errorCode The error code to identify
     * @param message   The body of the message
     */
    public JsonMessage(boolean error, int errorCode, String message) {
        this.error = error;
        this.errorCode = errorCode;
        this.message = message;
    }

    /**
     * Check if the message is an errorMessage
     *
     * @return True if it is an error, false otherwise
     */
    public boolean isError() {
        return error;
    }

    /**
     * Return the error code of the Message
     *
     * @return The error Code
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * Get the current message body
     *
     * @return The message body
     */
    public String getMessage() {
        return message;
    }
}
