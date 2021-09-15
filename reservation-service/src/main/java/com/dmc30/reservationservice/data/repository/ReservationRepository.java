package com.dmc30.reservationservice.data.repository;

import com.dmc30.reservationservice.data.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query(value = "SELECT * FROM reservation WHERE id_utilisateur=?1 ORDER BY date_reservation", nativeQuery = true)
    List<Reservation> getReservationsByUtilisateurIdOrderByDateReservation(Long userId);

    @Query(value = "SELECT COUNT(id) FROM reservation WHERE id_livre=?1 AND id_bibliotheque=?2 AND expiree=false", nativeQuery = true)
    Integer getNombreDeReservationByLivreIdAndBibliothequeId(Long livreId, Long bibliothequeId);

    @Query(value = "SELECT * FROM reservation WHERE id_livre=?1 AND id_bibliotheque=?2 AND expiree=false ORDER BY date_reservation", nativeQuery = true)
    List<Reservation> getReservationByLivreIdAndAndBibliothequeIdOrderByDateReservation(Long livreId, Long bibliothequeId);

    //DONE T1 : Mail Service Méthode

    //----------------- Mail Service Methode ----------------------

    @Query(value = "SELECT * FROM reservation WHERE id_livre=?1 ORDER BY date_reservation", nativeQuery = true)
    List<Reservation> getReservationByLivreIdOrderByDateReservation(Long livreId);
}
