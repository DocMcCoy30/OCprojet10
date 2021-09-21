package com.dmc30.empruntservice.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
public class OuvrageDto {

    private Long id;

    private String idInterne;

    private boolean emprunte;

    private Long bibliothequeId;

}
