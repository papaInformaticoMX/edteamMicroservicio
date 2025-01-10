package net.viancom.api_reservations.mapper;

import net.viancom.api_reservations.dto.ReservationDTO;
import net.viancom.api_reservations.model.Reservation;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;


@Mapper(componentModel = "spring")
public interface ReservationDTOMapper extends Converter<ReservationDTO, Reservation> {

    @Override
    Reservation convert(ReservationDTO source);

}