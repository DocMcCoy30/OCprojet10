package com.dmc30.clientui.service.impl;

import com.dmc30.clientui.proxy.ReservationServiceProxy;
import com.dmc30.clientui.service.contract.LivreService;
import com.dmc30.clientui.service.contract.OuvrageService;
import com.dmc30.clientui.shared.bean.bibliotheque.CreateEmpruntBean;
import com.dmc30.clientui.shared.bean.bibliotheque.EmpruntBean;
import com.dmc30.clientui.proxy.EmpruntServiceProxy;
import com.dmc30.clientui.service.contract.EmpruntService;
import com.dmc30.clientui.shared.bean.bibliotheque.OuvrageBean;
import com.dmc30.clientui.shared.bean.reservation.ReservationBean;
import com.dmc30.clientui.web.exception.ErrorMessage;
import com.dmc30.clientui.web.exception.TechnicalException;
import feign.FeignException;
import org.apache.commons.lang.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class EmpruntServiceImpl implements EmpruntService {

    Logger logger = LogManager.getLogger(EmpruntServiceImpl.class);

    EmpruntServiceProxy empruntServiceProxy;
    ReservationServiceProxy reservationServiceProxy;
    OuvrageService ouvrageService;
    LivreService livreService;

    @Autowired
    public EmpruntServiceImpl(EmpruntServiceProxy empruntServiceProxy,
                              OuvrageService ouvrageService,
                              ReservationServiceProxy reservationServiceProxy,
                              LivreService livreService) {
        this.empruntServiceProxy = empruntServiceProxy;
        this.ouvrageService = ouvrageService;
        this.reservationServiceProxy = reservationServiceProxy;
        this.livreService = livreService;
    }

    @Override
    public EmpruntBean createEmprunt(CreateEmpruntBean createEmpruntBean) throws TechnicalException {
        EmpruntBean empruntBean = new EmpruntBean();
        try {
            empruntBean = empruntServiceProxy.createEmprunt(createEmpruntBean);
            Long livreEmprunteId = livreService.getLivreIdByOuvrageId(empruntBean.getOuvrageId());
            List<ReservationBean> reservationsEnCours = reservationServiceProxy.getReservationsByUserId(empruntBean.getUtilisateurId());
            //DONE T2 : réservation annuler lorsque livre emprunté
            for (ReservationBean reservation:reservationsEnCours) {
                if (reservation.getLivreId().equals(livreEmprunteId)) {
                    reservationServiceProxy.deleteReservation(reservation.getId());
                }
            }
        } catch (FeignException e) {
            throw new TechnicalException(ErrorMessage.TECHNICAL_ERROR.getErrorMessage());
        }
        return empruntBean;
    }

    @Override
    public List<EmpruntBean> getEmpruntsEnCours(Long bibliothequeId) throws TechnicalException {
        List<EmpruntBean> empruntBeanList = new ArrayList<>();
        try {
            empruntBeanList = empruntServiceProxy.findEmpruntEnCours(bibliothequeId);
        } catch (FeignException e) {
            throw new TechnicalException(ErrorMessage.TECHNICAL_ERROR.getErrorMessage());
        }
        return empruntBeanList;
    }

    @Override
    public List<EmpruntBean> getEmpruntByUtilisateurId(Long utilisateurId) throws TechnicalException {
        List<EmpruntBean> empruntBeanList = new ArrayList<>();
        try {
            empruntBeanList = empruntServiceProxy.findEmpruntByUtilisateurId(utilisateurId);
        } catch (FeignException e) {
            throw new TechnicalException(ErrorMessage.TECHNICAL_ERROR.getErrorMessage());
        }
        return empruntBeanList;
    }


    @Override
    public void retournerEmprunt(Long empruntId, String ouvrageId) throws TechnicalException {
        try {
            empruntServiceProxy.retournerEmprunt(empruntId, ouvrageId);

        } catch (FeignException e) {
            throw new TechnicalException(ErrorMessage.TECHNICAL_ERROR.getErrorMessage());
        }
    }

    @Override
    public void prolongerEmprunt(Long empruntId) throws TechnicalException {
        try {
            empruntServiceProxy.prolongerEmprunt(empruntId);

        } catch (FeignException e) {
            throw new TechnicalException(ErrorMessage.TECHNICAL_ERROR.getErrorMessage());
        }
    }

    @Override
    public String getDateDeRetourPrevue(Long livreId, Long bibliothequeId) throws TechnicalException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE dd MMMMM yyyy");
        List<EmpruntBean> empruntBeans = new ArrayList<>();
        Date dateRetourEmprunt = null;
        Date dateRetourPrevue = DateUtils.addMonths(new Date(), 2);
        try {
            //récupérer la liste des ouvrages correspondants au livre et à la bibliotheque
            List<OuvrageBean> ouvrageBeans = ouvrageService.getOuvrageByLivreIdAndBibliothequeId(livreId, bibliothequeId);
            //récupérer la liste des emprunts non restitués correspondants aux ouvrages
            for (OuvrageBean ouvrage : ouvrageBeans) {
                if (ouvrage.isEmprunte()) {
                    EmpruntBean empruntBean = empruntServiceProxy.getEmpruntEnCoursByOuvrageId(ouvrage.getId());
                    empruntBeans.add(empruntBean);
                }
            }
            //Comparer pour chaque ouvrage la date de restitution ou prolongation et récupérer la plus proche.
            for (EmpruntBean empruntEnCours:empruntBeans) {
                if(!empruntEnCours.isProlongation()) {
                    dateRetourEmprunt = empruntEnCours.getDateRestitution();
                }else {
                    dateRetourEmprunt = empruntEnCours.getDateProlongation();
                }
                if(dateRetourEmprunt.before(dateRetourPrevue)) {
                    dateRetourPrevue = dateRetourEmprunt;
                }
            }
        } catch (FeignException e) {
            throw new TechnicalException(ErrorMessage.TECHNICAL_ERROR.getErrorMessage());
        }
        return dateFormat.format(dateRetourPrevue);
    }

}
