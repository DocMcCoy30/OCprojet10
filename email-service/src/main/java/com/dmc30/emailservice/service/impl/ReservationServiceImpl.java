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

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
     *
     * @return la liste
     */
    public List<ReservationBean> createReservationsForSendingMail() {
        List<LivreBean> listOfLivresRestitues = checkLivresRestitues();
        List<ReservationBean> reservationsForMail = checkReservationsAndSetExpirees(listOfLivresRestitues);
        return reservationsForMail;
    }

    /**
     * Retourne une liste de livres pour lesquels un emprunt a été retourné le jour même.
     *
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
     *
     * @param listOfLivresRestitues les listes des livres restitues par bibliotheque
     * @return la liste des reservations pour lesquelles un mail doit etre envoyé.
     */
    private List<ReservationBean> checkReservationsAndSetExpirees(List<LivreBean> listOfLivresRestitues) {
        Calendar c = Calendar.getInstance();
        ZoneId zoneId = ZoneId.of( "Europe/Paris" );
        ZonedDateTime today = ZonedDateTime.ofInstant( Instant.now() , zoneId );
        ZonedDateTime dateExpiration = null;
        List<ReservationBean> reservationsForMail = new ArrayList<>();
        List<ReservationBean> reservationsByLivre;
//        for(List<LivreBean> livresRestituesParBibliotheque : listOfLivresRestitues) {
            for (LivreBean livreRestitue : listOfLivresRestitues) {
                boolean mailEnvoye = false;
                Long livreId = livreRestitue.getId();
                reservationsByLivre = reservationServiceProxy.getReservationsByLivreId(livreId);
                //si la liste est >0
                if (reservationsByLivre.size() > 0) {
                    //vérifier les dates d'expiration et update
                    for (ReservationBean reservationByLivre : reservationsByLivre) {
                        if (reservationByLivre.getDateEnvoiMailTz() != null) {
                            dateExpiration = (reservationByLivre.getDateEnvoiMailTz()).plusDays(2);
//                            c.setTime(reservationByLivre.getDateEnvoiMailTz());
//                            c.add(Calendar.DAY_OF_YEAR, 2);
//                            dateExpiration = c.getTime();
                        }
                        if ((reservationByLivre.isMailEnvoye()) && (today.isAfter(dateExpiration))) {
                            reservationByLivre.setExpiree(true);
                            reservationServiceProxy.updateReservation(reservationByLivre);
                        }
                        //si date expiration ok, vérifier si mail envoyé
                        if ((reservationByLivre.isMailEnvoye()) && (today.isBefore(dateExpiration))) {
                            //si mailEnvoye et !expiree  dans la liste on ne fait rien
                            mailEnvoye = true;
                        }
                        if ((!mailEnvoye) && (!reservationByLivre.isExpiree())) {
                            //si !mailEnvoye on ajoute à la liste à retourner et on passe à la liste de reservation pour le livre suivant
                            reservationByLivre.setMailEnvoye(true);
                            reservationByLivre.setDateEnvoiMailTz(today);
                            reservationServiceProxy.updateReservation(reservationByLivre);
                            reservationsForMail.add(reservationByLivre);
                            mailEnvoye = true;
                        }
                    }
                }
            }
//        }
        return reservationsForMail;
    }
}