package com.dmc30.clientui.service.contract;

import com.dmc30.clientui.shared.bean.bibliotheque.OuvrageBean;
import com.dmc30.clientui.shared.bean.bibliotheque.OuvrageResponseModelBean;
import com.dmc30.clientui.web.exception.TechnicalException;

import java.util.List;

public interface OuvrageService {

    Integer getOuvrageDispoInOneBibliotheque(Long livreId, Long bibliothequeId) throws TechnicalException;
    List<Object> getOuvrageDispoInOtherBibliotheque(Long livreId, Long bibliothequeId) throws TechnicalException;
    OuvrageResponseModelBean getOuvrageById(Long ouvrageId) throws TechnicalException;
    List<OuvrageResponseModelBean> getOuvragesByIdInterne(String idInterne) throws TechnicalException;
    OuvrageResponseModelBean getOuvrageByIdInterne(String idInterne) throws TechnicalException;
    Integer getNombreDOuvrage(Long livreId, Long bibliothequeId);
    List<OuvrageBean> getOuvrageByLivreIdAndBibliothequeId(Long livreId, Long bibliothequeId);
}
