package com.dmc30.empruntservice.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor @NoArgsConstructor
public class CreateEmpruntDto {

    private Long abonneId;
    private String numAbonne;
    private String prenom;
    private String nom;
    private String numTelephone;
    private Long ouvrageId;
    private String idInterne;
    private String titre;
    private String auteur;

}
