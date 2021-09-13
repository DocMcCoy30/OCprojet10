package com.dmc30.emailservice.service.impl;

import com.dmc30.emailservice.proxy.ReservationServiceProxy;
import com.dmc30.emailservice.service.bean.ReservationBean;
import com.dmc30.emailservice.service.contract.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {

    ReservationServiceProxy reservationServiceProxy;

    @Autowired
    public ReservationServiceImpl(ReservationServiceProxy reservationServiceProxy) {
        this.reservationServiceProxy = reservationServiceProxy;
    }

    @Override
    public List<ReservationBean> getReservationsExpireesByUserId(Long UserId) {
        List<ReservationBean> reservationExpirees = reservationServiceProxy.getReservationsExpireesByUserId(UserId);
        return reservationExpirees;
    }
}
