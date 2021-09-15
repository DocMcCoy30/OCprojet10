package com.dmc30.emailservice.mail;

import com.dmc30.emailservice.service.bean.*;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Locale;

public interface EmailService {

//-------------------- Méthodes Mail pour retard prêt -------------------------
    MailForRetardEmpruntModel expiredEmpruntEmailMaker(UtilisateurBean utilisateur, List<LivreForMailBean> livres);
    List<MailForRetardEmpruntModel> createMailListForRetardEmprunt();
    void sendMailForRetard(MailForRetardEmpruntModel mailForRetardEmpruntModel, final Locale locale) throws MessagingException;

    // ---------------- Méthodes Mail pour Notification Réservation --------------------
    List<MailForReservationModel> createMailListForReservation();
    void sendMailForReservation(MailForReservationModel mailForReservation, final Locale locale) throws MessagingException;


    void sendSimpleMessage(String to, String subject, String text);



}
