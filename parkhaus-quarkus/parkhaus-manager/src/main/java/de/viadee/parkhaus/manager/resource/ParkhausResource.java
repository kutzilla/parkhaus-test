package de.viadee.parkhaus.manager.resource;

import de.viadee.parkhaus.manager.config.ParkhausConfig;
import de.viadee.parkhaus.manager.entity.Parkticket;
import de.viadee.parkhaus.manager.repository.ParkticketRepository;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.time.LocalDateTime;
import java.util.List;

@Path("/parkhaus")
public class ParkhausResource {

    @Inject
    ParkticketRepository parkticketRepository;

    @Inject
    ParkhausConfig parkhausConfig;

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public String create(@QueryParam("entered") LocalDateTime entered) {

        if (entered == null) {
            throw new BadRequestException("validTo must be specified");
        }

        Parkticket parkticket = new Parkticket(entered);

        parkticketRepository.persist(parkticket);

        return parkticket.getId();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Parkticket> getAll() {
        return parkticketRepository.findAll();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}/validTo")
    public boolean getValidTo(@PathParam("id") String id) {

        Parkticket parkticket = this.parkticketRepository.findById(id);

        LocalDateTime now = LocalDateTime.now();
        return parkticket != null && parkticket.getEntered().isBefore(now)
                && parkticket.getPayment() != null
                && now.minusMinutes(parkhausConfig.getToleranceBtwPaymentAndExit()).isBefore(parkticket.getPayment());
    }

}
