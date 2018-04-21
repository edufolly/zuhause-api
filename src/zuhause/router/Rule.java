package zuhause.router;

/**
 *
 * @author Eduardo Folly
 */
public class Rule {

    private int id;
    private String name;
    private int idHost;
    private String host;
    private int idDestination;
    private String destination;
    private int idSchedule;
    private String schedule;
    private int status;

    /**
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     * @return
     */
    public Rule setId(int id) {
        this.id = id;
        return this;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * @return
     */
    public Rule setName(String name) {
        this.name = name;
        return this;
    }

    /**
     *
     * @return
     */
    public int getIdHost() {
        return idHost;
    }

    /**
     *
     * @param idHost
     * @return
     */
    public Rule setIdHost(int idHost) {
        this.idHost = idHost;
        return this;
    }

    /**
     *
     * @return
     */
    public String getHost() {
        return host;
    }

    /**
     *
     * @param host
     * @return
     */
    public Rule setHost(String host) {
        this.host = host;
        return this;
    }

    /**
     *
     * @return
     */
    public int getIdDestination() {
        return idDestination;
    }

    /**
     *
     * @param idDestination
     * @return
     */
    public Rule setIdDestination(int idDestination) {
        this.idDestination = idDestination;
        return this;
    }

    /**
     *
     * @return
     */
    public String getDestination() {
        return destination;
    }

    /**
     *
     * @param destination
     * @return
     */
    public Rule setDestination(String destination) {
        this.destination = destination;
        return this;
    }

    /**
     *
     * @return
     */
    public int getIdSchedule() {
        return idSchedule;
    }

    /**
     *
     * @param idSchedule
     * @return
     */
    public Rule setIdSchedule(int idSchedule) {
        this.idSchedule = idSchedule;
        return this;
    }

    /**
     *
     * @return
     */
    public String getSchedule() {
        return schedule;
    }

    /**
     *
     * @param schedule
     * @return
     */
    public Rule setSchedule(String schedule) {
        this.schedule = schedule;
        return this;
    }

    /**
     *
     * @return
     */
    public int getStatus() {
        return status;
    }

    /**
     *
     * @param status
     * @return
     */
    public Rule setStatus(int status) {
        this.status = status;
        return this;
    }

}
