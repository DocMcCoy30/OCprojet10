package com.dmc30.reservationservice.model.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OuvrageBean {

    private Long id;
    private String idInterne;
    private boolean emprunte;
    private Long bibliothequeId;
    private Long livreId;
}
