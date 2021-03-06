package com.dmc30.clientui.web.controller;

import com.dmc30.clientui.security.PasswordEncoderHelper;
import com.dmc30.clientui.service.contract.*;
import com.dmc30.clientui.shared.UtilsMethodService;
import com.dmc30.clientui.shared.bean.bibliotheque.BibliothequeBean;
import com.dmc30.clientui.shared.bean.livre.LivreResponseModelBean;
import com.dmc30.clientui.shared.bean.reservation.ReservationModelBean;
import com.dmc30.clientui.shared.bean.utilisateur.CreateAbonneBean;
import com.dmc30.clientui.shared.bean.utilisateur.LoginRequestBean;
import com.dmc30.clientui.shared.bean.utilisateur.UtilisateurBean;
import com.dmc30.clientui.web.exception.TechnicalException;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * A terme, connecte l'interface utilisateur (Front) avec le microservice User (user-service) via la couche service et proxy.
 */
@Controller
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);
    UtilsMethodService utilsMethodService;
    UserService userService;
    BibliothequeService bibliothequeService;
    OuvrageService ouvrageService;
    EmpruntService empruntService;
    LivreService livreService;
    ReservationService reservationService;
    PasswordEncoderHelper passwordEncoderHelper;

    @Autowired
    public UserController(UtilsMethodService utilsMethodService,
                          UserService userService,
                          BibliothequeService bibliothequeService,
                          EmpruntService empruntService,
                          OuvrageService ouvrageService,
                          LivreService livreService,
                          ReservationService reservationService,
                          PasswordEncoderHelper passwordEncoderHelper) {
        this.utilsMethodService = utilsMethodService;
        this.userService = userService;
        this.bibliothequeService = bibliothequeService;
        this.empruntService = empruntService;
        this.ouvrageService = ouvrageService;
        this.livreService = livreService;
        this.reservationService = reservationService;
        this.passwordEncoderHelper = passwordEncoderHelper;
    }

    /**
     * m??thode test - affiche la page accueil.
     *
     * @param theModel le model renvoy??.
     * @return la vue avec un message compte rendu de v??rification du bon fonctionnement du service.
     */
    @GetMapping(path = "/check")
    public String status(Model theModel) {
        String message = userService.check();
        theModel.addAttribute("statut", message);
        return "accueil";
    }

    /**
     * Affiche la vue login-page pour que l'utilisateur renseigne ses identifiants / mot de passe ou la page index en cas de demande de d??connexion
     *
     * @param logout         si pr??sent, deconnecte l'utilisateur.
     * @param bibliothequeId l'identifiant de la biblioth??que selectionn??e
     * @return la vue login-page avec un objet LoginRequest (identifiant/mot de passe) ou la page index avec la liste des biblioth??que en cas de deconnexion.
     */
    @GetMapping(path = "/login")
    public ModelAndView loginPage(@RequestParam(value = "logout", required = false) String logout,
                                  @RequestParam(value = "bibliothequeId", required = false) Long bibliothequeId) {
        ModelAndView theModel = new ModelAndView("login-page");
        String errorMessage = "";
        utilsMethodService.setBibliothequeForTheVue(theModel, bibliothequeId);
        LoginRequestBean user = new LoginRequestBean();
        List<BibliothequeBean> bibliotheques = new ArrayList<>();
        if (logout != null) {
            try {
                 bibliotheques= bibliothequeService.getBibliotheques();
            }catch (TechnicalException e) {
                errorMessage = e.getMessage();
            }
            theModel.addObject("bibliotheques", bibliotheques);
            theModel.addObject("logoutMessage", "Vous ??tes deconnect?? !");
            theModel.addObject("errorMessage", errorMessage);
            theModel.setViewName("index");
        }
        theModel.addObject("user", user);
        return theModel;
    }

    /**
     * Traitement des donn??es pour identification et connexion s??curis??e de l'utilisateur (SpringSecurity)
     *
     * @param userLoginDetails Les identifiants/mot de passe de l'utilisateur entr??s sur la page login-page
     * @param bibliothequeId   L'identifiant de la biblioth??que selectionn??e.
     * @param theModel         Le Model renvoy??.
     * @return la vue accueil si l'identification est un succ??s, la page login avec un messge d'erreur si les identifiant/mot de passe renseign??s sont erron??s.
     */
    @PostMapping(path = "/login")
    public ModelAndView secureLogin(@ModelAttribute LoginRequestBean userLoginDetails,
                                    @RequestParam(value = "bibliothequeId", required = false) Long bibliothequeId,
                                    ModelAndView theModel) {
        utilsMethodService.setBibliothequeForTheVue(theModel, bibliothequeId);
        UtilisateurBean abonneDto = new UtilisateurBean();
        String errorMessage = "";
        String viewName = "login-page";
        try {
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            LoginRequestBean loginRequestBean = modelMapper.map(userLoginDetails, LoginRequestBean.class);
            String[] result = userService.secureLogin(loginRequestBean);
            switch (result[0]) {
                case "OK":
                    abonneDto = userService.getUtilisateurByPublicId(result[1]);
                    theModel.addObject("abonne", abonneDto);
                    List<LivreResponseModelBean> lastLivres = livreService.get12LastLivres();
                    theModel.addObject("lastLivres", lastLivres);
                    viewName = "accueil";
                    break;
                case "KO":
                    errorMessage = result[1];
                    theModel.addObject("errorMessage", errorMessage);
                    theModel.addObject("user", new LoginRequestBean());
            }
        } catch (TechnicalException e) {
            errorMessage = e.getMessage();
            theModel.addObject("errorMessage", errorMessage);
        }
        theModel.setViewName(viewName);
        return theModel;
    }

    /**
     * Affiche la page signin-page permettant ?? l'utilisateur de renseigner ses donn??es personnelles afin de cr??er un compte (abonn??)
     *
     * @param bibliothequeId L'identifiant de la biblioth??que selectionn??e.
     * @return la vue signin-page avec la biblioth??que selectionn??e.
     */
    @GetMapping(path = "/signin")
    public ModelAndView signinPage(@RequestParam(value = "bibliothequeId", required = false) Long bibliothequeId) {
        ModelAndView theModel = new ModelAndView("signin-page");
        utilsMethodService.setBibliothequeForTheVue(theModel, bibliothequeId);
        CreateAbonneBean abonne = new CreateAbonneBean();
        theModel.addObject("abonne", abonne);
        return theModel;
    }

    /**
     * Traitement des donn??es pour cr??ation d'un compte abonn?? avec cryptage du mot de passe.
     *
     * @param userDetails    Les donn??es personnelles de l'utilisateur n??cessaire ?? la cr??ation d'un compte abonn??.
     * @param paysId         L'identifiant du pays de r??sidence selectionn??.
     * @param bibliothequeId L'identifiant de la biblioth??que selectionn??e.
     * @return La page accueil avec la biblioth??que selectionn??e et un message e confirmation de la cr??ation du compte.
     */
    @PostMapping("/signin")
    public ModelAndView createAbonne(@ModelAttribute @Valid CreateAbonneBean userDetails,
                                     BindingResult bindingResult,
                                     @RequestParam("paysId") Long paysId,
                                     @RequestParam(value = "bibliothequeId", required = false) Long bibliothequeId) {
        ModelAndView theModel = new ModelAndView();
        utilsMethodService.setBibliothequeForTheVue(theModel, bibliothequeId);
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        String message;
        String fieldError;
        String path;
        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                logger.info(error.getObjectName() + " - " + error.getField() + " - " + error.getDefaultMessage());
                fieldError = error.getDefaultMessage();
                theModel.addObject("fieldError", fieldError);
            }
            logger.info(String.valueOf(bindingResult.getModel()));
            theModel.addObject("abonne", userDetails);
            message = "Les informations renseign??es ne sont pas conformes";
            theModel.addObject("message", message);
            path = "signin-page";
        } else {
            UtilisateurBean createdAbonne = modelMapper.map(userDetails, UtilisateurBean.class);
            createdAbonne.setEncryptedPassword(passwordEncoderHelper.encodePwd(userDetails.getPassword()));
            try {
                createdAbonne = userService.createAbonne(createdAbonne, paysId);
                message = "L'abonn?? " + createdAbonne.getUsername() + " / " + createdAbonne.getEmail() + " a bien ??t?? enregistr??.";
                theModel.addObject("message", message);
                path = "accueil";
            } catch (TechnicalException e) {
                theModel.addObject("errorMessage", e.getMessage());
                UtilisateurBean abooneToCreate = new UtilisateurBean();
                theModel.addObject("abonne", abooneToCreate);
                path = "signin-page";
            }

        }
        theModel.setViewName(path);
        return theModel;
    }

    /**
     * affiche la page profil de l'utilisateur pour consultation et modification de ses donn??es personnelles
     *
     * @param bibliothequeId L'identiant de la biblioth??que selectionn??es
     * @return la vue profil-utilisateur avec la biblioth??que selectionn??e, et le d??tail des donn??es de l'utilisateur connect??.
     */
    @GetMapping(value = "/showProfil")
    public ModelAndView showProfil(@RequestParam(value = "username", required = false) String username,
                                   @RequestParam(value = "bibliothequeId", required = false) Long bibliothequeId,
                                   @RequestParam(value = "modification", required = false) boolean modification) {
        ModelAndView theModel = new ModelAndView("profil-utilisateur");
        try {
            utilsMethodService.setBibliothequeForTheVue(theModel, bibliothequeId);
            //DONE T2 : retard pret => prolongation impossible + message
            utilsMethodService.setEmpruntListForProfilView(username, theModel, modification);
            //DONE T1 : afficher la liste des reservations en cours : titre du livre + date de retour pr??vu + position sur liste d'attente
            List<ReservationModelBean> reservationsToReturn = reservationService.getListeReservationsEnCours(username, bibliothequeId);
            theModel.addObject("reservationList", reservationsToReturn);
        } catch (TechnicalException e) {
            theModel.addObject("errorMessage", e.getMessage());
        }
        return theModel;
    }



    /**
     * Modification d'un profil utilisateur.
     *
     * @param userDetails    Les donn??es personnelles de l'utilisateur ?? la modification d'un compte abonn??.
     * @param paysId         L'identifiant du pays de r??sidence selectionn??.
     * @param bibliothequeId L'identifiant de la biblioth??que selectionn??e.
     * @return La page profil avec la biblioth??que selectionn??e et un message e confirmation de la modification du compte.
     */
    @PostMapping("/update")
    public ModelAndView updateAbonne(@ModelAttribute UtilisateurBean userDetails,
                                     @RequestParam(value = "paysId", required = false) Long paysId,
                                     @RequestParam(value = "username", required = false) String username,
                                     @RequestParam(value = "bibliothequeId", required = false) Long bibliothequeId,
                                     @RequestParam(value = "modification", required = false) boolean modification) {
        ModelAndView theModel = new ModelAndView("profil-utilisateur");
        modification = false;
        utilsMethodService.setBibliothequeForTheVue(theModel, bibliothequeId);
        userService.updateAbonne(userDetails);
        utilsMethodService.setEmpruntListForProfilView(username, theModel, modification);
        return theModel;
    }
}
