package com.dmc30.reservationservice.data.repository;

import com.dmc30.reservationservice.data.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> getReservationsByUtilisateurIdOrderByDateReservation(Long userId);
}
