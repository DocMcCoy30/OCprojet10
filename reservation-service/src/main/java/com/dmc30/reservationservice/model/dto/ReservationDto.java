package com.dmc30.reservationservice.model.dto;

import com.dmc30.reservationservice.model.bean.OuvrageBean;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class ReservationDto {

    private Long id;
    private Date dateReservation;
    private boolean expiree;
    private Long utilisateurId;
    List<OuvrageBean> ouvrages;
}
