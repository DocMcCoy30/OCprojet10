package com.dmc30.clientui.service.contract;

import com.dmc30.clientui.shared.bean.reservation.ReservationBean;

public interface ReservationService {

    ReservationBean createReservation (Long livreId, String Username);
}
