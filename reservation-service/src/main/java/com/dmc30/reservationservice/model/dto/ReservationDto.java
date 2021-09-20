package com.dmc30.reservationservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.Date;

@Data
@AllArgsConstructor @NoArgsConstructor
public class ReservationDto {

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
