package zuhause.ws;

import java.util.List;
import zuhause.annotations.GET;
import zuhause.annotations.Path;
import zuhause.router.DhcpClient;
import zuhause.router.Router;
import zuhause.util.Config;

/**
 *
 * @author Eduardo Folly
 */
@Path("/api/router")
public class ApiRouter {

    @Path("/dhcp/list")
    @GET
    public List<DhcpClient> DhcpListGET() throws Exception {
        Config config = Config.getInstance();
        Router router = config.getRouterConfigs().get("WRN240").parse();
        return router.dhcpList();
    }

}
