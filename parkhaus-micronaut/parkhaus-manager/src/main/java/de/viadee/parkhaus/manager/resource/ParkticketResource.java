package de.viadee.parkhaus.manager.resource;

import de.viadee.parkhaus.manager.config.ParkhausConfig;
import de.viadee.parkhaus.manager.entity.Parkticket;
import de.viadee.parkhaus.manager.repository.ParkticketRepository;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;

@Controller("/parkticket")
public class ParkticketResource {

    private ParkticketRepository parkticketRepository;

    private ParkhausConfig parkhausConfig;

    public ParkticketResource(ParkticketRepository ParkticketRepository, ParkhausConfig parkhausConfig) {
        this.parkticketRepository = ParkticketRepository;
        this.parkhausConfig = parkhausConfig;
    }

    @Post(produces = MediaType.TEXT_PLAIN)
    public String create(
            @QueryValue("entered") LocalDateTime entered) {

        Parkticket parkticket = new Parkticket(entered);

        parkticketRepository.save(parkticket);

        return parkticket.getId();
    }

    @Get(produces = MediaType.APPLICATION_JSON)
    public Parkticket get(String id) {
        return parkticketRepository.findById(id).orElse(null);
    }

    @Get(produces = MediaType.APPLICATION_JSON)
    public Double getPaymentAmount(String id) {
        Parkticket parkticket = parkticketRepository.findById(id).orElseThrow(NoSuchElementException::new);
        return getPaymentAmount(parkticket);
    }

    private Double getPaymentAmount(Parkticket parkticket) {
        LocalDateTime to = LocalDateTime.now();

        LocalDateTime from = parkticket.getEntered();

        long parkingTime = ChronoUnit.HOURS.between(from, to);

        return parkhausConfig.getGebuehr() * parkingTime;
    }

    @Put(produces = MediaType.APPLICATION_JSON)
    public boolean makePayment(String id, Double payment) {
        Parkticket parkticket = parkticketRepository.findById(id).orElseThrow(NoSuchElementException::new);

        if (parkticket != null && getPaymentAmount(parkticket).equals(payment)) {
            parkticket.setPayment(LocalDateTime.now());
            parkticketRepository.save(parkticket);
            return true;
        } else {
            return false;
        }

    }

    @Get(produces = MediaType.APPLICATION_JSON)
    public List<Parkticket> getAll() {
        return parkticketRepository.findAll();
    }

    @Get(value = "{id}/isAllowedToExit", produces = MediaType.APPLICATION_JSON)
    public boolean isAllowedToExit(@PathVariable("id") String id) {
        Parkticket parkticket = this.parkticketRepository.findById(id).orElseThrow(NoSuchElementException::new);
        LocalDateTime now = LocalDateTime.now();
        return parkticket.getEntered().isBefore(now)
                && parkticket.getPayment() != null
                && now.minusMinutes(parkhausConfig.getToleranceBtwPaymentAndExit()).isBefore(parkticket.getPayment());
    }


}
