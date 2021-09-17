package com.dmc30.clientui.service.impl;

import com.dmc30.clientui.proxy.LivreServiceProxy;
import com.dmc30.clientui.service.contract.BibliothequeService;
import com.dmc30.clientui.shared.bean.bibliotheque.BibliothequeBean;
import com.dmc30.clientui.web.exception.ErrorMessage;
import com.dmc30.clientui.web.exception.TechnicalException;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BibliothequeServiceImpl implements BibliothequeService {

    LivreServiceProxy livreServiceProxy;

    @Autowired
    public BibliothequeServiceImpl(LivreServiceProxy livreServiceProxy) {
        this.livreServiceProxy = livreServiceProxy;
    }

    /**
     * Cherche la liste de toutes les bibliotheques
     *
     * @return la liste
     */
    @Override
    public List<BibliothequeBean> getBibliotheques() throws TechnicalException {
        List<BibliothequeBean> bibliotheques = new ArrayList<>();
        try {
            bibliotheques = livreServiceProxy.getBibliotheques();
        } catch (FeignException e) {
            throw new TechnicalException(ErrorMessage.TECHNICAL_ERROR.getErrorMessage());
        }
        return bibliotheques;
    }

    /**
     * Recherche d'une bibliothèque par son identifiant
     *
     * @param bibliothequeId l'identifiant de la bibliotheque
     * @return la bibliothèque recherchée
     */
    @Override
    public BibliothequeBean getBibliothequeById(Long bibliothequeId) throws TechnicalException {
        BibliothequeBean bibliotheque = null;
        try {
            bibliotheque = livreServiceProxy.getBibliothequeById(bibliothequeId);
        } catch (FeignException e) {
            throw new TechnicalException(ErrorMessage.INTROUVABLE_EXCEPTION.getErrorMessage());
        }
        return bibliotheque;
    }


}
