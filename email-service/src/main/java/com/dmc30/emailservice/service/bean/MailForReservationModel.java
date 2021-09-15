package com.dmc30.emailservice.service.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailForReservationModel {

    private Long userId;
    private String username;
    private String prenom;
    private String nom;
    private String email;
    private String titre;
    private String dateReservation;
    private String bibliotheque;
}
