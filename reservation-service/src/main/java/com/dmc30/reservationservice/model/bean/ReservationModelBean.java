package com.dmc30.reservationservice.model.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ReservationModelBean {

        private Long id;
        private Date dateReservation;
        private String titreDuLivre;
        private Integer numeroAttente;
        private String dateRetourPrevu;
}