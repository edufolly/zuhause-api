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

    public Rule() {
    }

    public int getId() {
        return id;
    }

    public Rule setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Rule setName(String name) {
        this.name = name;
        return this;
    }

    public int getIdHost() {
        return idHost;
    }

    public Rule setIdHost(int idHost) {
        this.idHost = idHost;
        return this;
    }

    public String getHost() {
        return host;
    }

    public Rule setHost(String host) {
        this.host = host;
        return this;
    }

    public int getIdDestination() {
        return idDestination;
    }

    public Rule setIdDestination(int idDestination) {
        this.idDestination = idDestination;
        return this;
    }

    public String getDestination() {
        return destination;
    }

    public Rule setDestination(String destination) {
        this.destination = destination;
        return this;
    }

    public int getIdSchedule() {
        return idSchedule;
    }

    public Rule setIdSchedule(int idSchedule) {
        this.idSchedule = idSchedule;
        return this;
    }

    public String getSchedule() {
        return schedule;
    }

    public Rule setSchedule(String schedule) {
        this.schedule = schedule;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public Rule setStatus(int status) {
        this.status = status;
        return this;
    }

}
