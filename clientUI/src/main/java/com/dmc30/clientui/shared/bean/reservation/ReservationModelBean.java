package com.dmc30.clientui.shared.bean.reservation;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ReservationModelBean {

        private Long id;
        private String dateReservation;
        private String titreDuLivre;
        private Integer numeroAttente;
        private String dateRetourPrevu;
        private boolean expiree;
        String bibliotheque;
}
