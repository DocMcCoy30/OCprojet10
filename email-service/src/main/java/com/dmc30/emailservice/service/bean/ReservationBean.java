package com.dmc30.emailservice.service.bean;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.Date;

@Getter
@Setter
public class ReservationBean {

    private Long id;
    private Date dateReservation;
    private ZonedDateTime dateReservationTz;
    private boolean expiree;
    private boolean mailEnvoye;
    private Date dateEnvoiMail;
    private ZonedDateTime dateEnvoiMailTz;
    private Long utilisateurId;
    private Long livreId;
    private Long bibliothequeId;
}
