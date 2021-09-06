package com.dmc30.reservationservice.data.repository;

import com.dmc30.reservationservice.data.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
