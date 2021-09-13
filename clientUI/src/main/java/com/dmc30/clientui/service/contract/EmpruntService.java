package com.dmc30.clientui.service.contract;

import com.dmc30.clientui.shared.bean.bibliotheque.CreateEmpruntBean;
import com.dmc30.clientui.shared.bean.bibliotheque.EmpruntBean;
import com.dmc30.clientui.web.exception.TechnicalException;

import java.util.List;

public interface EmpruntService {

    EmpruntBean createEmprunt(CreateEmpruntBean createEmpruntBean) throws TechnicalException;
    List<EmpruntBean> getEmpruntsEnCours(Long bibliothequeId) throws TechnicalException;
    List<EmpruntBean> getEmpruntByUtilisateurId(Long utilisateurId) throws TechnicalException;
    void retournerEmprunt(Long empruntId, String ouvrageId) throws TechnicalException;
    void prolongerEmprunt(Long empruntId) throws TechnicalException;
    String getDateDeRetourPrevue(Long livreId, Long bibliothequeId) throws TechnicalException;
}
