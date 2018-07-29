package zuhause.ws;

import com.google.common.base.CharMatcher;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import zuhause.annotations.GET;
import zuhause.annotations.Path;
import zuhause.util.Config;
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
     * @return JSON
     */
    @Path("/osversion") 
    @GET
    public Map<String, Object> osVersionGet() {
        Map<String, Object> map = new HashMap();
        map.put("os_version", Config.getOsVersionId());
        return map;
    }
    
    /**
     *
     * @return JSON
     * @throws IOException
     * @throws InterruptedException
     */
    @Path("/disk")
    @GET
    public List<Map<String, Object>> diskGet()
            throws IOException, InterruptedException {

        return TerminalHelper.execute("df");
    }

    /**
     *
     * @return JSON
     * @throws IOException
     * @throws InterruptedException
     */
    @Path("/ram")
    @GET
    public List<Map<String, Object>> ramGet()
            throws IOException, InterruptedException {

        // TODO - Problema pode acontecer aqui. Talvez somente "free" resolva.
        
        return TerminalHelper.execute("free -o");
    }

    /**
     *
     * @return JSON
     * @throws IOException
     * @throws InterruptedException
     */
    @Path("/top")
    @GET
    public List<Map<String, Object>> topGet()
            throws IOException, InterruptedException {

        // TODO - Problema pode acontecer aqui. Verificar como fazer o comando.
        
        return TerminalHelper.execute("top -bn1w1024", 6);
    }

    /**
     *
     * @return JSON
     * @throws IOException
     */
    @Path("/temp")
    @GET
    public Map<String, Object> tempGet() throws IOException {
        String temp = TerminalHelper
                .rawExecute("/opt/vc/bin/vcgencmd measure_temp").trim();

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
     * @return JSON
     * @throws IOException
     */
    @Path("/volts")
    @GET
    public Map<String, Object> voltsGet() throws IOException {
        String temp = TerminalHelper
                .rawExecute("/opt/vc/bin/vcgencmd measure_volts").trim();

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
     * @return JSON
     * @throws IOException
     * @throws ParseException
     */
    @Path("/uptime")
    @GET
    public Map<String, Object> uptimeGet() throws IOException, ParseException {
        String since = TerminalHelper.rawExecute("uptime -s").trim();

        Map<String, Object> mapa = new HashMap();

        mapa.put("since", since);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date uptime = sdf.parse(since);

        mapa.put("uptime", DateUtil.humanDateDifference(uptime, new Date()));

        return mapa;
    }

    /**
     *
     * @return JSON
     * @throws IOException
     * @throws java.lang.InterruptedException
     */
    @Path("/proc")
    @GET
    public List<Map<String, Object>> procGet()
            throws IOException, InterruptedException {

        // sudo apt-get install sysstat
        return TerminalHelper.execute("mpstat -P ON", 2);
    }

    /**
     *
     * @return JSON
     * @throws IOException
     */
    @Path("/arp")
    @GET
    public Map<String, Object> arpGet() throws IOException {
        // sudo apt-get install arp-scan

        Map<String, Object> mapa = new HashMap();

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

    /**
     *
     * @return JSON
     * @throws IOException
     */
    @Path("/wireless")
    @GET
    public Map<String, Object> wirelessGet() throws IOException {

        Map<String, Object> mapa = new HashMap();

        String as = TerminalHelper.rawExecute("/sbin/iwlist wlan0 scan");

        String[] wifi = as.split("\\n");

        String[] key = new String[]{"Address:", "Channel:", "ESSID:\"",
            "Quality=", "Signal level="};

        for (String line : wifi) {
            if (line.contains(key[0])) {
                int start = line.indexOf(key[0]) + key[0].length();
                mapa.put("mac", line.substring(start).trim());
            } else if (line.contains(key[1])) {
                int start = line.indexOf(key[1]) + key[1].length();
                mapa.put("channel", Integer
                        .parseInt(line.substring(start).trim()));
            } else if (line.contains(key[2])) {
                int start = line.indexOf(key[2]) + key[2].length();
                int end = line.length() - 1;
                mapa.put("essid", line.substring(start, end).trim());
            } else if (line.contains(key[3])) {
                int start = line.indexOf(key[3]) + key[3].length();
                int end = line.lastIndexOf(" dBm");
                String[] p = line.substring(start, end).trim().split("  ", 2);
                mapa.put("quality", p[0]);

                String[] q = p[0].split("/");

                float f = Float.parseFloat(q[0])
                        / Float.parseFloat(q[1]) * 100f;

                mapa.put("quality_percent", f);

                mapa.put("signal", Integer
                        .parseInt(p[1].substring(key[4].length())));
            }
        }

        return mapa;
    }
}
