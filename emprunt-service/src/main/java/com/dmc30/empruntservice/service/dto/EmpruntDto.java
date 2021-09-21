package com.dmc30.empruntservice.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
public class EmpruntDto {

    private Long id;

    private Date dateEmprunt;

    private Date dateRestitution;

    private Date dateProlongation;

    private Boolean prolongation;

    private boolean restitution;

    private Long ouvrageId;

    private Long utilisateurId;
}
