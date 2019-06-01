package it.unitn.disi.wp.cup.persistence.dao.exception;

/**
 * The exception thrown when something goes wrong in DAO factoring
 *
 * @author Carlo Corradini
 */
public class DAOFactoryException extends Exception {

    /**
     * Constructs a new exception with {@code null} as its detail message. The
     * cause is not initialized, and may subsequently be initialized by a call
     * to {@link #initCause}
     */
    public DAOFactoryException() {
        super();
    }

    /**
     * Constructs a new exception with the specified detail message. The cause
     * is not initialized, and may subsequently be initialized by a call to
     * {@link #initCause}
     *
     * @param message The detail message. The detail message is saved for later
     * retrieval by the {@link #getMessage()} method
     */
    public DAOFactoryException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified cause and a detail message
     * of <tt>(cause==null ? null : cause.toString())</tt> (which typically
     * contains the class and detail message of <tt>cause</tt>). This
     * constructor is useful for exceptions that are little more than wrappers
     * for other throwables (for example, {@link
     * java.security.PrivilegedActionException})
     *
     * @param cause The cause (which is saved for later retrieval by the
     * {@link #getCause()} method). (A <tt>null</tt> value is permitted, and
     * indicates that the cause is nonexistent or unknown.)
     */
    public DAOFactoryException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     * <p>
     * Note that the detail message associated with {@code cause} is <i>not</i>
     * automatically incorporated in this exception's detail message.
     *
     * @param message The detail message (which is saved for later retrieval by
     * the {@link #getMessage()} method)
     * @param cause The cause (which is saved for later retrieval by the
     * {@link #getCause()} method). (A <tt>null</tt> value is permitted, and
     * indicates that the cause is nonexistent or unknown.)
     */
    public DAOFactoryException(String message, Throwable cause) {
        super(message, cause);
    }

}
