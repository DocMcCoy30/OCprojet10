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

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
     * Enregistre une r??servation dans la BdD.
     *
     * @param bibliothequeId l'identifiant de la biblioth??que.
     * @param livreId        l'identifiant du livre.
     * @param username       le username de l'utilisateur.
     * @return la reservation cr????e
     * @throws TechnicalException
     */
    @Override
    public ReservationBean createReservation(Long bibliothequeId, Long livreId, String username) throws TechnicalException {
        ReservationBean savedReservation;
        try {
            UtilisateurBean utilisateurBean = userService.getUtilisateurByUsername(username);
            ReservationBean newReservation = new ReservationBean();
            ZoneId zoneId = ZoneId.of( "Europe/Paris" );
            ZonedDateTime zdt = ZonedDateTime.ofInstant( Instant.now() , zoneId );
            logger.info(zdt);
            newReservation.setDateReservationTz(zdt);
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
    //DONE T3: tests unitaires

    /**
     * V??rifie qu'une r??servation peut ??tre effectu??e
     * (RG : pas d'emprunt ni de r??servation en cours, liste d'attente incompl??te)
     *
     * @param livreId        l'identifiant du livre ?? r??server.
     * @param username       le username de l'utilisateur.
     * @param bibliothequeId l'identifiant de la bibliotheque
     * @return true si la r??servation est possible, false si une des RG n'est pas respect??e.
     * @throws TechnicalException
     */
    public boolean globalReservationPossibleCheck(Long livreId, String username, Long bibliothequeId) throws TechnicalException {
        boolean reservationPossible = true;
        if (!reservationPossibleCheck1(livreId, username)) {
            reservationPossible = false;
            throw new TechnicalException("R??servation impossible : vous avez un emprunt en cours pour ce livre.");
        } else if (!reservationPossibleCheck2(livreId, username)) {
            reservationPossible = false;
            throw new TechnicalException("Une r??servation est d??j?? enregistr??e pour ce livre.");
        } else if (!reservationPossibleCheck3(livreId, bibliothequeId)) {
            reservationPossible = false;
            throw new TechnicalException("R??servation impossible : la liste d'attente est pleine.");
        }

        return reservationPossible;
    }

    /**
     * R??cup??re le nombre de r??servation en cours pour un livre
     *
     * @param livreId        l'identifiant du livre
     * @param bibliothequeId l'identifiant de la bibliotheque
     * @return le nombre de r??servation en cours
     */
    @Override
    public Integer getNombreDeReservation(Long livreId, Long bibliothequeId) {
        Integer nbReservation = reservationServiceProxy.getNombreDeReservation(livreId, bibliothequeId);
        return nbReservation;
    }

    //DONE T1: liste des r??servations en cours pour la page profil utilisateur

    /**
     * Renvoie la liste des r??servations en cours : titre du livre, date de retour pr??vue, position dans le liste d'attente
     *
     * @param username       le username de l'abonn??
     * @param bibliothequeId l'identifiant de la bibliotheque
     * @return la liste
     * @throws TechnicalException
     */
    @Override
    public List<ReservationModelBean> getListeReservationsEnCours(String username, Long bibliothequeId) throws TechnicalException {
        ObjectMapper mapper = new ObjectMapper();
        Long livreId;
        List<Long> reservationToReturnIds = new ArrayList<>();
        List<ReservationModelBean> reservationsToReturn = new ArrayList<>();
        // recup??rer les reservations de l'utilisateur.
        try {
            Long userId = userService.getUtilisateurByUsername(username).getId();
            List<ReservationBean> reservationsParUser = reservationServiceProxy.getReservationsByUserId(userId);
            // pour chaque r??sa de l'u r??cup??rer le livre id, la date, le titre
            for (ReservationBean reservationParUser : reservationsParUser) {
                ReservationModelBean reservationModelBean = new ReservationModelBean();
                livreId = reservationParUser.getLivreId();
                ZonedDateTime dateToFormat = (reservationParUser.getDateReservationTz()).withZoneSameInstant(ZoneId.of("Europe/Paris"));
                reservationModelBean.setId(reservationParUser.getId());
                String dateReservation = (DateTimeFormatter.ofPattern("EEEE dd LLLL yyyy").format(dateToFormat));
                reservationModelBean.setDateReservation(dateReservation);
                reservationModelBean.setExpiree(reservationParUser.isExpiree());
                ResponseEntity<?> response = livreService.getLivreById(livreId);
                if (response.getStatusCodeValue() == 202) {
                    LivreResponseModelBean livreResponseModelBean = (LivreResponseModelBean) response.getBody();
                    reservationModelBean.setTitreDuLivre(livreResponseModelBean.getTitre());
                }
                // r??cup??rer la liste des biblioth??ques
                List<BibliothequeBean> bibliotheques = bibliothequeService.getBibliotheques();
                // r??cup??rer la liste des resa par livre et biblio ordonn??e par date
                for (BibliothequeBean bibliotheque : bibliotheques) {
                    List<ReservationBean> reservationsOrdonnees = reservationServiceProxy.getReservationByLivreIdAndAndBibliothequeIdOrderByDateReservation(livreId, bibliotheque.getId());
                    for (ReservationBean reservationParLivre : reservationsOrdonnees) {
                        // r??cup??rer le num??ro dans la file d'attente
                        if (reservationParLivre.getUtilisateurId().equals(userId)) {
                            int index = reservationsOrdonnees.indexOf(reservationParLivre);
                            reservationModelBean.setNumeroAttente(index + 1);
                            reservationModelBean.setBibliotheque(bibliotheque.getNom());
                            //r??cup??rer la date de retour pr??vu
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


//-------------------------M??thodes de classe----------------------------------------

    //DONE T3: Tests unitaires Check1, Check2, Check3
    //DONE T1: Check1 : pas d'emprunt en cours pour ce livre pour cet utilisateur

    /**
     * V??rifie que la RG "pas d'emprunt en cours pour ce livre et cet utilisateur" est respect??e.
     *
     * @param livreId  l'identifiant du livre.
     * @param username le username de l'utilisateur.
     * @return true si la r??servation est possible, false si une des RG n'est pas respect??e.
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

    //DONE T1: Check2 : pas de r??servation d??j?? en cours pour ce livre pour cet utilisateur

    /**
     * V??rifie que la RG "pas de r??servation en cours pour ce livre et cet utilisateur" est respect??e.
     *
     * @param livreId  l'identifiant du livre.
     * @param username le username de l'utilisateur.
     * @return true si la r??servation est possible, false si une des RG n'est pas respect??e.
     */

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

    //DONE T1: Check3 :  la liste d'attente n'est pas compl??te
    /**
     * V??rifie que la RG "la liste d'attente n'est pas compl??te" est respect??e.
     *
     * @param livreId        l'identifiant du livre.
     * @param bibliothequeId l'identifiant de la bibliotheque
     * @return true si la r??servation est possible, false si une des RG n'est pas respect??e.
     */
    public boolean reservationPossibleCheck3(Long livreId, Long bibliothequeId) {
        boolean reservation = true;
        // r??cup??rer le nombre de r??servation (nbReservation) pour ce livre et cette bibliotheque
        Integer nbReservation = reservationServiceProxy.getNombreDeReservation(livreId, bibliothequeId);
        logger.info("nbReservation =" + nbReservation);
        // r??cup??rer le nombre d'ouvrage (nbOuvrage) correspondant ?? ce livre dans cette bibliotheque
        Integer nbOuvrage = ouvrageService.getNombreDOuvrage(livreId, bibliothequeId);
        logger.info("nbOuvrage = " + nbOuvrage);
        // v??rifier que nbReservation est < ?? nbOuvrage
        if (nbReservation >= nbOuvrage * 2) {
            reservation = false;
        }
        return reservation;
    }

}
