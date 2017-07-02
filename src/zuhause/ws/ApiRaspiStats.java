package zuhause.ws;

import com.google.common.base.CharMatcher;
import java.io.IOException;
import java.util.HashMap;
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

    /**
     *
     * @return @throws IOException
     * @throws InterruptedException
     */
    @Path("/disk")
    @GET
    public List<Map<String, String>> DiskGET() throws IOException, InterruptedException {
        return TerminalHelper.execute("df");
    }

    /**
     *
     * @return @throws IOException
     * @throws InterruptedException
     */
    @Path("/ram")
    @GET
    public List<Map<String, String>> RamGET() throws IOException, InterruptedException {
        return TerminalHelper.execute("free -o");
    }

    /**
     *
     * @return @throws IOException
     * @throws InterruptedException
     */
    @Path("/top")
    @GET
    public List<Map<String, String>> TopGET() throws IOException, InterruptedException {
        return TerminalHelper.execute("top -bn1w1024", 6);
    }

    /**
     *
     * @return @throws IOException
     */
    @Path("/temp")
    @GET
    public Map<String, Object> TempGET() throws IOException {
        String temp = TerminalHelper.rawExecute("/opt/vc/bin/vcgencmd measure_temp");

        String[] p = temp.split("=");
        String[] q = p[1].split("'");

        Map<String, Object> mapa = new HashMap();

        mapa.put("raw", p[1]);
        mapa.put("value", Float.parseFloat(q[0]));
        mapa.put("unit", q[1]);

        return mapa;
    }

    /**
     *
     * @return @throws IOException
     */
    @Path("/volts")
    @GET
    public Map<String, Object> VoltsGET() throws IOException {
        String temp = TerminalHelper.rawExecute("/opt/vc/bin/vcgencmd measure_volts");

        String[] p = temp.split("=");

        Map<String, Object> mapa = new HashMap();

        mapa.put("raw", p[1]);
        mapa.put("value", Float.parseFloat(CharMatcher.inRange('0', '9')
                .or(CharMatcher.is('.'))
                .or(CharMatcher.is('-'))
                .retainFrom(p[1])));

        return mapa;
    }

    /**
     *
     * @return @throws IOException
     */
    @Path("/uptime")
    @GET
    public Map<String, String> UptimeGET() throws IOException {
        String since = TerminalHelper.rawExecute("uptime -s");

        Map<String, String> mapa = new HashMap();

        mapa.put("since", since);

        return mapa;
    }

}
