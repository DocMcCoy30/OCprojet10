package com.dmc30.emailservice.mail;

import com.dmc30.emailservice.service.bean.MailForRetardEmpruntModelBean;
import com.dmc30.emailservice.service.bean.LivreForMailBean;
import com.dmc30.emailservice.service.bean.UtilisateurBean;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Locale;

public interface EmailService {

//-------------------- Méthodes Mail pour retard prêt -------------------------
    MailForRetardEmpruntModelBean expiredEmpruntEmailMaker(UtilisateurBean utilisateur, List<LivreForMailBean> livres);
    List<MailForRetardEmpruntModelBean> createMailListForRetardEmprunt();
    void sendMailForRetard(MailForRetardEmpruntModelBean mailForRetardEmpruntModelBean, final Locale locale) throws MessagingException;




}
