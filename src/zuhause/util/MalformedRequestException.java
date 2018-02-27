package zuhause.util;

/**
 *
 * @author Eduardo Folly
 */
public class MalformedRequestException extends Exception {

    /**
     *
     * @param message
     */
    public MalformedRequestException(String message) {
        super(message);
    }

    /**
     *
     * @param message
     * @param cause
     */
    public MalformedRequestException(String message, Throwable cause) {
        super(message, cause);
    }

}
