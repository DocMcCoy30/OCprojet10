package com.dmc30.reservationservice.model.mappers;

import com.dmc30.reservationservice.data.entity.Reservation;
import com.dmc30.reservationservice.model.dto.ReservationDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    ReservationDto reservationToReservationDto (Reservation reservation);
    Reservation reservationDtoToReservation (ReservationDto reservationDto);
}
