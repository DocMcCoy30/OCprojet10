package com.dmc30.clientui.shared.bean.reservation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor @NoArgsConstructor
public class ReservationBean {

    private Long id;
    private Date dateReservation;
    private ZonedDateTime dateReservationTz;
    private boolean expiree;
    private Long utilisateurId;
    private Long livreId;
    private Long bibliothequeId;
}
