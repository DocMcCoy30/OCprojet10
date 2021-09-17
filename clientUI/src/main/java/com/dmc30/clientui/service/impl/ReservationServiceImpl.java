package com.dmc30.clientui.service.impl;

import com.dmc30.clientui.proxy.LivreServiceProxy;
import com.dmc30.clientui.proxy.ReservationServiceProxy;
import com.dmc30.clientui.service.contract.*;
import com.dmc30.clientui.shared.bean.bibliotheque.BibliothequeBean;
import com.dmc30.clientui.shared.bean.bibliotheque.EmpruntBean;
import com.dmc30.clientui.shared.bean.livre.LivreResponseModelBean;
import com.dmc30.clientui.shared.bean.reservation.ReservationBean;
import com.dmc30.clientui.shared.bean.reservation.ReservationModelBean;
import com.dmc30.clientui.shared.bean.utilisateur.UtilisateurBean;
import com.dmc30.clientui.web.exception.ErrorMessage;
import com.dmc30.clientui.web.exception.TechnicalException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class ReservationServiceImpl implements ReservationService {

    Logger logger = LogManager.getLogger(ReservationServiceImpl.class);

    ReservationServiceProxy reservationServiceProxy;
    LivreServiceProxy livreServiceProxy;
    LivreService livreService;
    EmpruntService empruntService;
    OuvrageService ouvrageService;
    UserService userService;
    BibliothequeService bibliothequeService;

    @Autowired
    public ReservationServiceImpl(ReservationServiceProxy reservationServiceProxy,
                                  LivreServiceProxy livreServiceProxy,
                                  LivreService livreService,
                                  EmpruntService empruntService,
                                  OuvrageService ouvrageService,
                                  UserService userService,
                                  BibliothequeService bibliothequeService) {
        this.reservationServiceProxy = reservationServiceProxy;
        this.livreServiceProxy = livreServiceProxy;
        this.livreService = livreService;
        this.empruntService = empruntService;
        this.ouvrageService = ouvrageService;
        this.userService = userService;
        this.bibliothequeService = bibliothequeService;
    }

    /**
     * Enregistre une réservation dans la BdD.
     *
     * @param bibliothequeId l'identifiant de la bibliothèque.
     * @param livreId        l'identifiant du livre.
     * @param username       le username de l'utilisateur.
     * @return la reservation créée
     * @throws TechnicalException
     */
    @Override
    public ReservationBean createReservation(Long bibliothequeId, Long livreId, String username) throws TechnicalException {
        ReservationBean savedReservation;
        try {
            UtilisateurBean utilisateurBean = userService.getUtilisateurByUsername(username);
            ReservationBean newReservation = new ReservationBean();
            newReservation.setDateReservation(new Date());
            newReservation.setExpiree(false);
            newReservation.setUtilisateurId(utilisateurBean.getId());
            newReservation.setLivreId(livreId);
            newReservation.setBibliothequeId(bibliothequeId);
            savedReservation = reservationServiceProxy.createReservation(newReservation);
        } catch (FeignException e) {
            throw new TechnicalException(ErrorMessage.TECHNICAL_ERROR.getErrorMessage());
        }
        return savedReservation;
    }


    //DONE T1: checkReservationPossible

    /**
     * Vérifie qu'une réservation peut être effectuée
     * (RG : pas d'emprunt ni de réservation en cours, liste d'attente incomplète)
     *
     * @param livreId        l'identifiant du livre à réserver.
     * @param username       le username de l'utilisateur.
     * @param bibliothequeId l'identifiant de la bibliotheque
     * @return true si la réservation est possible, false si une des RG n'est pas respectée.
     * @throws TechnicalException
     */
    public boolean globalReservationPossibleCheck(Long livreId, String username, Long bibliothequeId) throws TechnicalException {
        boolean reservationPossible = true;
        if (!reservationPossibleCheck1(livreId, username)) {
            reservationPossible = false;
            throw new TechnicalException("Réservation impossible : vous avez un emprunt en cours pour ce livre.");
        } else if (!reservationPossibleCheck2(livreId, username)) {
            reservationPossible = false;
            throw new TechnicalException("Une réservation est déjà enregistrée pour ce livre.");
        } else if (!reservationPossibleCheck3(livreId, bibliothequeId)) {
            reservationPossible = false;
            throw new TechnicalException("Réservation impossible : la liste d'attente est pleine.");
        }

        return reservationPossible;
    }

    /**
     * Récupère le nombre de réservation en cours pour un livre
     *
     * @param livreId        l'identifiant du livre
     * @param bibliothequeId l'identifiant de la bibliotheque
     * @return le nombre de réservation en cours
     */
    @Override
    public Integer getNombreDeReservation(Long livreId, Long bibliothequeId) {
        Integer nbReservation = reservationServiceProxy.getNombreDeReservation(livreId, bibliothequeId);
        return nbReservation;
    }

    /**
     * Récupère la liste des réservations d'un utilisateur
     *
     * @param userId l'identifiant de l'utilisateur
     * @return la liste
     */
    @Override
    public List<ReservationBean> getReservationByUserId(Long userId) {
        List<ReservationBean> reservations = reservationServiceProxy.getReservationsByUserId(userId);
        return reservations;
    }

    /**
     * Récupère la liste des réservations par livre et par bibliothèque ordonnée par date
     *
     * @param livreId        l'identifiant du livre
     * @param bibliothequeId l'identifiant de la bibliothèque
     * @return la liste
     */
    @Override
    public List<ReservationBean> getReservationByLivreIdAndAndBibliothequeIdOrderByDateReservation(Long livreId, Long bibliothequeId) {
        List<ReservationBean> reservations = reservationServiceProxy.getReservationByLivreIdAndAndBibliothequeIdOrderByDateReservation(livreId, bibliothequeId);
        return reservations;
    }

    //DONE T1: liste des réservations en cours pour la page profil utilisateur

    /**
     * Renvoie la liste des réservations en cours : titre du livre, date de retour prévue, position dans le liste d'attente
     *
     * @param username       le username de l'abonné
     * @param bibliothequeId l'identifiant de la bibliotheque
     * @return la liste
     * @throws TechnicalException
     */
    @Override
    public List<ReservationModelBean> getListeReservationsEnCours(String username, Long bibliothequeId) throws TechnicalException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE dd MMMMM yyyy");
        Long livreId;
        List<Long> reservationToReturnIds = new ArrayList<>();
        List<ReservationModelBean> reservationsToReturn = new ArrayList<>();
        // recupérer les reservations de l'utilisateur.
        try {
            Long userId = userService.getUtilisateurByUsername(username).getId();
            List<ReservationBean> reservationsParUser = reservationServiceProxy.getReservationsByUserId(userId);
            // pour chaque résa de l'u récupérer le livre id, la date, le titre
            for (ReservationBean reservationParUser : reservationsParUser) {
                ReservationModelBean reservationModelBean = new ReservationModelBean();
                livreId = reservationParUser.getLivreId();
                reservationModelBean.setId(reservationParUser.getId());
                String dateReservation = dateFormat.format(reservationParUser.getDateReservation());
                reservationModelBean.setDateReservation(dateReservation);
                reservationModelBean.setExpiree(reservationParUser.isExpiree());
                ResponseEntity<?> response = livreService.getLivreById(livreId);
                if (response.getStatusCodeValue() == 202) {
                    LivreResponseModelBean livreResponseModelBean = (LivreResponseModelBean) response.getBody();
                    reservationModelBean.setTitreDuLivre(livreResponseModelBean.getTitre());
                }
                // récupérer la liste des bibliothèques
                List<BibliothequeBean> bibliotheques = bibliothequeService.getBibliotheques();
                // récupérer la liste des resa par livre et biblio ordonnée par date
                for (BibliothequeBean bibliotheque : bibliotheques) {
                    List<ReservationBean> reservationsOrdonnees = reservationServiceProxy.getReservationByLivreIdAndAndBibliothequeIdOrderByDateReservation(livreId, bibliotheque.getId());
                    for (ReservationBean reservationParLivre : reservationsOrdonnees) {
                        // récupérer le numéro dans la file d'attente
                        if (reservationParLivre.getUtilisateurId().equals(userId)) {
                            int index = reservationsOrdonnees.indexOf(reservationParLivre);
                            reservationModelBean.setNumeroAttente(index + 1);
                            reservationModelBean.setBibliotheque(bibliotheque.getNom());
                            //récupérer la date de retour prévu
                            reservationModelBean.setDateRetourPrevu(empruntService.getDateDeRetourPrevue(livreId, bibliothequeId));
                            //construire la liste des reservationListeModel
                            if (!reservationToReturnIds.contains(reservationModelBean.getId())) {
                                reservationsToReturn.add(reservationModelBean);
                                reservationToReturnIds.add(reservationModelBean.getId());
                            }
                        }
                    }
                }
            }
        } catch (FeignException e) {
            throw new TechnicalException(ErrorMessage.TECHNICAL_ERROR.getErrorMessage());
        }
        return reservationsToReturn;
    }


    @Override
    public String deleteReservation(Long reservationId) throws TechnicalException {
        String reservationMessage;
        try {
            reservationMessage = reservationServiceProxy.deleteReservation(reservationId);
        } catch (FeignException e) {
            throw new TechnicalException(ErrorMessage.TECHNICAL_ERROR.getErrorMessage());
        }
        return reservationMessage;
    }


