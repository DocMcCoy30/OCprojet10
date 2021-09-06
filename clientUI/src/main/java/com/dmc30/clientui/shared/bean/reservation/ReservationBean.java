package com.dmc30.clientui.shared.bean.reservation;

import com.dmc30.clientui.shared.bean.bibliotheque.OuvrageBean;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ReservationBean {

    private Long id;
    private Date dateReservation;
    private boolean expiree;
    private Long utilisateurId;
    List<OuvrageBean> ouvrages;
}
