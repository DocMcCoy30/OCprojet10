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
    EmailService emailService;


    @Autowired
    public ReservationServiceImpl(ReservationServiceProxy reservationServiceProxy,
                                  UserServiceProxy userServiceProxy,
                                  EmpruntServiceProxy empruntServiceProxy,
                                  LivreServiceProxy livreServiceProxy,
                                  EmailService emailService) {
        this.reservationServiceProxy = reservationServiceProxy;
        this.userServiceProxy = userServiceProxy;
        this.empruntServiceProxy = empruntServiceProxy;
        this.livreServiceProxy = livreServiceProxy;
        this.emailService = emailService;
    }

    @Override
    public List<ReservationBean> checkReservationsForSendingMail() {
        List<LivreBean> livresRestitues = checkLivresRestitues();
        List<ReservationBean> reservationsForMail = checkReservationsAndSetExpirees(livresRestitues);
        return reservationsForMail;
    }


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

    private List<ReservationBean> checkReservationsAndSetExpirees(List<LivreBean> livresRestitues) {
        Calendar c = Calendar.getInstance(); // starts with today's date and time
        c.add(Calendar.DAY_OF_YEAR, 2);  // advances day by 2
        Date dateExpiration = c.getTime(); // gets modified time
        List<ReservationBean> reservationsForMail = new ArrayList<>();
        for (LivreBean livreRestitue : livresRestitues) {
            Long livreId = livreRestitue.getId();
            List<ReservationBean> reservationsByLivre = reservationServiceProxy.getReservationsByLivreId(livreId);
            for (ReservationBean reservationByLivre : reservationsByLivre)
            if (((!reservationByLivre.isExpiree()) && (reservationByLivre.isMailEnvoye()) && ((reservationByLivre.getDateEnvoiMail().after(dateExpiration))))) {
                reservationByLivre.setExpiree(true);
                reservationsForMail.add(reservationByLivre);
            }
        }
        return reservationsForMail;
    }
}
