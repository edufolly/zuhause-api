package zuhause.ws;

import java.util.HashMap;
import java.util.Map;
import zuhause.annotations.GET;
import zuhause.annotations.MediaTypeEnum;
import zuhause.annotations.Path;
import zuhause.annotations.ReturnType;

/**
 *
 * @author Eduardo Folly
 */
@Path("/api")
public class ApiUtil {

    @GET
    @Path("/login")
    @ReturnType(MediaTypeEnum.JSON)
    public Map loginGet() {
        Map map = new HashMap();
        map.put("login", "ok");
        return map;
    }

}
