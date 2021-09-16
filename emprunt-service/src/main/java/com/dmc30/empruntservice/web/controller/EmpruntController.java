package com.dmc30.empruntservice.web.controller;

import com.dmc30.empruntservice.service.dto.EmpruntDto;
import com.dmc30.empruntservice.service.contract.EmpruntService;
import com.dmc30.empruntservice.service.dto.CreateEmpruntDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/emprunts")
public class EmpruntController {

    EmpruntService empruntService;

    @Autowired
    public EmpruntController(EmpruntService empruntService) {
        this.empruntService = empruntService;
    }

    //DONE : javadoc

    /**
     * Enregistre un emprunt dans la BdD
     * @param createEmpruntDto DTO formulaire de création d'un emprunt
     * @return l'enmprunt enregistré
     */
    @PostMapping("/create")
    public EmpruntDto createEmprunt(@RequestBody CreateEmpruntDto createEmpruntDto) {
        EmpruntDto empruntDto = empruntService.createEmprunt(createEmpruntDto);
        return empruntDto;
    }

    /**
     * Récupère les emprunts en cours pour une bibliotheque
     * @param bibliothequeId l'identifiant de la bibliotheque
     * @return la liste des emprunts en cours
     */
    @GetMapping("/encours")
    public List<EmpruntDto> findEmpruntEnCours(@RequestParam Long bibliothequeId) {
        List<EmpruntDto> empruntDtoList = empruntService.findEmpruntEnCours(bibliothequeId);
        return empruntDtoList;
    }

    /**
     * Récupère la liste des emprunts pour un utilisateur
     * @param utilisateurId l'identifiant d'un utilisateur
     * @return la liste
     */
    @GetMapping("/utilisateur")
    public List<EmpruntDto> findEmpruntByUtilisateurId (@RequestParam Long utilisateurId) {
        List<EmpruntDto> empruntDtoList = empruntService.findEmpruntByUtilisateurId(utilisateurId);
        return empruntDtoList;
    }

    /**
     * Enregistre le retour d'un emprunt par un abonné
     * @param empruntId l'identfiant de l'emprunt
     * @param ouvrageId l'identifiant de l'ouvrage
     */
    @GetMapping("/retour")
    public void retournerEmprunt(@RequestParam Long empruntId, @RequestParam String ouvrageId) {
        empruntService.retournerEmprunt(empruntId, ouvrageId);
    }

    /**
     * Prolonge la durée d'un emprunt
     * @param empruntId l'identifiant de l'emprunt concerné
     */
    @GetMapping("/prolongation")
    public void prolongerEmprunt(@RequestParam Long empruntId) {
        empruntService.prolongerEmprunt(empruntId);
    }

    /**
     * Retourne la liste des emprunts expirés
     * @return la liste
     */
    @GetMapping("/expired")
    List<EmpruntDto> findExpiredemprunts() {
        return empruntService.findExpiredemprunts();
    }

    /**
     * Retourne la liste des emprunts expirés pour un utilisateur
     * @param utilisateurId l'identifiant de l'utilisateur
     * @return la liste
     */
    @GetMapping("/expiredByUtilisateur")
    List<EmpruntDto> findExpiredempruntsByUtilisateurId(@RequestParam Long utilisateurId) {
        List<EmpruntDto> empruntDtos = empruntService.findExpiredempruntsByUtilisateurId(utilisateurId);
         return empruntDtos;
    }

    /**
     * Retourne la liste des utilisateurs en retard pour le retour d'un emprunt
     * @return la liste
     */
    @GetMapping("/expiredUsers")
    List<Long> findUtilisateurEnRetard() {
        return empruntService.findUtilisateurEnRetard();
    }

    /**
     * Récupère un emprunt en cours par l'identifiant de l'ouvrage
      * @param ouvrageId l'identifiant de l'ouvrage emprunté
     * @return l'ouvrage en cours d'emprunt
     */
    @GetMapping("/ouvrage/{ouvrageId}")
    public EmpruntDto getEmpruntEnCoursByOuvrageId(@PathVariable(name = "ouvrageId") Long ouvrageId) {
        EmpruntDto emprunt = empruntService.getEmpruntEnCoursByOuvrageId(ouvrageId);
        return emprunt;
    }

    //DONE : Méthodes pour le service d'envoi de mail pour les reservations

    // ---------------------Mail Service Methode --------------------------

    @GetMapping("/restitues")
    public List<EmpruntDto> empruntsRestitues () {
        List<EmpruntDto> empruntsRestitues = empruntService.getEmpruntsRestitues();
        return empruntsRestitues;
    }
}
