package zuhause.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Eduardo Folly
 */
public class Firewall {

    private static final Logger LOGGER = LogManager.getRootLogger();
    private static final Set<String> NETWORKS = new HashSet();

    /**
     *
     */
    static {
        try {
            LOGGER.trace("Carregando configurações do firewall.");
            File file = new File("firewall.conf");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String network = line.split("#", 2)[0].trim();
                if (!network.isEmpty()) {
                    NETWORKS.add(network);
                    LOGGER.trace("Rede Liberada: {}", network);
                }
            }
        } catch (Exception ex) {
            LOGGER.warn("Erro ao carregar configurações do firewall.", ex);
            NETWORKS.clear();
        }
    }

    /**
     *
     * @param address
     * @return
     */
    public static boolean run(InetAddress address) {
        try {
            if (NETWORKS.isEmpty()) {
                return true;
            }

            for (String network : NETWORKS) {
                CIDRUtils util = new CIDRUtils(network);
                if (util.isInRange(address.getHostAddress())) {
                    return true;
                }
            }
        } catch (Exception ex) {
            LOGGER.error("Erro no firewall.", ex);
        }
        return false;
    }
}
