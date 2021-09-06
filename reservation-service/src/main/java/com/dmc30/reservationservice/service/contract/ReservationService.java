package com.dmc30.reservationservice.service.contract;

import com.dmc30.reservationservice.model.dto.ReservationDto;

import java.util.List;

public interface ReservationService {

    ReservationDto createReservation (ReservationDto reservationDto);
    List<ReservationDto> getAllReservations();
    ReservationDto getReservationById(Long reservationId);
}
