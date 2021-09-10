package com.dmc30.clientui.web.controller;

import com.dmc30.clientui.service.contract.LivreService;
import com.dmc30.clientui.service.contract.ReservationService;
import com.dmc30.clientui.shared.UtilsMethodService;
import com.dmc30.clientui.shared.bean.livre.LivreResponseModelBean;
import com.dmc30.clientui.shared.bean.reservation.ReservationBean;
import com.dmc30.clientui.web.exception.TechnicalException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class ReservationController {

    Logger logger = LogManager.getLogger(ReservationController.class);

    UtilsMethodService utilsMethodService;
    ReservationService reservationService;
    LivreService livreService;

    @Autowired
    public ReservationController(UtilsMethodService utilsMethodService, ReservationService reservationService, LivreService livreService) {
        this.utilsMethodService = utilsMethodService;
        this.reservationService = reservationService;
        this.livreService = livreService;
    }

    //TODO : javadoc

    @PostMapping("/createReservation")
    public ModelAndView createReservation(@RequestParam("bibliothequeId") Long bibliothequeId,
                                          @RequestParam(value = "livreId") Long livreId,
                                          @RequestParam(value = "username") String username) throws TechnicalException {
        ModelAndView theModel = new ModelAndView("accueil");
        utilsMethodService.setBibliothequeForTheVue(theModel, bibliothequeId);
        try {
            ReservationBean newReservation = reservationService.createReservation(bibliothequeId, livreId, username);
            theModel.addObject("reservation", newReservation);
            List<LivreResponseModelBean> livres = livreService.get12LastLivres();
            theModel.addObject("lastLivres", livres);
            String message = "Votre réservation a bien été enregistrée";
            theModel.addObject("message", message);
        } catch (TechnicalException e) {
            String errorMessage = e.getMessage();
            theModel.addObject("errorMessage", errorMessage);
        }
        return theModel;
    }

    public ModelAndView annulerReservation() {
        ModelAndView theModel = new ModelAndView("profil-utilisateur");
        //TODO : implémenter méthode annulerReservation dans ReservationService
        return theModel;
    }
}
