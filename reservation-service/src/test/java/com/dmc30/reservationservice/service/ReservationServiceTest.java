package com.dmc30.reservationservice.service;


import com.dmc30.reservationservice.data.entity.Reservation;
import com.dmc30.reservationservice.data.repository.ReservationRepository;
import com.dmc30.reservationservice.model.mappers.ReservationMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class ReservationServiceTest {

    @Mock
    ReservationRepository reservationRepository;
    @Mock
    ReservationMapper reservationMapper;

    @Test
    @DisplayName("getReservationById")
    public void getReservationById_ShouldReturnAReservationDtoByItsId() {

    }


    //------------------- STUBS -------------------------

    Reservation generateAReservation() {
        Reservation reservation = new Reservation();

        return null;
    }
}
