package com.dmc30.emailservice;

import com.dmc30.emailservice.mail.EmailService;
import com.dmc30.emailservice.service.bean.MailForReservationModel;
import com.dmc30.emailservice.service.bean.ReservationBean;
import com.dmc30.emailservice.service.contract.EmpruntService;
import com.dmc30.emailservice.service.contract.ReservationService;
import com.dmc30.emailservice.service.contract.UtilisateurService;
import com.dmc30.emailservice.service.bean.MailForRetardEmpruntModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Locale;

@Component
public class ScheduledTasks {

    EmpruntService empruntService;
    UtilisateurService utilisateurService;
    ReservationService reservationService;
    EmailService emailService;

    @Autowired
    public ScheduledTasks(EmpruntService empruntService,
                          UtilisateurService utilisateurService,
                          ReservationService reservationService,
                          EmailService emailService) {
        this.empruntService = empruntService;
        this.utilisateurService = utilisateurService;
        this.reservationService = reservationService;
        this.emailService = emailService;
    }

//    @Scheduled(cron = "0 0 0 * * ?") // tous les jours à minuit
//    @Scheduled(cron = "*/30 * * * * *") // toutes les 30 secondes
//    @Scheduled(cron = "0 */3 * * * *") // toutes les 3 minutes
    public void scheduledMailServiceForRetard() throws MessagingException {
        System.out.println("scheduledMailServiceForRetard is running.");
        Locale locale = new Locale("FRANCE");
        List<MailForRetardEmpruntModel> mailToSendList = emailService.createMailListForRetardEmprunt();
        for (MailForRetardEmpruntModel mailToSend : mailToSendList) {
            emailService.sendMailForRetard(mailToSend, locale);
        }
    }

    //DONE T1 : Scheduled Methodde pour envoi mail notification réservation.

//    @Scheduled(cron = "0 */1 * * * *") // toutes les 1 minutes
    @Scheduled(cron = "*/30 * * * * *") // toutes les 30 secondes
    public void scheduledMailServiceForReservation() throws MessagingException {
        System.out.println("scheduledMailServiceForReservation is running.");
        Locale locale = new Locale("FRANCE");
        List<MailForReservationModel> mailToSendList = emailService.createMailListForReservation();
        for (MailForReservationModel mailToSend : mailToSendList) {
            emailService.sendMailForReservation(mailToSend, locale);
        }
    }

}
