package com.dmc30.emailservice.service.impl;

import com.dmc30.emailservice.proxy.LivreServiceProxy;
import com.dmc30.emailservice.service.bean.BibliothequeBean;
import com.dmc30.emailservice.service.contract.BibliothequeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BibliothequeServiceImpl implements BibliothequeService {

    LivreServiceProxy livreServiceProxy;

    @Autowired
    public BibliothequeServiceImpl(LivreServiceProxy livreServiceProxy) {
        this.livreServiceProxy = livreServiceProxy;
    }

    @Override
    public BibliothequeBean getBibliothequeById(Long bibliothequeId) {
        ObjectMapper mapper = new ObjectMapper();
        ResponseEntity<?> response = livreServiceProxy.getBibliothequeById(bibliothequeId);
        BibliothequeBean bibliotheque = mapper.convertValue(response.getBody(), BibliothequeBean.class);
        return bibliotheque;
    }
}
