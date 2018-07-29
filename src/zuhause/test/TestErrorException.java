package zuhause.test;

/**
 *
 * @author Eduardo Folly
 */
public class TestErrorException extends Exception {

    public TestErrorException() {
    }

    public TestErrorException(String message) {
        super(message);
    }

    public TestErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public TestErrorException(Throwable cause) {
        super(cause);
    }

}
