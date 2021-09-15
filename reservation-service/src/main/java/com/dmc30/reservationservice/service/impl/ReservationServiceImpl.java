package com.dmc30.reservationservice.service.impl;

import com.dmc30.reservationservice.data.entity.Reservation;
import com.dmc30.reservationservice.data.repository.ReservationRepository;
import com.dmc30.reservationservice.model.dto.ReservationDto;
import com.dmc30.reservationservice.model.mappers.ReservationMapper;
import com.dmc30.reservationservice.service.contract.ReservationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
     * @param reservationDto le DTO réservation
     * @return la réservation enregistrée
     */
    @Override
    public ReservationDto createReservation(ReservationDto reservationDto) {
        Reservation newReservation = reservationMapper.reservationDtoToReservation(reservationDto);
        Reservation savedReservation = reservationRepository.save(newReservation);
        ReservationDto savedReservationDto = reservationMapper.reservationToReservationDto(savedReservation);
        return savedReservationDto;
    }

    /**
     * Récupère la liste de toutes les réservations enregistrées
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
     * @param reservationId l'identifiant de la réservation
     * @return la réservation concernée
     */
    @Override
    public ReservationDto getReservationById(Long reservationId) {
        Reservation reservation = reservationRepository.getById(reservationId);
        ReservationDto reservationDto = reservationMapper.reservationToReservationDto(reservation);
        return reservationDto;
    }

    /**
     * Récupère la liste des réservations non expirées d'un utilisateur
     * @param userId l'identifiant de l'utilisateur
     * @return la liste
     */
    @Override
    public List<ReservationDto> getReservationsByUserId(Long userId) {
        List<Reservation> reservations = reservationRepository
                .getReservationsByUtilisateurIdOrderByDateReservation(userId);
        List<ReservationDto> reservationDtos = reservations.stream()
                .map(reservation -> reservationMapper.reservationToReservationDto(reservation) )
                .collect(Collectors.toList());
        return reservationDtos;
    }

    /**
     * Renvoie le nombre de réservations non expirées pour un livre et un bibliotheque
     * @param livreId l'identifiant du livre
     * @param bibliothequeId l'identifiant de la bibliotheque
     * @return le nombre de reservation en cours
     */
    @Override
    public Integer getNombreDeReservation(Long livreId, Long bibliothequeId) {
        Integer nbReservation = reservationRepository.getNombreDeReservationByLivreIdAndBibliothequeId(livreId,bibliothequeId);
        logger.info("Nombre de reservation = " + nbReservation);
        return nbReservation;
    }

    /**
     * Récupère une liste de réservation pour un livre dans une bibliothèque ordonnée par date
     * @param livreId l'identifiant du livre
     * @param bibliothequeId l'identifiant de la bibliothèque
     * @return la liste ordonnée
     */
    @Override
    public List<ReservationDto> getReservationByLivreIdAndAndBibliothequeIdOrderByDateReservation(Long livreId, Long bibliothequeId) {
        List<Reservation> reservations = reservationRepository.getReservationByLivreIdAndAndBibliothequeIdOrderByDateReservation(livreId, bibliothequeId);
        List<ReservationDto> reservationDtos = reservations.stream()
                .map(reservation -> reservationMapper.reservationToReservationDto(reservation) )
                .collect(Collectors.toList());
        return reservationDtos;
    }

    /**
     * Supprime une réservation
     * @param reservationId l'identifiant de la réservation à supprimer
     */
    @Override
    public void deleteReservation(Long reservationId) {
        reservationRepository.deleteById(reservationId);
    }


    //DONE T1 : Mail Service

    // ----------------- Mail Service Methode -----------------------
    @Override
    public List<ReservationDto> getReservationsByLivreId(Long livreId) {
        List<Reservation> reservationsByLivreId = reservationRepository.getReservationByLivreIdOrderByDateReservation(livreId);
        List<ReservationDto> reservationsByLivreIdDto = reservationsByLivreId.stream()
                .map(reservation -> reservationMapper.reservationToReservationDto(reservation))
                .collect(Collectors.toList());
        return reservationsByLivreIdDto;
    }

    @Override
    public void updateReservation(ReservationDto reservationDto) {
        Reservation reservationToUpdate = reservationMapper.reservationDtoToReservation(reservationDto);
        reservationRepository.save(reservationToUpdate);
    }
}
