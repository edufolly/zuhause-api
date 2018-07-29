package zuhause.test;

import org.apache.logging.log4j.Logger;

/**
 *
 * @author Eduardo Folly
 */
public abstract class AbstractTest implements Runnable {

    protected final Logger logger;

    public AbstractTest(Logger logger) {
        this.logger = logger;
    }
}
