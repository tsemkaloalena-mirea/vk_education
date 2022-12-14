package com.tsemkalo.homework8;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;

@Path("/info")
public final class CommonController {
    public CommonController() {

    }

    @GET
    @Path("/description")
    @Produces(MediaType.TEXT_HTML)
    public InputStream getDescription() {
        return CommonController.class.getResourceAsStream("/static/description");
    }
}
