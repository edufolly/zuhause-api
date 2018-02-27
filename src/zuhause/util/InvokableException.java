package zuhause.util;

/**
 *
 * @author Eduardo Folly
 */
public class InvokableException extends Exception {

    /**
     *
     * @param message
     */
    public InvokableException(String message) {
        super(message);
    }

    /**
     *
     * @param message
     * @param cause
     */
    public InvokableException(String message, Throwable cause) {
        super(message, cause);
    }

}
