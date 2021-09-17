package com.dmc30.clientui.service.contract;

import com.dmc30.clientui.shared.bean.bibliotheque.BibliothequeBean;
import com.dmc30.clientui.web.exception.TechnicalException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BibliothequeService {

    List<BibliothequeBean> getBibliotheques() throws TechnicalException;
    BibliothequeBean getBibliothequeById(Long bibliothequeId) throws TechnicalException;
}
