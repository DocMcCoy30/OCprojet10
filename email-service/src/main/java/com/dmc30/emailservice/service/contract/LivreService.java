package com.dmc30.emailservice.service.contract;

import com.dmc30.emailservice.service.bean.LivreBean;
import com.dmc30.emailservice.service.bean.LivreForMailBean;

public interface LivreService {

    LivreForMailBean getTitreDuLivre(Long ouvrageId);
    LivreBean getLivreById(Long livreId);

}
