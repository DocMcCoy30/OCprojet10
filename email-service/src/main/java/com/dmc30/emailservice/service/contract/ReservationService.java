package com.dmc30.emailservice.service.contract;

import com.dmc30.emailservice.service.bean.ReservationBean;

import java.util.List;

public interface ReservationService {

    List<ReservationBean> checkReservationsForSendingMail();

}