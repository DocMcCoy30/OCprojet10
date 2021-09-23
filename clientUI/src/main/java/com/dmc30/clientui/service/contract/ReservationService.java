package com.dmc30.clientui.service.contract;

import com.dmc30.clientui.shared.bean.reservation.ReservationBean;
import com.dmc30.clientui.shared.bean.reservation.ReservationModelBean;
import com.dmc30.clientui.web.exception.TechnicalException;

import java.util.List;

public interface ReservationService {

    ReservationBean createReservation (Long bibliothequeId, Long livreId, String Username) throws TechnicalException;
    Integer getNombreDeReservation(Long livreId, Long bibliothequeId);
    List<ReservationModelBean> getListeReservationsEnCours(String username, Long bibliothequeId) throws TechnicalException;
    String deleteReservation(Long reservationId) throws TechnicalException;

    boolean globalReservationPossibleCheck(Long livreId, String username, Long bibliothequeId) throws TechnicalException;
    boolean reservationPossibleCheck1(Long livreId, String username) throws TechnicalException;
    boolean reservationPossibleCheck2(Long livreId, String username);
    boolean reservationPossibleCheck3(Long livreId, Long bibliothequeId);
}
