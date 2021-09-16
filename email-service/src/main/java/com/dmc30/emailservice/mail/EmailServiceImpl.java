package com.dmc30.emailservice.mail;

import com.dmc30.emailservice.service.bean.*;
import com.dmc30.emailservice.service.contract.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
public class EmailServiceImpl implements EmailService {

    private static final String EMAIL_RETARD_EMPRUNT = "email-retard_emprunt";
    private static final String EMAIL_NOTIFICATION_RESERVATION = "email-notification-reservation";
    private static final String NOREPLY_ADDRESS = "noreply@BibliothequeDeNimes.com";
    private static final String SUBJECT = "Non restitution de votre (vos) emprunt(s)";
    private static final String SUBJECT2 = "Le livre que vous avez réservé est à nouveau disponible !";

    private EmpruntService empruntService;
    private LivreService livreService;
    private UtilisateurService utilisateurService;
    private BibliothequeService bibliothequeService;
    private ReservationService reservationService;
    private JavaMailSender emailSender;
    TemplateEngine htmlTemplateEngine;

    @Autowired
    public EmailServiceImpl(EmpruntService empruntService,
                            LivreService livreService,
                            UtilisateurService utilisateurService,
                            BibliothequeService bibliothequeService,
                            ReservationService reservationService,
                            JavaMailSender emailSender,
                            TemplateEngine htmlTemplateEngine) {
        this.empruntService = empruntService;
        this.livreService = livreService;
        this.utilisateurService = utilisateurService;
        this.bibliothequeService = bibliothequeService;
        this.reservationService = reservationService;
        this.emailSender = emailSender;
        this.htmlTemplateEngine = htmlTemplateEngine;
    }


    @Override
    public MailForRetardEmpruntModel expiredEmpruntEmailMaker(
            UtilisateurBean utilisateur,
            List<LivreForMailBean> livres) {
        MailForRetardEmpruntModel mailForRetardEmpruntModel = new MailForRetardEmpruntModel();
        mailForRetardEmpruntModel.setUserId(utilisateur.getId());
        mailForRetardEmpruntModel.setUsername(utilisateur.getUsername());
        mailForRetardEmpruntModel.setPrenom(utilisateur.getPrenom());
        mailForRetardEmpruntModel.setNom(utilisateur.getNom());
        mailForRetardEmpruntModel.setEmail(utilisateur.getEmail());
        mailForRetardEmpruntModel.setLivres(livres);
        return mailForRetardEmpruntModel;
    }

    @Override
    public List<MailForRetardEmpruntModel> createMailListForRetardEmprunt() {
        List<MailForRetardEmpruntModel> mailToCreateList = new ArrayList<>();
        List<LivreForMailBean> livres = new ArrayList<>();
        List<EmpruntBean> expiredempruntsList = new ArrayList<>();
        List<Long> utilisateursEnRetardId = empruntService.findUtilisateurEnRetard();
        for (Long utisateurEnRetardId : utilisateursEnRetardId) {
            UtilisateurBean utilisateurBean = utilisateurService.findUtilisateurById(utisateurEnRetardId);
            expiredempruntsList = empruntService.findExpiredempruntsByUtilisateurId(utisateurEnRetardId);
            for (EmpruntBean empruntBean : expiredempruntsList) {
                LivreForMailBean livre = livreService.getTitreDuLivre(empruntBean.getOuvrageId());
                livres.add(livre);
            }
            MailForRetardEmpruntModel newMail = expiredEmpruntEmailMaker(utilisateurBean, livres);
            mailToCreateList.add(newMail);
            livres = new ArrayList<>();
        }
        return mailToCreateList;
    }

    /*
     * Send HTML mail (simple)
     */
    public void sendMailForRetard(MailForRetardEmpruntModel mailForRetardEmpruntModel, final Locale locale) throws MessagingException {
        // Prepare the evaluation context
        final Context ctx = new Context(locale);
        ctx.setVariable("name", mailForRetardEmpruntModel.getUsername());
        ctx.setVariable("livres", mailForRetardEmpruntModel.getLivres());
        ctx.setVariable("subscriptionDate", new Date());

        // Prepare message using a Spring helper
        final MimeMessage mimeMessage = this.emailSender.createMimeMessage();
        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
        message.setSubject(SUBJECT);
        message.setFrom(NOREPLY_ADDRESS);
        message.setTo(mailForRetardEmpruntModel.getEmail());

        // Create the HTML body using Thymeleaf
        final String htmlContent = this.htmlTemplateEngine.process(EMAIL_RETARD_EMPRUNT, ctx);
        message.setText(htmlContent, true /* isHtml */);

        // Send email
        this.emailSender.send(mimeMessage);
    }

    @Override
    public List<MailForReservationModel> createMailListForReservation() {
        List<ReservationBean> reservationANotifier = reservationService.createReservationsForSendingMail();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE dd MMMMM yyyy");
        List<MailForReservationModel> mailForReservationModels = new ArrayList<>();

        for (ReservationBean reservation : reservationANotifier) {
            MailForReservationModel mailForReservationModel = new MailForReservationModel();
            UtilisateurBean utilisateur = utilisateurService.findUtilisateurById(reservation.getUtilisateurId());
            LivreBean livre = livreService.getLivreById(reservation.getLivreId());
            BibliothequeBean bibliotheque= bibliothequeService.getBibliothequeById(reservation.getBibliothequeId());

            mailForReservationModel.setPrenom(utilisateur.getPrenom());
            mailForReservationModel.setNom(utilisateur.getNom());
            mailForReservationModel.setUsername(utilisateur.getUsername());
            mailForReservationModel.setEmail(utilisateur.getEmail());
            mailForReservationModel.setTitre(livre.getTitre());
            mailForReservationModel.setDateReservation(dateFormat.format(reservation.getDateReservation()));
            mailForReservationModel.setBibliotheque(bibliotheque.getNom());

            mailForReservationModels.add(mailForReservationModel);
        }
        return mailForReservationModels;
    }


    @Override
    public void sendMailForReservation(MailForReservationModel mailForReservation, final Locale locale) throws MessagingException {
        // Prepare the evaluation context
        final Context ctx = new Context(locale);

        ctx.setVariable("username", mailForReservation.getUsername());
        ctx.setVariable("prenom", mailForReservation.getPrenom());
        ctx.setVariable("nom", mailForReservation.getNom());
        ctx.setVariable("livre", mailForReservation.getTitre());
        ctx.setVariable("dateReservation", mailForReservation.getDateReservation());
        ctx.setVariable("bibliotheque", mailForReservation.getBibliotheque());

        // Prepare message using a Spring helper
        final MimeMessage mimeMessage = this.emailSender.createMimeMessage();
        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
        message.setSubject(SUBJECT2);
        message.setFrom(NOREPLY_ADDRESS);
        message.setTo(mailForReservation.getEmail());

        // Create the HTML body using Thymeleaf
        final String htmlContent = this.htmlTemplateEngine.process(EMAIL_NOTIFICATION_RESERVATION, ctx);
        message.setText(htmlContent, true /* isHtml */);

        // Send email
        this.emailSender.send(mimeMessage);
    }



    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(NOREPLY_ADDRESS);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);

            emailSender.send(message);
        } catch (MailException exception) {
            exception.printStackTrace();
        }
    }

}