//-------------------------Méthodes de classe----------------------------------------

//DONE T1: Check1 : pas d'emprunt en cours pour ce livre pour cet utilisateur

    /**
     * Vérifie que la RG "pas d'emprunt en cours pour ce livre et cet utilisateur" est respectée.
     *
     * @param livreId  l'identifiant du livre.
     * @param username le username de l'utilisateur.
     * @return true si la réservation est possible, false si une des RG n'est pas respectée.
     * @throws TechnicalException
     */
    public boolean reservationPossibleCheck1(Long livreId, String username) throws TechnicalException {
        boolean reservation = true;
        Long userId = (userService.getUtilisateurByUsername(username)).getId();
        List<EmpruntBean> emprunts = empruntService.getEmpruntByUtilisateurId(userId);
        if (emprunts.isEmpty()) {
            reservation = true;
        } else {
            for (EmpruntBean empruntBean : emprunts) {
                Long livreEmprunteId = livreServiceProxy.getLivreIdByOuvrageId(empruntBean.getOuvrageId());
                if ((Objects.equals(livreId, livreEmprunteId)) && (!empruntBean.isRestitution())) {
                    reservation = false;
                    break;
                }
            }
        }
        return reservation;
    }

    /**
     * Vérifie que la RG "pas de réservation en cours pour ce livre et cet utilisateur" est respectée.
     *
     * @param livreId  l'identifiant du livre.
     * @param username le username de l'utilisateur.
     * @return true si la réservation est possible, false si une des RG n'est pas respectée.
     */
