package com.dmc30.clientui.service.impl;

import com.dmc30.clientui.proxy.LivreServiceProxy;
import com.dmc30.clientui.proxy.ReservationServiceProxy;
import com.dmc30.clientui.proxy.UserServiceProxy;
import com.dmc30.clientui.service.contract.EmpruntService;
import com.dmc30.clientui.service.contract.OuvrageService;
import com.dmc30.clientui.service.contract.ReservationService;
import com.dmc30.clientui.service.contract.UserService;
import com.dmc30.clientui.shared.bean.bibliotheque.EmpruntBean;
import com.dmc30.clientui.shared.bean.reservation.ReservationBean;
import com.dmc30.clientui.shared.bean.utilisateur.UtilisateurBean;
import com.dmc30.clientui.web.exception.ErrorMessage;
import com.dmc30.clientui.web.exception.TechnicalException;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class ReservationServiceImpl implements ReservationService {

    ReservationServiceProxy reservationServiceProxy;
    LivreServiceProxy livreServiceProxy;
    EmpruntService empruntService;
    OuvrageService ouvrageService;
    UserService userService;

    @Autowired
    public ReservationServiceImpl(ReservationServiceProxy reservationServiceProxy,
                                  LivreServiceProxy livreServiceProxy,
                                  UserServiceProxy userServiceProxy,
                                  EmpruntService empruntService,
                                  OuvrageService ouvrageService,
                                  UserService userService) {
        this.reservationServiceProxy = reservationServiceProxy;
        this.livreServiceProxy = livreServiceProxy;
        this.empruntService = empruntService;
        this.ouvrageService = ouvrageService;
        this.userService = userService;
    }


    @Override
    public ReservationBean createReservation(Long bibliothequeId, Long livreId, String username) throws TechnicalException {
        ReservationBean savedReservation;
        try {
            UtilisateurBean utilisateurBean = userService.getUtilisateurByUsername(username);
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


    //TODO : checkReservationPossible :
    // -> Check1 : pas d'emprunt en cours pour ce livre pour cet utilisateur
    public boolean check1(Long livreId, Long userId) throws TechnicalException {
        boolean reservation = true;
        List<EmpruntBean> emprunts = empruntService.getEmpruntByUtilisateurId(userId);
        if (emprunts.isEmpty()) {
            reservation = true;
        } else {
            for (EmpruntBean empruntBean : emprunts) {
                Long livreEmprunteId = livreServiceProxy.getLivreIdByOuvrageId(empruntBean.getOuvrageId());
                if ( (Objects.equals(livreId, livreEmprunteId)) && (!empruntBean.isRestitution())) {
                    reservation = false;
                    break;
                }
            }
        }
        return reservation;
    }

    //TODO : checkReservationPossible dans ReservationService :
    // -> Check2 : pas de réservation déjà en cours pour ce livre pour cet utilisateur
    public boolean check2(Long livreId, Long userId) {
        boolean reservation = false;

        return reservation;
    }





}
