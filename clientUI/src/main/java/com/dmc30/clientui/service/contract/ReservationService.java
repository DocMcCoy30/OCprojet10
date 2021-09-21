package com.dmc30.clientui.service.contract;

import com.dmc30.clientui.shared.bean.reservation.ReservationBean;
import com.dmc30.clientui.shared.bean.reservation.ReservationModelBean;
import com.dmc30.clientui.web.exception.TechnicalException;

import java.util.List;

public interface ReservationService {

    ReservationBean createReservation (Long bibliothequeId, Long livreId, String Username) throws TechnicalException;
    boolean globalReservationPossibleCheck(Long livreId, String username, Long bibliothequeId) throws TechnicalException;
    Integer getNombreDeReservation(Long livreId, Long bibliothequeId);
    List<ReservationModelBean> getListeReservationsEnCours(String username, Long bibliothequeId) throws TechnicalException;
    String deleteReservation(Long reservationId) throws TechnicalException;
}
