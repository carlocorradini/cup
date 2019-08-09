package it.unitn.disi.wp.cup.util.obj;

import com.alibaba.fastjson.JSON;

/**
 * Simple Json Message Response Object
 *
 * @author Carlo Corradini
 */
public final class JsonMessage {

    public static final int ERROR_NO_ERROR = 0;
    public static final int ERROR_MESSAGE_NOT_INITIALIZED = 1;
    public static final int ERROR_AUTHENTICATION = 2;
    public static final int ERROR_PASSWORD_LENGTH = 3;
    public static final int ERROR_UNKNOWN = 256;

    private int error;

    /**
     * Create a Json Message with {@code error} passed
     *
     * @param error The error to set
     */
    public JsonMessage(int error) {
        this.error = error;
    }

    /**
     * Create a new Empty Json Message
     */
    public JsonMessage() {
        this(ERROR_MESSAGE_NOT_INITIALIZED);
    }

    /**
     * Return the current error
     *
     * @return The error
     */
    public int getError() {
        return error;
    }

    /**
     * Set the current {@code this.error} to {@code error}
     *
     * @param error The new Error to se
     */
    public void setError(int error) {
        this.error = error;
    }

    /**
     * Return this instance into a JSON String
     *
     * @return The JSON String format of this class
     */
    public String toJsonString() {
        return JSON.toJSONString(this, false);
    }
}
