package com.dmc30.emailservice.service.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdresseBean {

    private String rue;
    private String codePostal;
    private String ville;
    private PaysBean pays;
}
