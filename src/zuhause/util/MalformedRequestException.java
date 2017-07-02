package zuhause.util;

/**
 *
 * @author Eduardo Folly
 */
public class MalformedRequestException extends Exception {

    public MalformedRequestException(String message) {
        super(message);
    }

    public MalformedRequestException(String message, Throwable cause) {
        super(message, cause);
    }

}
