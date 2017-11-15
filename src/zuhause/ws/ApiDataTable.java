package zuhause.ws;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import zuhause.annotations.GET;
import zuhause.annotations.Path;
import zuhause.annotations.PathParam;
import zuhause.chart.Data;
import zuhause.chart.DataSet;
import zuhause.db.DbConfig;
import zuhause.db.Pair;
import zuhause.db.PairDao;
import zuhause.util.Config;

/**
 *
 * @author Eduardo Folly
 */
@Path("/api/datatable")
public class ApiDataTable {

    private static final DbConfig DB_CONFIG = Config.getDbConfig("localhost");
    private static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    private static final SimpleDateFormat dbDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 
     * @param table
     * @param key
     * @return Data
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    @Path("/:table/:key")
    @GET
    @PathParam({"table", "key"})
    public Data teste(String table, String key) throws ClassNotFoundException, SQLException {

        Data data = new Data();

        DataSet dataSet = new DataSet();
        dataSet.setLabel("Temperatura");

        Calendar ini = new GregorianCalendar();
        ini.add(Calendar.DATE, -1);

        Calendar fim = new GregorianCalendar();

        List<Pair> pairs = new PairDao(DB_CONFIG).select("`tab` = ? AND "
                + "`key` = ? AND `when` BETWEEN ? AND ?",
                new String[]{table, key, dbDate.format(ini.getTime()),
                    dbDate.format(fim.getTime())},
                "`when` ASC", null);

        pairs = clear(pairs);

        for (Pair pair : pairs) {
            Date date = new Date(pair.getWhen().getTime());

            data.addLabel(sdf.format(date));

            dataSet.addData(Float.parseFloat(pair.getValue()));
        }

        data.addDataSet(dataSet);

        return data;
    }

    /**
     *
     * @param pairs
     * @return
     */
    private List<Pair> clear(List<Pair> pairs) {

        for (int i = 1; i < pairs.size() - 1; i++) {
            Float a = Float.parseFloat(pairs.get(i - 1).getValue());
            Float b = Float.parseFloat(pairs.get(i).getValue());
            Float c = Float.parseFloat(pairs.get(i + 1).getValue());
            if (a.equals(c) && !b.equals(a)) {
                pairs.get(i).setValue(String.valueOf(a));
            }
        }

        return pairs;
    }
}
