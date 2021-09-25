package com.dmc30.reservationservice.service.contract;

import com.dmc30.reservationservice.model.dto.ReservationDto;
import com.dmc30.reservationservice.web.exception.TechnicalException;

import java.util.List;

public interface ReservationService {

    ReservationDto createReservation (ReservationDto reservationDto);
    List<ReservationDto> getAllReservations();
    ReservationDto getReservationById(Long reservationId) throws TechnicalException;
    List<ReservationDto> getReservationsByUserId(Long userId) throws TechnicalException;
    Integer getNombreDeReservation(Long livreId, Long bibliothequeId) throws TechnicalException;
    List<ReservationDto> getReservationByLivreIdAndAndBibliothequeIdOrderByDateReservation(Long livreId, Long bibliothequeId) throws TechnicalException;
    void deleteReservation(Long reservationId);

    //--------------- Mail Service Methode --------------------
    //DONE: MailService Methodes
    List<ReservationDto> getReservationsByLivreIdOrderByDateReservation(Long livreId) throws TechnicalException;
    void updateReservation(ReservationDto reservationDto);
}
