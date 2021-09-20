package com.dmc30.reservationservice.service.impl;

import com.dmc30.reservationservice.data.entity.Reservation;
import com.dmc30.reservationservice.data.repository.ReservationRepository;
import com.dmc30.reservationservice.model.dto.ReservationDto;
import com.dmc30.reservationservice.model.mappers.ReservationMapper;
import com.dmc30.reservationservice.service.contract.ReservationService;
import com.dmc30.reservationservice.web.exception.ErrorMessage;
import com.dmc30.reservationservice.web.exception.TechnicalException;
import com.fasterxml.jackson.core.JsonParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.sql.SQLDataException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReservationServiceImpl implements ReservationService {

    Logger logger = LogManager.getLogger(ReservationServiceImpl.class);

    private ReservationRepository reservationRepository;
    private ReservationMapper reservationMapper;

    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository, ReservationMapper reservationMapper) {
        this.reservationRepository = reservationRepository;
        this.reservationMapper = reservationMapper;
    }

    //DONE javadoc

    /**
     * Permet d'enregister une réservation dans la BdD. Interface entre le controller et le repository
     *
     * @param reservationDto le DTO réservation
     * @return la réservation enregistrée
     */
    @Override
    public ReservationDto createReservation(ReservationDto reservationDto) {
        reservationDto.setMailEnvoye(false);
        ReservationDto savedReservationDto;
        Reservation newReservation = reservationMapper.reservationDtoToReservation(reservationDto);
        Reservation savedReservation = reservationRepository.save(newReservation);
        savedReservationDto = reservationMapper.reservationToReservationDto(savedReservation);
        return savedReservationDto;
    }

    /**
     * Récupère la liste de toutes les réservations enregistrées
     *
     * @return la liste
     */
    @Override
    public List<ReservationDto> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        List<ReservationDto> reservationDtos =
                reservations.stream()
                        .map(reservation -> reservationMapper.reservationToReservationDto(reservation))
                        .collect(Collectors.toList());
        return reservationDtos;
    }

    /**
     * Recherche une réservation par son identifiant
     *
     * @param reservationId l'identifiant de la réservation
     * @return la réservation concernée
     */
    @Override
    public ReservationDto getReservationById(Long reservationId) throws TechnicalException {
        ReservationDto reservationDto;
        try {
            Reservation reservation = reservationRepository.getById(reservationId);
            reservationDto = reservationMapper.reservationToReservationDto(reservation);
        } catch (EntityNotFoundException e) {
            throw new TechnicalException(ErrorMessage.INTROUVABLE_EXCEPTION.getErrorMessage());
        }

        return reservationDto;
    }

    /**
     * Récupère la liste des réservations non expirées d'un utilisateur
     *
     * @param userId l'identifiant de l'utilisateur
     * @return la liste
     */
    @Override
    public List<ReservationDto> getReservationsByUserId(Long userId) throws TechnicalException {
        List<ReservationDto> reservationDtos;
        try {
            List<Reservation> reservations = reservationRepository
                    .getReservationsByUtilisateurIdOrderByDateReservation(userId);
            reservationDtos = reservations.stream()
                    .map(reservation -> reservationMapper.reservationToReservationDto(reservation))
                    .collect(Collectors.toList());
        } catch (EntityNotFoundException e) {
            throw new TechnicalException(ErrorMessage.INTROUVABLE_EXCEPTION.getErrorMessage());
        }
        return reservationDtos;
    }

    /**
     * Renvoie le nombre de réservations non expirées pour un livre et un bibliotheque
     *
     * @param livreId        l'identifiant du livre
     * @param bibliothequeId l'identifiant de la bibliotheque
     * @return le nombre de reservation en cours
     */
    @Override
    public Integer getNombreDeReservation(Long livreId, Long bibliothequeId) throws TechnicalException {
        Integer nbReservation;
        try {
            nbReservation = reservationRepository.getNombreDeReservationByLivreIdAndBibliothequeId(livreId, bibliothequeId);
        } catch (EntityNotFoundException e) {
            throw new TechnicalException(ErrorMessage.INTROUVABLE_EXCEPTION.getErrorMessage());
        }
        logger.info("Nombre de reservation = " + nbReservation);
        return nbReservation;
    }

    /**
     * Récupère une liste de réservation pour un livre dans une bibliothèque ordonnée par date
     *
     * @param livreId        l'identifiant du livre
     * @param bibliothequeId l'identifiant de la bibliothèque
     * @return la liste ordonnée
     */
    @Override
    public List<ReservationDto> getReservationByLivreIdAndAndBibliothequeIdOrderByDateReservation(Long livreId, Long bibliothequeId) throws TechnicalException {
        List<ReservationDto> reservationDtos;
        try {
            List<Reservation> reservations = reservationRepository.getReservationByLivreIdAndAndBibliothequeIdOrderByDateReservation(livreId, bibliothequeId);
            reservationDtos = reservations.stream()
                    .map(reservation -> reservationMapper.reservationToReservationDto(reservation))
                    .collect(Collectors.toList());
        } catch (EntityNotFoundException e) {
            throw new TechnicalException(ErrorMessage.INTROUVABLE_EXCEPTION.getErrorMessage());
        }
        return reservationDtos;
    }

    /**
     * Supprime une réservation
     *
     * @param reservationId l'identifiant de la réservation à supprimer
     */
    @Override
    public void deleteReservation(Long reservationId) {
        reservationRepository.deleteById(reservationId);
    }

        //DONE T1 : Mail Service

        // ----------------- Mail Service Methode -----------------------

        /**
         * Récupère une liste de réservations avec l'identifiant du livre.
         *
         * @param livreId l'identifiant du livre
         * @return la liste
         */
        @Override
        public List<ReservationDto> getReservationsByLivreIdOrderByDateReservation (Long livreId) throws
        TechnicalException {
            List<ReservationDto> reservationDtos;
            try {
                List<Reservation> reservations = reservationRepository.getReservationsByLivreIdOrderByDateReservation(livreId);
                reservationDtos = reservations.stream()
                        .map(reservation -> reservationMapper.reservationToReservationDto(reservation))
                        .collect(Collectors.toList());
            } catch (EntityNotFoundException e) {
                throw new TechnicalException(ErrorMessage.INTROUVABLE_EXCEPTION.getErrorMessage());
            }
            return reservationDtos;
        }

        /**
         * Met à jour une réservation
         *
         * @param reservationDto la réservation à mettre à jour
         */
        @Override
        public void updateReservation (ReservationDto reservationDto){
            Reservation reservationToUpdate = reservationMapper.reservationDtoToReservation(reservationDto);
            reservationRepository.save(reservationToUpdate);
        }
    }
