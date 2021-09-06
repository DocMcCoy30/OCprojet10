package com.dmc30.reservationservice.data.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "reservation")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Reservation {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "date_reservation")
    private Date dateReservation;

    @Column(name = "expiree")
    private boolean expiree;

    @Column(name = "id_utilisateur")
    private Long utilisateurId;

}
