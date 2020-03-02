package de.viadee.parkhaus.schranke.api;

import java.time.LocalDateTime;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient
@Path("parkticket")
public interface ParkhausManager {

    @Path("{id}/isAllowedToExit")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public boolean isAllowedToExit(@PathParam("id") String id);

}

