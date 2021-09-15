package com.dmc30.emailservice.service.impl;

import com.dmc30.emailservice.mail.EmailService;
import com.dmc30.emailservice.proxy.EmpruntServiceProxy;
import com.dmc30.emailservice.proxy.LivreServiceProxy;
import com.dmc30.emailservice.proxy.ReservationServiceProxy;
import com.dmc30.emailservice.proxy.UserServiceProxy;
import com.dmc30.emailservice.service.bean.*;
import com.dmc30.emailservice.service.contract.ReservationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReservationServiceImpl implements ReservationService {

    ReservationServiceProxy reservationServiceProxy;
    UserServiceProxy userServiceProxy;
    EmpruntServiceProxy empruntServiceProxy;
    LivreServiceProxy livreServiceProxy;

    @Autowired
    public ReservationServiceImpl(ReservationServiceProxy reservationServiceProxy,
                                  UserServiceProxy userServiceProxy,
                                  EmpruntServiceProxy empruntServiceProxy,
                                  LivreServiceProxy livreServiceProxy) {
        this.reservationServiceProxy = reservationServiceProxy;
        this.userServiceProxy = userServiceProxy;
        this.empruntServiceProxy = empruntServiceProxy;
        this.livreServiceProxy = livreServiceProxy;
    }

    /**
     * Crée la liste des reservations concernées par l'envoi d'un mail de notification
     * @return la liste
     */
    public List<ReservationBean> createReservationsForSendingMail() {
        List<LivreBean> livresRestitues = checkLivresRestitues();
        List<ReservationBean> reservationsForMail = checkReservationsAndSetExpirees(livresRestitues);
        return reservationsForMail;
    }

    /**
     * Retourne une liste de livres pour lesquels un emprunt a été retourné le jour même.
     * @return la liste
     */
    private List<LivreBean> checkLivresRestitues() {
        ObjectMapper mapper = new ObjectMapper();
        List<LivreBean> livresRestitues = new ArrayList<>();
        List<EmpruntBean> empruntsRestitues = empruntServiceProxy.getEmpruntRestitue();
        for (EmpruntBean empruntRestitue : empruntsRestitues) {
            Long ouvrageId = empruntRestitue.getOuvrageId();
            OuvrageBean ouvrageRestitue = livreServiceProxy.getOuvrageById(ouvrageId);
            ResponseEntity<?> responseEntity = livreServiceProxy.getLivreById(ouvrageRestitue.getLivreId());
            LivreBean livreRestitue = mapper.convertValue(responseEntity.getBody(), LivreBean.class);
            livresRestitues.add(livreRestitue);
        }
        return livresRestitues;
    }

    /**
     * Récupère une liste de réservations concernées par un retour d'emprunt, vérifie que la réservation n'est pas expirée,
     * met à jour la réservation si la date d'expiration est dépassée et retourne la liste des reservations pour envoi d'un mail.
     * @param livresRestitues  la liste des livres restitues
     * @return la liste des reservations pour lesquelles un mail doit etre envoyé.
     */
    private List<ReservationBean> checkReservationsAndSetExpirees(List<LivreBean> livresRestitues) {
        Calendar c = Calendar.getInstance(); // starts with today's date and time
        c.add(Calendar.DAY_OF_YEAR, 2);  // advances day by 2
        Date dateExpiration = c.getTime(); // gets modified time
        List<ReservationBean> reservationsForMail = new ArrayList<>();
        for (LivreBean livreRestitue : livresRestitues) {
            Long livreId = livreRestitue.getId();
            List<ReservationBean> reservationsByLivre = reservationServiceProxy.getReservationsByLivreId(livreId);
            boolean constructList = true;
            for (ReservationBean reservationByLivre : reservationsByLivre) {
                while (constructList) {
                    if (reservationByLivre.getDateEnvoiMail() != null) {
                        if (!reservationByLivre.getDateEnvoiMail().after(dateExpiration)) {
                            reservationByLivre.setExpiree(true);
                            reservationServiceProxy.updateReservation(reservationByLivre);
                        }
                    } else if ((!reservationByLivre.isExpiree()) && (!reservationByLivre.isMailEnvoye())) {
                        reservationByLivre.setMailEnvoye(true);
                        reservationByLivre.setDateEnvoiMail(new Date());
                        reservationsForMail.add(reservationByLivre);
                        constructList = false;
                    }
                }
            }
        }
        return reservationsForMail;
    }
}
