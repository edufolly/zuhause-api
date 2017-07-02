package zuhause.ws;

import zuhause.annotations.GET;
import zuhause.annotations.Path;

/**
 *
 * @author Eduardo Folly
 */
@Path("/api/teste")
public class ApiTeste {

    @GET
    public String TesteGET() {
        return "Teste OK!!";
    }

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
