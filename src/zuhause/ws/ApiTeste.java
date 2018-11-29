package zuhause.ws;

import zuhause.annotations.Cache;
import zuhause.annotations.GET;
import zuhause.annotations.MediaTypeEnum;
import zuhause.annotations.Path;
import zuhause.annotations.ReturnType;

/**
 *
 * @author Eduardo Folly
 */
@Path("/api/teste")
public class ApiTeste {

    @GET
    @Cache(60)
    @ReturnType(MediaTypeEnum.TEXT)
    public String testeGet() {
        return "Teste OK!!";
    }

//    @GET
//    @Path("/serial/:cmd")
//    @PathParam({"cmd"})
//    public String SerialGET(String cmd) throws SerialPortException, IOException {
//
//        Serial arduino = Config.getInstance().getSerialConfigs().get("arduino");
//
//        arduino.write(cmd.substring(0, 1));
//
//        return arduino.waitFor("#");
//    }
    /**
     *
     * @param unidade
     * @param area
     * @return
     */
//    @GET
//    @Path("/:unidade/:area")
//    @PathParam({"unidade", "area"})
//    public RadiusConta TesteGET(int unidade, String area) {
//        return new RadiusConta(area, unidade);
//    }
    /**
     *
     * @param q
     * @return
     */
//    @GET
//    @Path("/qs")
//    @QueryStringParam({"q"})
//    public RadiusConta TesteGet(int q) {
//        return new RadiusConta("qs", q);
//    }
    /**
     *
     * @param account
     * @return
     */
//    @POST
//    @BodyParam
//    public RadiusConta TestPOST(RadiusConta account) {
//        return account;
//    }
    /**
     *
     * @param account
     * @param name
     * @param value
     * @return
     */
//    @POST
//    @Path("/:name/:value")
//    @BodyParam
//    @PathParam({"name", "value"})
//    public RadiusConta TestFull(RadiusConta account, String name, int value) {
//
//        return account;
//    }
    /**
     *
     * @return Account
     * @throws Exception
     */
//    @GET
//    @Path("/erro")
//    public Account TestException() throws Exception {
//        throw new Exception("Minha mensagem de erro.");
//    }
}
