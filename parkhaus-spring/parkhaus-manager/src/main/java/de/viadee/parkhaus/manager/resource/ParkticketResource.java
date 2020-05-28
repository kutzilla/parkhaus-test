package de.viadee.parkhaus.manager.resource;

import de.viadee.parkhaus.manager.config.ParkhausConfig;
import de.viadee.parkhaus.manager.entity.Parkticket;
import de.viadee.parkhaus.manager.repository.ParkticketRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/parkticket")
public class ParkticketResource {

    private ParkticketRepository parkticketRepository;

    private ParkhausConfig parkhausConfig;

    public ParkticketResource(ParkticketRepository ParkticketRepository, ParkhausConfig parkhausConfig) {
        this.parkticketRepository = ParkticketRepository;
        this.parkhausConfig = parkhausConfig;
    }

    @PostMapping
    public String create(@RequestParam("entered") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime entered) {

        Parkticket parkticket = new Parkticket(entered);

        parkticketRepository.persist(parkticket);

        return parkticket.getId();
    }

    @GetMapping
    public Parkticket get(String id) {
        return parkticketRepository.findById(id);
    }

   @GetMapping(path = "{id}/getPaymentAmount")
    public Double getPaymentAmount(@PathVariable("id") String id) {
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

    @PutMapping(value = "/{id}/makePayment")
    public Boolean makePayment(@RequestBody Double payment, @PathVariable String id) {
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

    @GetMapping(path = "getAll")
    public Collection<Parkticket> getAll() {
        return parkticketRepository.findAll();
    }

    @GetMapping(path = "/{id}/isAllowedToExit")
    public Boolean isAllowedToExit(@PathVariable("id") String id) {
        Parkticket parkticket = parkticketRepository.findById(id);

        if (parkticket == null) {
            throw new NoSuchElementException();
        }

        LocalDateTime now = LocalDateTime.now();
        return parkticket.getEntered().isBefore(now)
                && parkticket.getPayment() != null
                && now.minusMinutes(parkhausConfig.getToleranceBtwPaymentAndExit()).isBefore(parkticket.getPayment());
    }

}