//DONE T1: Check2 : pas de réservation déjà en cours pour ce livre pour cet utilisateur
    public boolean reservationPossibleCheck2(Long livreId, String username) {
        boolean reservation = true;
        Long userId = (userService.getUtilisateurByUsername(username)).getId();
        List<ReservationBean> reservationBeans = reservationServiceProxy.getReservationsByUserId(userId);
        for (ReservationBean reservationBean : reservationBeans) {
            if ((reservationBean.getLivreId().equals(livreId)) && (!reservationBean.isExpiree())) {
                reservation = false;
                break;
            }
        }
        return reservation;
    }

    /**
     * Vérifie que la RG "la liste d'attente n'est pas complète" est respectée.
     *
     * @param livreId        l'identifiant du livre.
     * @param bibliothequeId l'identifiant de la bibliotheque
     * @return true si la réservation est possible, false si une des RG n'est pas respectée.
     */
//DONE T1: Check3 :  la liste d'attente n'est pas complète
    public boolean reservationPossibleCheck3(Long livreId, Long bibliothequeId) {
        boolean reservation = true;
        // récupérer le nombre de réservation (nbReservation) pour ce livre et cette bibliotheque
        Integer nbReservation = reservationServiceProxy.getNombreDeReservation(livreId, bibliothequeId);
        logger.info("nbReservation =" + nbReservation);
        // récupérer le nombre d'ouvrage X2 (nbOuvrage) correspondant à ce livre dans cette bibliotheque
        Integer nbOuvrage = ouvrageService.getNombreDOuvrage(livreId, bibliothequeId);
        logger.info("nbOuvrage = " + nbOuvrage);
        // vérifier que nbReservation est < à nbOuvrage
        if (nbReservation >= nbOuvrage * 2) {
            reservation = false;
        }
        return reservation;
    }

}
