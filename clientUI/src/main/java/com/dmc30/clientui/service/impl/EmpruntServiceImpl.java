package com.dmc30.clientui.service.impl;

import com.dmc30.clientui.shared.bean.bibliotheque.CreateEmpruntBean;
import com.dmc30.clientui.shared.bean.bibliotheque.EmpruntBean;
import com.dmc30.clientui.proxy.EmpruntServiceProxy;
import com.dmc30.clientui.service.contract.EmpruntService;
import com.dmc30.clientui.web.exception.ErrorMessage;
import com.dmc30.clientui.web.exception.TechnicalException;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmpruntServiceImpl implements EmpruntService {

    EmpruntServiceProxy empruntServiceProxy;

    @Autowired
    public EmpruntServiceImpl(EmpruntServiceProxy empruntServiceProxy) {
        this.empruntServiceProxy = empruntServiceProxy;
    }

    @Override
    public EmpruntBean createEmprunt(CreateEmpruntBean createEmpruntBean) throws TechnicalException {
        EmpruntBean empruntBean = new EmpruntBean();
        try {
            empruntBean = empruntServiceProxy.createEmprunt(createEmpruntBean);
        } catch (FeignException e) {
            throw new TechnicalException(ErrorMessage.TECHNICAL_ERROR.getErrorMessage());
        }
        return empruntBean;
    }

    @Override
    public List<EmpruntBean> getEmpruntsEnCours(Long bibliothequeId) throws TechnicalException {
        List<EmpruntBean> empruntBeanList = new ArrayList<>();
        try {
            empruntBeanList = empruntServiceProxy.findEmpruntEnCours(bibliothequeId);
        } catch (FeignException e) {
            throw new TechnicalException(ErrorMessage.TECHNICAL_ERROR.getErrorMessage());
        }
        return empruntBeanList;
    }

    @Override
    public List<EmpruntBean> getEmpruntByUtilisateurId(Long utilisateurId) throws TechnicalException {
        List<EmpruntBean> empruntBeanList = new ArrayList<>();
        try {
            empruntBeanList = empruntServiceProxy.findEmpruntByUtilisateurId(utilisateurId);
        } catch (FeignException e) {
            throw new TechnicalException(ErrorMessage.TECHNICAL_ERROR.getErrorMessage());
        }
        return empruntBeanList;
    }


    @Override
    public void retournerEmprunt(Long empruntId, String ouvrageId) throws TechnicalException {
        try {
            empruntServiceProxy.retournerEmprunt(empruntId, ouvrageId);

        } catch (FeignException e) {
            throw new TechnicalException(ErrorMessage.TECHNICAL_ERROR.getErrorMessage());
        }
    }

    @Override
    public void prolongerEmprunt(Long empruntId) throws TechnicalException {
        try {
        empruntServiceProxy.prolongerEmprunt(empruntId);
    } catch (FeignException e) {
            throw new TechnicalException(ErrorMessage.TECHNICAL_ERROR.getErrorMessage());
        }
    }
}
