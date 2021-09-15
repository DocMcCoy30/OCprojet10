package com.dmc30.reservationservice.service.contract;

import com.dmc30.reservationservice.model.dto.ReservationDto;

import java.util.List;

public interface ReservationService {

    ReservationDto createReservation (ReservationDto reservationDto);
    List<ReservationDto> getAllReservations();
    ReservationDto getReservationById(Long reservationId);
    List<ReservationDto> getReservationsByUserId(Long userId);
    Integer getNombreDeReservation(Long livreId, Long bibliothequeId);
    List<ReservationDto> getReservationByLivreIdAndAndBibliothequeIdOrderByDateReservation(Long livreId, Long bibliothequeId);
    void deleteReservation(Long reservationId);

    //--------------- Mail Service Methode --------------------

    List<ReservationDto> getReservationsByLivreId(Long livreId);
}
