package zuhause;

import java.util.List;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zuhause.bot.TelegramBot;
import zuhause.db.DbConfig;
import zuhause.db.Pair;
import zuhause.db.PairDao;
import zuhause.util.BooleanUtil;
import zuhause.util.Config;
import zuhause.ws.ApiRouter;

/**
 *
 * @author Eduardo Folly
 */
public class RouterScheduler implements Runnable {

    /**
     * Monitora os MAC Address que estão na chave "monitora_mac". key = MAC
     * Address value = Nome para mensagem
     *
     * A chave "mac_status" registra o último status do Mac Address. key = MAC
     * Address value = 0 (desconectou da rede) ou 1 (conectou na rede)
     */
    private static final DbConfig DB_CONFIG
            = Config.getDbConfig("localhost");

    private static final Logger LOGGER = LogManager.getRootLogger();

    private static final TelegramBot BOT
            = Config.getTelegramBot("default");

    /**
     *
     */
    @Override
    public void run() {
        try {
            PairDao dao = new PairDao(DB_CONFIG);

            List<Pair> pairs = dao.selectTab("monitora_mac");

            if (!pairs.isEmpty()) {
                ApiRouter router = new ApiRouter();

                Set<String> macs = router.getConnected();

                for (Pair pair : pairs) {

                    String mac = pair.getKey();

                    List<Pair> p = dao.select("`tab` = ? AND `key` = ?",
                            new String[]{"mac_status", mac},
                            "`when` DESC", "1");

                    boolean save;
                    boolean ativo;

                    if (p.isEmpty()) {
                        save = true;
                    } else {
                        ativo = BooleanUtil.fromString(p.get(0).getValue());
                        save = macs.contains(mac) != ativo;
                    }

                    if (save) {
                        ativo = macs.contains(mac);

                        dao.insert("mac_status", mac, BooleanUtil
                                .toString(ativo));

                        String msg = pair.getValue()
                                + (ativo ? " chegou em" : " saiu de")
                                + " casa.";

                        BOT.sendMessage(msg);

                        LOGGER.info(msg);
                    }
                }
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }

        LOGGER.info("RouterScheduler");
    }
}
