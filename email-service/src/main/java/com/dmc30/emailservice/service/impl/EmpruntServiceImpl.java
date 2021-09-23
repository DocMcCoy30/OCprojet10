package com.dmc30.emailservice.service.impl;

import com.dmc30.emailservice.proxy.EmpruntServiceProxy;
import com.dmc30.emailservice.service.bean.EmpruntBean;
import com.dmc30.emailservice.service.contract.EmpruntService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpruntServiceImpl implements EmpruntService {

    EmpruntServiceProxy empruntServiceProxy;

    @Autowired
    public EmpruntServiceImpl(EmpruntServiceProxy empruntServiceProxy) {
        this.empruntServiceProxy = empruntServiceProxy;
    }

    @Override
    public List<EmpruntBean> findExpiredemprunts() {
        List<EmpruntBean> empruntBeans = empruntServiceProxy.findExpiredemprunts();
        return empruntBeans;
    }

    @Override
    public List<Long> findUtilisateurEnRetard() {
        return empruntServiceProxy.findUtilisateurEnRetard();
    }

    @Override
    public List<EmpruntBean> findExpiredempruntsByUtilisateurId(Long utilisateurId) {
        List<EmpruntBean> empruntBeans = empruntServiceProxy.findExpiredempruntsByUtilisateurId(utilisateurId);
        return empruntBeans;
    }
}
