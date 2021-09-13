package com.dmc30.clientui.service.contract;

import org.springframework.http.ResponseEntity;

public interface BibliothequeService {

    ResponseEntity<?> getBibliotheques();
    ResponseEntity<?> getBibliothequeById(Long bibliothequeId);
}
