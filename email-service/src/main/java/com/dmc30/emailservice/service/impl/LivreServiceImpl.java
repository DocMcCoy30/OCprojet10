package com.dmc30.emailservice.service.impl;

import com.dmc30.emailservice.proxy.LivreServiceProxy;
import com.dmc30.emailservice.service.bean.LivreBean;
import com.dmc30.emailservice.service.bean.LivreForMailBean;
import com.dmc30.emailservice.service.bean.OuvrageBean;
import com.dmc30.emailservice.service.contract.LivreService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class LivreServiceImpl implements LivreService {

    LivreServiceProxy livreServiceProxy;

    @Autowired
    public LivreServiceImpl(LivreServiceProxy livreServiceProxy) {
        this.livreServiceProxy = livreServiceProxy;
    }

    //TODO : javadoc

    @Override
    public LivreForMailBean getTitreDuLivre(Long ouvrageId) {
        ObjectMapper mapper = new ObjectMapper();
        LivreForMailBean livre = new LivreForMailBean();
        OuvrageBean ouvrage = livreServiceProxy.getOuvrageById(ouvrageId);
        ResponseEntity<?> responseEntity = livreServiceProxy.getLivreById(ouvrage.getLivreId());
        LivreBean livreBean = mapper.convertValue(responseEntity.getBody(), LivreBean.class);
        String titre = livreBean.getTitre();
        livre.setTitre(titre);
        return livre;
    }

    @Override
    public LivreBean getLivreById(Long livreId) {
        ObjectMapper mapper = new ObjectMapper();
        ResponseEntity<?> responseEntity = livreServiceProxy.getLivreById(livreId);
        LivreBean livre = mapper.convertValue(responseEntity.getBody(), LivreBean.class);
        return livre;
    }
}
