package com.tsemkalo.homework8;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.tsemkalo.homework8.generated.tables.pojos.Product;
import org.eclipse.jetty.http.HttpStatus;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/products")
public final class ProductController {
    private final ObjectMapper objectMapper;
    private final ProductService productService;

    @Inject
    public ProductController(ProductService productService, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.productService = productService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProducts() {
        return Response.ok(
                productService.getAll()
        ).header(HttpHeaders.CACHE_CONTROL, "no-cache").build();
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String save(Product product) {
        return productService.save(product);
    }

    @POST
    @Path("/{name}/delete")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteProducts(@PathParam("name") String productName) {
        List<Integer> productIds = productService.deleteProductsByName(productName);
        if (productIds != null) {
            return Response.ok(
                            "Products with ids " + productIds.stream().map(String::valueOf).collect(Collectors.joining(", ")) + " are deleted"
                    ).header(HttpHeaders.CACHE_CONTROL, "no-cache")
                    .build();
        }
        return Response.status(HttpStatus.NOT_FOUND_404).build();
    }
}
