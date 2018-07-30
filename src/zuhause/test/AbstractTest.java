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
     * @param o
     * @throws Exception
     */
    public void notNull(Object o) throws Exception {
        if (o == null) {
            throw new TestErrorException("Object é null.");
        }
    }

    /**
     *
     * @param list
     * @throws Exception
     */
    public void listNotEmpty(List list) throws Exception {
        notNull(list);

        if (list.isEmpty()) {
            throw new TestErrorException("Lista vazia.");
        }
    }

    /**
     *
     * @param list
     * @param size
     * @throws Exception
     */
    public void listSizeEqual(List list, int size) throws Exception {
        listNotEmpty(list);
        if (list.size() != size) {
            throw new TestErrorException("Lista com tamanho (" + list.size()
                    + ") diferente de " + size + ".");
        }
    }

    /**
     *
     * @param map
     * @throws Exception
     */
    public void mapNotEmpty(Map map) throws Exception {
        notNull(map);

        if (map.isEmpty()) {
            throw new TestErrorException("Map vazio.");
        }
    }

    /**
     * 
     * @param map
     * @param key
     * @throws Exception 
     */
    public void mapHasKey(Map map, Object key) throws Exception {
        notNull(map);
        notNull(key);

        if (!map.containsKey(key)) {
            throw new TestErrorException("Key " + key + " não encontrada.");
        }
    }

    /**
     *
     * @param map
     */
    public void print(Map map) {
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
                    print((Map) o);
                } else {
                    logger.info(o);
                }
            }
            logger.info("====================");
        }

    }

    /**
     *
     * @param map
     * @param key
     * @param dMin
     * @param dMax
     * @throws Exception
     */
    public void mapBetweenExclusive(Map map, Object key, Double dMin,
            Double dMax) throws Exception {

        Object o = getSafeInMap(map, key);
        betweenExclusive(o, dMin, dMax, key);
    }

    /**
     *
     * @param o
     * @param dMin
     * @param dMax
     * @throws Exception
     */
    public void betweenExclusive(Object o, Double dMin, Double dMax)
            throws Exception {

        betweenExclusive(o, dMin, dMax, null);
    }

    /**
     *
     * @param o
     * @param dMin
     * @param dMax
     * @param key
     * @throws Exception
     */
    private void betweenExclusive(Object o, Double dMin, Double dMax,
            Object key) throws Exception {

        notNull(o);
        notNull(dMin);
        notNull(dMax);

        if (key == null) {
            key = o;
        }

        Double nd = getSafeDouble(o);

        if (nd < dMin || nd > dMax) {
            throw new TestErrorException(key + " (" + o + ") não está entre "
                    + dMin + " e " + dMax + " exclusivo.");
        }
    }

    /**
     *
     * @param map
     * @param key
     * @param dMin
     * @param dMax
     * @throws Exception
     */
    public void mapBetweenInclusive(Map map, Object key, Double dMin,
            Double dMax) throws Exception {

        Object o = getSafeInMap(map, key);
        betweenInclusive(o, dMin, dMax, key);
    }

    /**
     *
     * @param o
     * @param dMin
     * @param dMax
     * @throws Exception
     */
    public void betweenInclusive(Object o, Double dMin, Double dMax)
            throws Exception {

        betweenInclusive(o, dMin, dMax, null);
    }

    /**
     *
     * @param o
     * @param dMin
     * @param dMax
     * @param key
     * @throws Exception
     */
    private void betweenInclusive(Object o, Double dMin, Double dMax,
            Object key) throws Exception {

        notNull(o);
        notNull(dMin);
        notNull(dMax);

        if (key == null) {
            key = o;
        }

        Double nd = getSafeDouble(o);

        if (nd <= dMin || nd >= dMax) {
            throw new TestErrorException(key + " (" + o + ") não está entre "
                    + dMin + " e " + dMax + " inclusivo.");
        }
    }

    /**
     *
     * @param map
     * @param key
     * @param i
     * @throws Exception
     */
    public void mapEqual(Map map, Object key, Integer i) throws Exception {
        Object o = getSafeInMap(map, key);
        equal(o, i, key);
    }

    /**
     *
     * @param o
     * @param i
     * @throws Exception
     */
    public void equal(Object o, Integer i) throws Exception {
        equal(o, i, null);
    }

    /**
     *
     * @param o
     * @param i
     * @param key
     * @throws Exception
     */
    private void equal(Object o, Integer i, Object key) throws Exception {

        notNull(o);
        notNull(i);

        if (key == null) {
            key = o;
        }

        Integer ni = getSafeInteger(o);

        if (!ni.equals(i)) {
            throw new TestErrorException(key + " diferente de " + i);
        }
    }

    /**
     *
     * @param map
     * @param key
     * @param d
     * @throws Exception
     */
    public void mapEqual(Map map, Object key, Double d) throws Exception {
        Object o = getSafeInMap(map, key);
        equal(o, d, key);
    }

    /**
     *
     * @param o
     * @param d
     * @throws Exception
     */
    public void equal(Object o, Double d) throws Exception {
        equal(o, d, null);
    }

    /**
     *
     * @param o
     * @param d
     * @param key
     * @throws Exception
     */
    private void equal(Object o, Double d, Object key) throws Exception {

        notNull(o);
        notNull(d);

        if (key == null) {
            key = o;
        }

        Double nd = getSafeDouble(o);

        if (!nd.equals(d)) {
            throw new TestErrorException(key + " diferente de " + d);
        }
    }

    /**
     *
     * @param map
     * @param key
     * @param s
     * @throws Exception
     */
    public void mapEqual(Map map, Object key, String s) throws Exception {
        Object o = getSafeInMap(map, key);
        equal(o, s, key);
    }

    /**
     *
     * @param o
     * @param s
     * @throws Exception
     */
    public void equal(Object o, String s) throws Exception {
        equal(o, s, null);
    }

    /**
     *
     * @param o
     * @param s
     * @param key
     * @throws Exception
     */
    private void equal(Object o, String s, Object key) throws Exception {

        notNull(o);
        notNull(s);

        if (key == null) {
            key = o;
        }

        String ns = getSafeString(o);

        if (!ns.equals(s)) {
            throw new TestErrorException(key + " diferente de " + s);
        }
    }

    /**
     *
     * @param o
     * @return
     * @throws Exception
     */
    private Double getSafeDouble(Object o) throws Exception {
        notNull(o);
        if (o instanceof Double) {
            return Double.parseDouble(o.toString());
        }
        throw new TestErrorException(o + " não é Double.");
    }

    /**
     *
     * @param o
     * @return
     * @throws Exception
     */
    private Integer getSafeInteger(Object o) throws Exception {
        notNull(o);
        if (o instanceof Double) {
            return Integer.parseInt(o.toString());
        }
        throw new TestErrorException(o + " não é Integer.");

    }

    /**
     *
     * @param o
     * @return
     * @throws Exception
     */
    private String getSafeString(Object o) throws Exception {
        notNull(o);
        if (o instanceof String) {
            return o.toString();
        }
        throw new TestErrorException(o + " não é String.");
    }

    /**
     *
     * @param map
     * @param key
     * @return
     * @throws Exception
     */
    private Object getSafeInMap(Map map, Object key) throws Exception {
        notNull(map);
        notNull(key);
        Object object = map.get(key);
        notNull(object);
        return object;
    }

}
