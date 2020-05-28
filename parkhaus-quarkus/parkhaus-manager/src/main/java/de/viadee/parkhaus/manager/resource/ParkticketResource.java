package de.viadee.parkhaus.manager.resource;

import de.viadee.parkhaus.manager.config.ParkhausConfig;
import de.viadee.parkhaus.manager.entity.Parkticket;
import de.viadee.parkhaus.manager.repository.ParkticketRepository;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;

@Path("/parkticket")
public class ParkticketResource {

    ParkticketRepository parkticketRepository;

    ParkhausConfig parkhausConfig;

    @Inject
    public ParkticketResource(ParkticketRepository parkticketRepository, ParkhausConfig parkhausConfig) {
        this.parkticketRepository = parkticketRepository;
        this.parkhausConfig = parkhausConfig;
    }

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
    public Parkticket get(String id) {
        return parkticketRepository.findById(id);
    }

    @GET
    @Path("{id}/getPaymentAmount")
    @Produces(MediaType.APPLICATION_JSON)
    public Double getPaymentAmount(@PathParam("id") String id) {
        Parkticket parkticket = parkticketRepository.findById(id);
        if (parkticket == null) {
            throw new NoSuchElementException();
        }
        return getPaymentAmount(parkticket);
    }

    private Double getPaymentAmount(Parkticket parkticket) {
        LocalDateTime to = LocalDateTime.now();

        LocalDateTime from = parkticket.getEntered();

        long parkingTime = ChronoUnit.HOURS.between(from, to);

        return parkhausConfig.getGebuehr() * parkingTime;
    }

    @PUT
    @Path("{id}/makePayment")
    public Boolean makePayment(@PathParam("id") String id, Double payment) {
        Parkticket parkticket = parkticketRepository.findById(id);

        if (parkticket == null) {
            throw new NoSuchElementException();
        }

        if (parkticket != null && getPaymentAmount(parkticket).equals(payment)) {
            parkticket.setPayment(LocalDateTime.now());
            parkticketRepository.persist(parkticket);
            return true;
        } else {
            return false;
        }

    }

    @GET
    @Path("getAll")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Parkticket> getAll() {
        return parkticketRepository.findAll();
    }

    @GET
    @Path("{id}/isAllowedToExit")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean isAllowedToExit(@PathParam("id") String id) {
        Parkticket parkticket = this.parkticketRepository.findById(id);

        if (parkticket == null) {
            throw new NoSuchElementException();
        }

        LocalDateTime now = LocalDateTime.now();
        return parkticket.getEntered().isBefore(now)
                && parkticket.getPayment() != null
                && now.minusMinutes(parkhausConfig.getToleranceBtwPaymentAndExit()).isBefore(parkticket.getPayment());
    }

}
