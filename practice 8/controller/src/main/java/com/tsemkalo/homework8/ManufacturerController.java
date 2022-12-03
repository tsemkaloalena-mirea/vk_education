package com.tsemkalo.homework8;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import generated.tables.pojos.Product;
import org.eclipse.jetty.http.HttpStatus;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/manufacturers")
public final class ManufacturerController {
    private final ObjectMapper objectMapper;
    private final ProductDAO productDAO;

    @Inject
    public ManufacturerController(ObjectMapper objectMapper, ProductDAO productDAO) {
        this.objectMapper = objectMapper;
        this.productDAO = productDAO;
    }

    @GET
    @Path("{name}/products")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public Response getProductsByManufacturer(@PathParam("name") String name) {
        List<Product> products = productDAO.getAllByManufacturer(name);
        if (!products.isEmpty()) {
            return Response.ok(
                    products
            ).header(HttpHeaders.CACHE_CONTROL, "no-cache").build();
        }
        return Response.status(HttpStatus.NOT_FOUND_404).build();
    }
}
