package zuhause;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Map;
import zuhause.db.DbConfig;
import zuhause.db.DbUtil;
import zuhause.util.Config;
import zuhause.util.ServerLog;
import zuhause.ws.ApiArduino;

/**
 *
 * @author Eduardo Folly
 */
public class TempScheduler implements Runnable {

    private static final DbConfig DB_CONFIG = Config.getInstance().getDbConfigs().get("localhost");
    private static final ServerLog LOG = ServerLog.getInstance();

    @Override
    public void run() {
        try {
            ApiArduino apiArduino = new ApiArduino();
            Map<String, Object> map = apiArduino.TempInternaGET();

            String temp = map.get("t").toString();

            Connection connection = DbUtil.getConnection(DB_CONFIG);

            Statement statement = connection.createStatement();

            statement.execute("INSERT INTO `zuhause`.`pairs`"
                    + "(`key`, `value`) VALUES ('tempInterna', '" + temp + "');");

            statement.close();

            LOG.msg(0, "TempScheduler");

            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
