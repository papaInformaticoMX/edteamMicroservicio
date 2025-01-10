package net.viancom.api_reservations.service;

import net.viancom.api_reservations.connector.CatalogConnector;
import net.viancom.api_reservations.connector.response.CityDTO;
import net.viancom.api_reservations.dto.SegmentDTO;
import net.viancom.api_reservations.enums.APIError;
import net.viancom.api_reservations.exception.EdteamException;
import net.viancom.api_reservations.dto.ReservationDTO;
import net.viancom.api_reservations.model.Reservation;
import net.viancom.api_reservations.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ReservationService {


    private ReservationRepository repository;

    private ConversionService conversionService;

    @Autowired
    private CatalogConnector connector;

    @Autowired
    public ReservationService(ReservationRepository repository,
                              ConversionService conversionService,
                              CatalogConnector connector) {
        this.repository = repository;
        this.conversionService = conversionService;
    }

    public List<ReservationDTO> getReservations() {
        return  conversionService.convert(repository.getReservations(), List.class);
    }

    public ReservationDTO getReservationById(Long id) {
        Optional<Reservation> result = repository.getReservationById(id);
        if(result.isEmpty()) {
            throw  new EdteamException(APIError.RESERVATION_NOT_FOUND);
        }
        return conversionService.convert(result.get(), ReservationDTO.class);
    }

    public ReservationDTO save(ReservationDTO reservation) {
        if(Objects.nonNull(reservation.getId())) {
            throw  new EdteamException(APIError.RESERVATION_WITH_SAME_ID);
        }

        checkCity(reservation);
        Reservation transformed = conversionService.convert(reservation, Reservation.class);
        Reservation result = repository.save(Objects.requireNonNull(transformed));
        return conversionService.convert(result, ReservationDTO.class);
    }

    public ReservationDTO update(Long id, ReservationDTO reservation) {
        if(getReservationById(id) == null) {
            throw  new EdteamException(APIError.RESERVATION_NOT_FOUND);
        }

        checkCity(reservation);
        Reservation transformed = conversionService.convert(reservation, Reservation.class);
        Reservation result = repository.update(id, Objects.requireNonNull(transformed));
        return conversionService.convert(result, ReservationDTO.class);
    }

    public void delete(Long id) {
        if(getReservationById(id) == null) {
            throw  new EdteamException(APIError.RESERVATION_NOT_FOUND);
        }
        repository.delete(id);
    }

    private void checkCity(ReservationDTO reservationDTO){
        for(SegmentDTO segmentDTO: reservationDTO.getItinerary().getSegment()){
            String originCode = segmentDTO.getOrigin();
            CityDTO origin  = connector.getCity(originCode);
            String destinationCode = segmentDTO.getDestination();
            CityDTO destination = connector.getCity(destinationCode);

            if(origin == null || destination == null){
                throw new EdteamException(APIError.VALIDATION_ERROR);
            }else{
                System.out.println(origin.getName());
                System.out.println(destination.getName());
            }
        }
    }
}
