package zuhause.ws;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import zuhause.annotations.GET;
import zuhause.annotations.Path;
import zuhause.util.TerminalHelper;

/**
 *
 * @author Eduardo Folly
 */
@Path("/api/raspi")
public class ApiRaspiStats {

    @Path("/disk")
    @GET
    public List<Map<String, String>> DiskGET() throws IOException, InterruptedException {
        return TerminalHelper.execute("df");
    }

    @Path("/ram")
    @GET
    public List<Map<String, String>> RamGET() throws IOException, InterruptedException {
        return TerminalHelper.execute("free -o");
    }

    @Path("/top")
    @GET
    public List<Map<String, String>> TopGET() throws IOException, InterruptedException {
        return TerminalHelper.execute("top -bn1w1024", 6);
    }
}
