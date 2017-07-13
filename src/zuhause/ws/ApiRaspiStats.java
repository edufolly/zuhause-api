package zuhause.ws;

import com.google.common.base.CharMatcher;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import zuhause.annotations.GET;
import zuhause.annotations.Path;
import zuhause.util.DateUtil;
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
        String temp = TerminalHelper.rawExecute("/opt/vc/bin/vcgencmd measure_temp").trim();

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
        String temp = TerminalHelper.rawExecute("/opt/vc/bin/vcgencmd measure_volts").trim();

        String[] p = temp.split("=");

        Map<String, Object> mapa = new HashMap();

        mapa.put("raw", p[1]);
        mapa.put("value", Float.parseFloat(CharMatcher.inRange('0', '9')
                .or(CharMatcher.is('.'))
                .or(CharMatcher.is('-'))
                .retainFrom(p[1])));
        mapa.put("unit", "V");

        return mapa;
    }

    /**
     *
     * @return @throws IOException
     * @throws ParseException
     */
    @Path("/uptime")
    @GET
    public Map<String, String> UptimeGET() throws IOException, ParseException {
        String since = TerminalHelper.rawExecute("uptime -s").trim();

        Map<String, String> mapa = new HashMap();

        mapa.put("since", since);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date uptime = sdf.parse(since);

        mapa.put("uptime", DateUtil.humanDateDifference(uptime, new Date()));

        return mapa;
    }

    /**
     *
     * @return @throws IOException
     * @throws java.lang.InterruptedException
     */
    @Path("/proc")
    @GET
    public List<Map<String, String>> ProcGET() throws IOException, InterruptedException {
        // sudo apt-get install sysstat
        return TerminalHelper.execute("mpstat -P ON", 2);
    }

    @Path("/arp")
    @GET
    public Map<String, String> getArp() throws IOException {
        // sudo apt-get install arp-scan

        Map<String, String> mapa = new HashMap();

        String as = TerminalHelper.rawExecute("sudo arp-scan -l -g -q");

        String[] arpscan = as.split("\\n");

        if (arpscan.length > 2) {
            for (int i = 2; i < arpscan.length; i++) {
                String linha = arpscan[i].trim();
                if (linha.isEmpty()) {
                    break;
                }
                String[] p = linha.split("\\t");
                if (p.length > 1) {
                    mapa.put(p[1].toUpperCase().replaceAll(":", "-"), p[0]);
                }
            }
        }
        return mapa;
    }

    /*
    @Path("/iftop")
    @GET
    public Map<String, String> iftopGET() throws IOException, ParseException {
        String iftop = TerminalHelper.rawExecute("sudo iftop -t -s 2").trim();
        System.out.println(iftop);
        return null;
    }
     */
}
