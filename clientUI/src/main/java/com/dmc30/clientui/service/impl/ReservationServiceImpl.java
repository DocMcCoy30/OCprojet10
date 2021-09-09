package com.dmc30.clientui.service.impl;

import com.dmc30.clientui.proxy.ReservationServiceProxy;
import com.dmc30.clientui.proxy.UserServiceProxy;
import com.dmc30.clientui.service.contract.ReservationService;
import com.dmc30.clientui.shared.bean.reservation.ReservationBean;
import com.dmc30.clientui.shared.bean.utilisateur.UtilisateurBean;
import com.dmc30.clientui.web.exception.ErrorMessage;
import com.dmc30.clientui.web.exception.TechnicalException;
import feign.FeignException;
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
    public ReservationBean createReservation(Long bibliothequeId, Long livreId, String username) throws TechnicalException {
        ReservationBean savedReservation;
        try {
            UtilisateurBean utilisateurBean = userServiceProxy.findUtilisateurByUsername(username);
            ReservationBean newReservation = new ReservationBean();
            newReservation.setDateReservation(new Date());
            newReservation.setExpiree(false);
            newReservation.setUtilisateurId(utilisateurBean.getId());
            newReservation.setLivreId(livreId);
            newReservation.setBibliothequeId(bibliothequeId);
            savedReservation = reservationServiceProxy.createReservation(newReservation);
        } catch (FeignException e) {
            throw new TechnicalException(ErrorMessage.TECHNICAL_ERROR.getErrorMessage());
        }
        return savedReservation;
    }
}
