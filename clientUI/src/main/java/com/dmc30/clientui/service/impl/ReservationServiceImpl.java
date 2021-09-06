package com.dmc30.clientui.service.impl;

import com.dmc30.clientui.proxy.ReservationServiceProxy;
import com.dmc30.clientui.proxy.UserServiceProxy;
import com.dmc30.clientui.service.contract.ReservationService;
import com.dmc30.clientui.shared.bean.reservation.ReservationBean;
import com.dmc30.clientui.shared.bean.utilisateur.UtilisateurBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ReservationServiceImpl implements ReservationService {

    ReservationServiceProxy reservationServiceProxy;
    UserServiceProxy userServiceProxy;

    @Autowired
    public ReservationServiceImpl(ReservationServiceProxy reservationServiceProxy, UserServiceProxy userServiceProxy) {
        this.reservationServiceProxy = reservationServiceProxy;
        this.userServiceProxy = userServiceProxy;
    }



    @Override
    public ReservationBean createReservation(Long livreId, String username) {
        UtilisateurBean utilisateurBean = userServiceProxy.findUtilisateurByUsername(username);

        ReservationBean newReservation = new ReservationBean();
        newReservation.setDateReservation(new Date());
        newReservation.setExpiree(false);
        newReservation.setUtilisateurId(utilisateurBean.getId());
        ReservationBean savedReservation = reservationServiceProxy.createReservation(newReservation);
        return savedReservation;
    }
}
