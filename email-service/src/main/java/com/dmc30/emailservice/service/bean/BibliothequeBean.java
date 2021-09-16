package com.dmc30.emailservice.service.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BibliothequeBean {

    private Long id;
    private String code;
    private String numSiret;
    private String nom;
    private AdresseBean adresse;
    private List<OuvrageBean> ouvrages;



}
