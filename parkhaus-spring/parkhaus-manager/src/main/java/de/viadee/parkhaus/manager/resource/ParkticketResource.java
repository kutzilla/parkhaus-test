package de.viadee.parkhaus.manager.resource;

import de.viadee.parkhaus.manager.config.ParkhausConfig;
import de.viadee.parkhaus.manager.entity.Parkticket;
import de.viadee.parkhaus.manager.entity.Payment;
import de.viadee.parkhaus.manager.repository.ParkticketRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
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

    @PostMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    public String create(@RequestParam("entered") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime entered) {

        Parkticket parkticket = new Parkticket(entered);

        parkticketRepository.save(parkticket);

        return parkticket.getId();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Parkticket get(String id) {
        return parkticketRepository.findById(id).orElse(null);
    }

    @GetMapping(path = "{id}/getPaymentAmount", produces = MediaType.APPLICATION_JSON_VALUE)
    public Double getPaymentAmount(@PathVariable("id") String id) {
        Parkticket parkticket = parkticketRepository.findById(id).orElseThrow(NoSuchElementException::new);
        return getPaymentAmount(parkticket);
    }

    private Double getPaymentAmount(Parkticket parkticket) {
        LocalDateTime to = LocalDateTime.now();

        LocalDateTime from = parkticket.getEntered();

        long parkingTime = ChronoUnit.HOURS.between(from, to);

        return parkhausConfig.getGebuehr() * parkingTime;
    }

    @PutMapping(value = "/makePayment")
    public boolean makePayment(@RequestBody Payment payment) {
        Parkticket parkticket = parkticketRepository.findById(payment.getId()).orElseThrow(NoSuchElementException::new);

        if (parkticket != null && getPaymentAmount(parkticket).equals(payment.getPayment())) {
            parkticket.setPayment(LocalDateTime.now());
            parkticketRepository.save(parkticket);
            return true;
        } else {
            return false;
        }

    }

    @GetMapping(path = "getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Parkticket> getAll() {
        return parkticketRepository.findAll();
    }

    @GetMapping(path = "{id}/isAllowedToExit", produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean isAllowedToExit(@PathVariable("id") String id) {
        Parkticket parkticket = this.parkticketRepository.findById(id).orElseThrow(NoSuchElementException::new);
        LocalDateTime now = LocalDateTime.now();
        return parkticket.getEntered().isBefore(now)
                && parkticket.getPayment() != null
                && now.minusMinutes(parkhausConfig.getToleranceBtwPaymentAndExit()).isBefore(parkticket.getPayment());
    }


}
