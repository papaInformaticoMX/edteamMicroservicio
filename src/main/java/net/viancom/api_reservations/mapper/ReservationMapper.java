package net.viancom.api_reservations.mapper;

import net.viancom.api_reservations.dto.ReservationDTO;
import net.viancom.api_reservations.model.Reservation;
import org.springframework.core.convert.converter.Converter;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface ReservationMapper extends Converter<Reservation, ReservationDTO> {

    @Override
    ReservationDTO convert(Reservation source);

}