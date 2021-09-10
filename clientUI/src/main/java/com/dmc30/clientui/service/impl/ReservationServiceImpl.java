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
    public boolean globalReservationPossibleCheck(Long livreId, String username) throws TechnicalException {
        boolean reservationPossible = true;
        if (!reservationPossibleCheck1(livreId, username)) {
            reservationPossible = false;
            throw new TechnicalException("Réservation impossible : vous avez un emprunt en cours pour ce livre.");
        } else if (!reservationPossibleCheck2(livreId, username)) {
            reservationPossible = false;
            throw new TechnicalException("Une réservation est déjà enregistré pour ce livre.");
        } else if (!reservationPossibleCheck3(livreId)) {
            reservationPossible =false;
            throw new TechnicalException("Réservation impossible : la liste d'attente est pleine.");
        }

        return reservationPossible;
    }

    //DONE : Check1 : pas d'emprunt en cours pour ce livre pour cet utilisateur
    public boolean reservationPossibleCheck1(Long livreId, String username) throws TechnicalException {
        boolean reservation = true;
        Long userId = (userService.getUtilisateurByUsername(username)).getId();
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

    //TODO : Check2 : pas de réservation déjà en cours pour ce livre pour cet utilisateur
    public boolean reservationPossibleCheck2(Long livreId, String username) {
        boolean reservation = true;

        return reservation;
    }

    //TODO : Check2 :  la liste d'attente n'est pas complète
    public boolean reservationPossibleCheck3(Long livreId) {
        boolean reservation = true;

        return reservation;
    }

}
