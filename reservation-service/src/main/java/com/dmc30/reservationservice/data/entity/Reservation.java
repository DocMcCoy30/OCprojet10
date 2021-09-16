package com.dmc30.reservationservice.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "reservation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_reservation")
    private Date dateReservation;

    @Column(name = "expiree")
    private boolean expiree;

    @Column(name = "mail_envoye")
    private boolean mailEnvoye;

    @Column(name = "date_envoi_mail")
    private Date dateEnvoiMail;

    @Column(name = "id_utilisateur")
    private Long utilisateurId;

    @Column(name = "id_livre")
    private Long livreId;

    @Column(name = "id_bibliotheque")
    private Long bibliothequeId;
}
