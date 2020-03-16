package cz.ladicek.reproducer;

import javax.transaction.Transactional;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/example")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ExampleResource {
    @GET
    public List<Example> getAll() {
        return Example.listAll();
    }

    @GET
    @Path("/{id}")
    public Example get(@PathParam("id") Long id) {
        Example example = Example.findById(id);
        if (example == null) {
            throw new NotFoundException("example '" + id + "' not found");
        }
        return example;
    }


    @POST
    @Transactional
    public Response create(Example example) {
        if (example.id != null) {
            throw new ClientErrorException("unexpected ID in request", 422);
        }

        example.persist();
        return Response.ok(example).status(201).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Example update(@PathParam("id") Long id, Example newExample) {
        Example example = Example.findById(id);
        if (example == null) {
            throw new NotFoundException("example '" + id + "' not found");
        }

        example.text = newExample.text;
        return example;
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        Example example = Example.findById(id);
        if (example == null) {
            throw new NotFoundException("example '" + id + "' not found");
        }
        example.delete();
        return Response.status(204).build();
    }
}
