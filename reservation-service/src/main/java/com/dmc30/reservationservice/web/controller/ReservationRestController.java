package com.dmc30.reservationservice.web.controller;

import com.dmc30.reservationservice.model.dto.ReservationDto;
import com.dmc30.reservationservice.service.contract.ReservationService;
import com.dmc30.reservationservice.web.exception.TechnicalException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//DONE T1 : Gère les fonctionnalités pour les réservations
@RestController
@RequestMapping("/reservations")
public class ReservationRestController {

    Logger logger = LogManager.getLogger(ReservationRestController.class);

    ReservationService reservationService;

    @Autowired
    public ReservationRestController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    /**
     * Récupère la liste de toutes les réservations de la BdD
     *
     * @return la liste
     */
    @GetMapping("/")
    public List<ReservationDto> getAllReservations() {
        return reservationService.getAllReservations();
    }

    /**
     * Récupère une réservation par son identifiant
     *
     * @param reservationId l'identifiant de la réservation
     * @return la réservation concernée
     */
    @GetMapping(path = "/{reservationId}")
    public ReservationDto getReservationById(@PathVariable(name = "reservationId") Long reservationId) {
        return reservationService.getReservationById(reservationId);
    }

    /**
     * Enregistre une nouvelle réservation dans la base de données
     *
     * @param reservationDto la réservation à créer
     * @return le reservation enregistrée
     */
    @PostMapping("/")
    public ReservationDto createReservation(@RequestBody ReservationDto reservationDto) {
        return reservationService.createReservation(reservationDto);
    }

    /**
     * Récupère la liste des réservations pour un utilisateur
     *
     * @param userId l'identifiant de l'utilisateur
     * @return la liste
     */
    @GetMapping("/user/{userId}")
    public List<ReservationDto> getReservationsByUserId(@PathVariable(name = "userId") Long userId) {
        return reservationService.getReservationsByUserId(userId);
    }

    /**
     * Récupère le nombre de réservation pour un livre et une bibliothèque
     *
     * @param livreId        l'identifiant du livre
     * @param bibliothequeId l'identifiant de la bibliothèque
     * @return un nombre de réservation en cours
     */
    @GetMapping("/nbResa/{livreId}&{bibliothequeId}")
    public Integer getNombreDeReservation(@PathVariable(name = "livreId") Long livreId,
                                          @PathVariable(name = "bibliothequeId") Long bibliothequeId) {
        Integer nbReservation = reservationService.getNombreDeReservation(livreId, bibliothequeId);
        return nbReservation;
    }

    /**
     * Récupère une liste de réservation pour un livre dans une bibliothèque ordonnée par date
     *
     * @param livreId        l'identifiant du livre
     * @param bibliothequeId l'identifiant de la bibliothèque
     * @return la liste ordonnée
     */
    @GetMapping("/listeAttente/{livreId}&{bibliothequeId}")
    public List<ReservationDto> getReservationByLivreIdAndAndBibliothequeIdOrderByDateReservation(@PathVariable(name = "livreId") Long livreId,
                                                                                                  @PathVariable(name = "bibliothequeId") Long bibliothequeId) {
        List<ReservationDto> reservationDtos = reservationService.getReservationByLivreIdAndAndBibliothequeIdOrderByDateReservation(livreId, bibliothequeId);
        return reservationDtos;
    }

    /**
     * Supprime une réservation dans la BdD
     *
     * @param reservationId l'identifiant de la réservation à supprimer
     */
    @DeleteMapping("/{reservationId}")
    public String deleteReservation(@PathVariable(name = "reservationId") Long reservationId) throws TechnicalException {
        ReservationDto reservationDto = reservationService.getReservationById(reservationId);
        if (reservationDto == null) {
            throw new TechnicalException("La reservation n'existe pas");
        }else {
            reservationService.deleteReservation(reservationId);
        }
        return "Le reservation a bien été supprimée.";
    }

    @GetMapping("/expiree/{userId}")
    public List<ReservationDto> getReservationsExpireesByUserId(@PathVariable(name = "userId") Long userId) {

        return null;
    }
}
