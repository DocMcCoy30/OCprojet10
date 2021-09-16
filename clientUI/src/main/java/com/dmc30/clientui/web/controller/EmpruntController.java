package com.dmc30.clientui.web.controller;

import com.dmc30.clientui.shared.UtilsMethodService;
import com.dmc30.clientui.shared.bean.bibliotheque.*;
import com.dmc30.clientui.service.contract.BibliothequeService;
import com.dmc30.clientui.service.contract.EmpruntService;
import com.dmc30.clientui.service.contract.OuvrageService;
import com.dmc30.clientui.service.contract.UserService;
import com.dmc30.clientui.shared.bean.utilisateur.UtilisateurBean;
import com.dmc30.clientui.web.exception.TechnicalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class EmpruntController {

    Logger logger = LoggerFactory.getLogger(EmpruntController.class);
    UtilsMethodService utilsMethodService;
    BibliothequeService bibliothequeService;
    UserService userService;
    OuvrageService ouvrageService;
    EmpruntService empruntService;

    @Autowired
    public EmpruntController(UtilsMethodService utilsMethodService,
                             BibliothequeService bibliothequeService,
                             UserService userService,
                             OuvrageService ouvrageService,
                             EmpruntService empruntService) {
        this.utilsMethodService = utilsMethodService;
        this.bibliothequeService = bibliothequeService;
        this.userService = userService;
        this.ouvrageService = ouvrageService;
        this.empruntService = empruntService;
    }

    //DONE : javadoc

    /**
     * Récupère les données at affiche la page d'enregistrement d'un emprunt (employé)
     * @param bibliothequeId l'identifiant de la bibliothèque
     * @param createEmpruntBean le formulaire de création d'un emprunt
     * @param numAbonne le numéro de l'abonné
     * @param idInterne l'identifiant interne de l'ouvrage
     * @return le model + vue
     */
    @GetMapping("/showEmpruntPage")
    public ModelAndView showEmpruntPage(@RequestParam("bibliothequeId") Long bibliothequeId,
                                        @ModelAttribute CreateEmpruntBean createEmpruntBean,
                                        @RequestParam(value = "numAbonne", required = false) String numAbonne,
                                        @RequestParam(value = "idInterne", required = false) String idInterne) {
        ModelAndView theModel = new ModelAndView("emprunt-page");
        utilsMethodService.setBibliothequeForTheVue(theModel, bibliothequeId);
        List<UtilisateurBean> abonnes = new ArrayList<>();
        List<OuvrageResponseModelBean> ouvrages = new ArrayList<>();
        theModel.addObject("abonnes", abonnes);
        theModel.addObject("ouvrages", ouvrages);
        String message = "";
        createEmpruntBean = new CreateEmpruntBean();
        List<OuvrageResponseModelBean> ouvragesByBibliotheque = new ArrayList<>();
        if (numAbonne == null) numAbonne = "";
        if (idInterne == null) idInterne = "";
        try {
            if ((!numAbonne.equals("")) && (!idInterne.equals(""))) {
                abonnes = userService.getUtilisateursByNumAbonne(numAbonne);
                if (abonnes.size() == 1) {
                    utilsMethodService.setAbonneForEmpruntBean(createEmpruntBean, abonnes.get(0));
                    theModel.addObject("createEmpruntBean", createEmpruntBean);
                } else {
                    theModel.addObject("abonnes", abonnes);
                }
                ouvrages = ouvrageService.getOuvragesByIdInterne(idInterne);
                if (ouvrages.size() == 1) {
                    utilsMethodService.setOuvrageForEmpruntBean(createEmpruntBean, ouvrages.get(0));
                    theModel.addObject("createEmpruntBean", createEmpruntBean);
                } else {
                    for (OuvrageResponseModelBean ouvrage : ouvrages) {
                        if (ouvrage.getBibliothequeId().equals(bibliothequeId)) {
                            ouvragesByBibliotheque.add(ouvrage);
                        }
                    }
                    theModel.addObject("ouvrages", ouvragesByBibliotheque);
                }
            } else if (!numAbonne.equals("")) {
                UtilisateurBean abonneSelectionne = userService.getUtilisateurByNumAbonne(numAbonne);
                utilsMethodService.setAbonneForEmpruntBean(createEmpruntBean, abonneSelectionne);
                theModel.addObject("createEmpruntBean", createEmpruntBean);
            } else if (!idInterne.equals("")) {
                OuvrageResponseModelBean ouvrageSelectionne = ouvrageService.getOuvrageByIdInterne(idInterne);
                utilsMethodService.setOuvrageForEmpruntBean(createEmpruntBean, ouvrageSelectionne);
                theModel.addObject("createEmpruntBean", createEmpruntBean);
            }
        } catch (TechnicalException e) {
            String errorMessage = e.getMessage();
            theModel.addObject("errorMessage", errorMessage);
        }
        return theModel;
    }

    /**
     * Outil de création du formulaire d'enregistrement d'un emprunt
     * @param bibliothequeId l'identifiant de la bibliothèque
     * @param createEmpruntBean le formulaire de création d'un emprunt
     * @param numAbonne le numero de l'abonné
     * @param idInterne l'identifiant interne de l'ouvrage
     * @return le model + vue
     */
    @PostMapping("/createEmpruntSearchForm")
    public ModelAndView populateTheCreateEmpruntForm(@RequestParam("bibliothequeId") Long bibliothequeId,
                                                     @ModelAttribute CreateEmpruntBean createEmpruntBean,
                                                     @RequestParam(value = "numAbonne", required = false) String numAbonne,
                                                     @RequestParam(value = "idInterne", required = false) String idInterne) {
        ModelAndView theModel = new ModelAndView("emprunt-page");
        utilsMethodService.setBibliothequeForTheVue(theModel, bibliothequeId);
        List<UtilisateurBean> abonnes = new ArrayList<>();
        List<OuvrageResponseModelBean> ouvrages = new ArrayList<>();
        theModel.addObject("abonnes", abonnes);
        theModel.addObject("ouvrages", ouvrages);
        String message = "";
        List<OuvrageResponseModelBean> ouvragesByBibliotheque = new ArrayList<>();
        try {
        if ((numAbonne.equals("")) && (idInterne.equals(""))) {
            message = "Aucuns critères renseignés pour la recherche.";
        }
        if ((numAbonne != null) && (!numAbonne.equals(""))) {
            abonnes = userService.getUtilisateursByNumAbonne(createEmpruntBean.getNumAbonne());
            if (abonnes.size() == 1) {
                utilsMethodService.setAbonneForEmpruntBean(createEmpruntBean, abonnes.get(0));
            } else {
                message = "Il y a plusieurs abonnés correspondants à votre choix";
                theModel.addObject("abonnes", abonnes);
            }

        }
        if ((idInterne != null) && (!idInterne.equals(""))) {
            ouvrages = ouvrageService.getOuvragesByIdInterne(createEmpruntBean.getIdInterne());
            if (ouvrages.size() == 1) {
                utilsMethodService.setOuvrageForEmpruntBean(createEmpruntBean, ouvrages.get(0));
            } else {
                for (OuvrageResponseModelBean ouvrage : ouvrages) {
                    if (ouvrage.getBibliothequeId().equals(bibliothequeId)) {
                        ouvragesByBibliotheque.add(ouvrage);
                    }
                }
                message = "Il y a plusieurs ouvrages correspondants à votre choix";
                theModel.addObject("ouvrages", ouvragesByBibliotheque);
            }
            theModel.addObject("createEmpruntBean", createEmpruntBean);
        }
        } catch (TechnicalException e) {
            String errorMessage = e.getMessage();
            theModel.addObject("errorMessage", errorMessage);
        }
        theModel.addObject("message", message);
        return theModel;
    }

    /**
     * Enregitre un emprunt dans la base de données
     * @param bibliothequeId l'identifiant de la bibliotheque
     * @param createEmpruntBean le formulaire de création d'un emprunt
     * @return le model + vue
     */
    @PostMapping("/createEmprunt")
    public ModelAndView createEmprunt(@RequestParam("bibliothequeId") Long bibliothequeId,
                                      @ModelAttribute CreateEmpruntBean createEmpruntBean) {
        ModelAndView theModel = new ModelAndView("emprunt-page");
        utilsMethodService.setBibliothequeForTheVue(theModel, bibliothequeId);
        String message = "";
        List<UtilisateurBean> abonnes = new ArrayList<>();
        List<OuvrageResponseModelBean> ouvrages = new ArrayList<>();
        theModel.addObject("abonnes", abonnes);
        theModel.addObject("ouvrages", ouvrages);
        if (createEmpruntBean.getAbonneId()==null) {
            message = "Veuillez selectionner un abonné !";
        }
        else if (createEmpruntBean.getOuvrageId()==null) {
            message = "Veuillez selectionner un ouvrage !";
        }
        else {
            try {
                EmpruntBean empruntBean = empruntService.createEmprunt(createEmpruntBean);
                message = "L'emprunt du livre a bien été enregistré.";
            } catch (TechnicalException e) {
                String errorMessage = e.getMessage();
                theModel.addObject("errorMessage", errorMessage);
            }
        }
        createEmpruntBean = new CreateEmpruntBean();
        theModel.addObject("createEmpruntBean", createEmpruntBean);
        theModel.addObject("messageCreateEmprunt", message);
        return theModel;
    }

    /**
     * Recherche et retourne la liste des emprunts en cours pour une bibliotheque
     * @param bibliothequeId l'identifiant de le bibliotheque
     * @return le model + vue
     */
    @GetMapping("/searchEmpruntsEnCours")
    public ModelAndView getEmpruntEnCours(@RequestParam("bibliothequeId") Long bibliothequeId) {
        ModelAndView theModel = new ModelAndView("emprunt-en-cours-page");
        utilsMethodService.setBibliothequeForTheVue(theModel, bibliothequeId);
        List<EmpruntModelBean> empruntModelBeans = new ArrayList<>();
        String message = "";
        utilsMethodService.setEmpruntsEnCours(theModel, empruntModelBeans, bibliothequeId);
        return theModel;
    }

    /**
     * Permet d'enregistrer le retour d'un emprunt
     * @param bibliothequeId l'identifiant de la bibliotheque
     * @param ouvrageId l'identifiant de l'ouvrage
     * @param empruntId l'identifiant de l'ouvrage emprunté
     * @return le model + vue
     */
    @GetMapping("/retournerEmprunt")
    public ModelAndView retournerEmprunt(@RequestParam("bibliothequeId") Long bibliothequeId,
                                         @RequestParam("ouvrageId") String ouvrageId,
                                         @RequestParam("empruntId") Long empruntId) {
        ModelAndView theModel = new ModelAndView("emprunt-en-cours-page");
        utilsMethodService.setBibliothequeForTheVue(theModel, bibliothequeId);
        String messageRetour;
        List<EmpruntModelBean> empruntModelBeans = new ArrayList<>();
        try {
            empruntService.retournerEmprunt(empruntId, ouvrageId);
            messageRetour = "Le retour a bien été enregistré";
            theModel.addObject("messageRetour", messageRetour);
            utilsMethodService.setEmpruntsEnCours(theModel, empruntModelBeans, bibliothequeId);
        } catch (TechnicalException e) {
            String errorMessage = e.getMessage();
            theModel.addObject("errorMessage", errorMessage);
        }
        return theModel;
    }

    /**
     * Permet d'enregistrer la prolongation d'un emprunt
     * @param bibliothequeId l'identifiant de la bibliotheque
     * @param empruntId l'identifiant de l'emprunt
     * @param username le username de l'abonné
     * @return le model + vue
     */
    @GetMapping("/prolongerEmprunt")
    public ModelAndView prolongerEmprunt(@RequestParam("bibliothequeId") Long bibliothequeId,
                                         @RequestParam("empruntId") Long empruntId,
                                         @RequestParam(value = "username", required = false) String username) {
        ModelAndView theModel = new ModelAndView("/profil-utilisateur");
        utilsMethodService.setBibliothequeForTheVue(theModel, bibliothequeId);
        String messageProlongation = "";
        boolean modification = false;
        try {
            empruntService.prolongerEmprunt(empruntId);
            messageProlongation = "Votre demande de prolongation a bien été enregistrée.";
            utilsMethodService.setEmpruntListForProfilView(username, theModel, modification);
            theModel.addObject("messageProlongation", messageProlongation);
        } catch (TechnicalException e) {
            String errorMessage = e.getMessage();
            theModel.addObject("errorMessage", errorMessage);
        }
        return theModel;
    }
}



