package zuhause.test;

import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Eduardo Folly
 */
public abstract class AbstractTest implements Runnable {

    private static boolean passed = true;

    protected final Logger logger;

    /**
     *
     * @param logger
     */
    public AbstractTest(Logger logger) {
        this.logger = logger;
    }

    /**
     *
     * @return
     */
    public static boolean passed() {
        return passed;
    }

    /**
     *
     * @param tw
     */
    public void error(Throwable tw) {
        passed = false;
        logger.error("Erro ao executar teste:", tw);
    }

    /**
     *
     * @param tw
     * @param list
     */
    public void error(Throwable tw, List list) {
        error(tw);
        print(list);
    }

    /**
     *
     * @param tw
     * @param map
     */
    public void error(Throwable tw, Map map) {
        error(tw);
        print(map);
    }

    /**
     *
     * @param list
     * @throws Exception
     */
    public void notEmptyList(List list) throws Exception {
        if (list == null) {
            throw new Exception("Lista null.");
        }

        if (list.isEmpty()) {
            throw new Exception("Lista vazia.");
        }
    }

    /**
     *
     * @param map
     * @throws Exception
     */
    public void notEmptyMap(Map map) throws Exception {
        if (map == null) {
            throw new Exception("Map null.");
        }

        if (map.isEmpty()) {
            throw new Exception("Map vazio.");
        }
    }

    /**
     *
     * @param map
     */
    private void print(Map map) {
        if (map != null) {
            logger.info("====================");
            for (Object key : map.keySet()) {
                logger.info("{} => {}", key, map.get(key));
            }
            logger.info("====================");
        }

    }

    /**
     *
     * @param list
     */
    public void print(List list) {
        if (list != null) {
            for (Object o : list) {
                if (o instanceof Map) {
                    Map map = (Map) o;
                    logger.info("====================");
                    for (Object key : map.keySet()) {
                        logger.info("{} => {}", key, map.get(key));
                    }
                } else {
                    logger.info(o);
                }
            }
            logger.info("====================");
        }

    }

}
