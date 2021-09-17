package com.dmc30.livreservice.service.contract;

import com.dmc30.livreservice.data.entity.bibliotheque.Bibliotheque;
import com.dmc30.livreservice.service.dto.bibliotheque.BibliothequeDto;
import com.dmc30.livreservice.web.exception.TechnicalException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BibliothequeService {

    List<BibliothequeDto> findAll() throws TechnicalException;

    BibliothequeDto findById(Long Bibliothequeid) throws TechnicalException;

}
