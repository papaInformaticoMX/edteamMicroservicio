package net.viancom.api_reservations.controller;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import net.viancom.api_reservations.controller.resource.ReservationResource;
import net.viancom.api_reservations.dto.ReservationDTO;
import net.viancom.api_reservations.enums.APIError;
import net.viancom.api_reservations.exception.EdteamException;
import net.viancom.api_reservations.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/reservation")
public class ReservationController implements ReservationResource {
    private final ReservationService service;

    @Autowired
    public ReservationController(ReservationService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ReservationDTO>> getReservations() {
        List<ReservationDTO> response = service.getReservations();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ReservationDTO> getReservationById(@Min(1) @PathVariable Long id) {
        ReservationDTO response = service.getReservationById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    @RateLimiter(name="post-reservation", fallbackMethod = "fallbackPost")
    public ResponseEntity<ReservationDTO> save(@RequestBody @Valid ReservationDTO reservation) {
        ReservationDTO response = service.save(reservation);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationDTO> update(@Min(1) @PathVariable Long id, @RequestBody @Valid ReservationDTO reservation) {
        ReservationDTO response = service.update(id, reservation);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Min(1) @PathVariable Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    //@RateLimiter(name="post-reservation", fallbackMethod = "fallbackPost")
    public ResponseEntity<ReservationDTO> fallbackPost(@RequestBody @Valid ReservationDTO reservation, RequestNotPermitted e) {
        System.out.println("calling to fallbackpost");
        throw new EdteamException(APIError.EXCEED_NUMBER_REQUEST);

    }
}