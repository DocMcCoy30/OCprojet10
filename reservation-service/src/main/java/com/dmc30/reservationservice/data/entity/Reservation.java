package com.dmc30.reservationservice.data.entity;

import com.dmc30.reservationservice.model.bean.OuvrageBean;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "reservation")
@Data @NoArgsConstructor @AllArgsConstructor
public class Reservation {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_reservation")
    private Date dateReservation;

    @Column(name = "expiree")
    private boolean expiree;

    @Column(name = "id_utilisateur")
    private Long utilisateurId;

    @Column(name = "id_livre")
    private Long livreId;

    @Column(name = "id_bibliotheque")
    private Long bibliothequeId;

}
