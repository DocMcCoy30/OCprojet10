package com.dmc30.livreservice.service.contract;

import org.springframework.http.ResponseEntity;

public interface BibliothequeService {

    ResponseEntity<?> findAll();

    ResponseEntity<?> findById(Long Bibliothequeid);

}
