package com.dmc30.emailservice.service.contract;

import com.dmc30.emailservice.service.bean.EmpruntBean;

import java.util.List;

public interface EmpruntService {

    List<EmpruntBean> findExpiredemprunts();
    List<Long> findUtilisateurEnRetard();
    List<EmpruntBean> findExpiredempruntsByUtilisateurId(Long utilisateurId);
}
