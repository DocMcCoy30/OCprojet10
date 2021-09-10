package com.dmc30.clientui.service.contract;

import com.dmc30.clientui.shared.bean.reservation.ReservationBean;
import com.dmc30.clientui.web.exception.TechnicalException;

public interface ReservationService {

    ReservationBean createReservation (Long bibliothequeId, Long livreId, String Username) throws TechnicalException;

    boolean globalReservationPossibleCheck(Long livreId, String username) throws TechnicalException;
}
