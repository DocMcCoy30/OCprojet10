package com.dmc30.emailservice.proxy;

import com.dmc30.emailservice.service.bean.OuvrageBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "livre-service", url = "localhost:9002")
public interface LivreServiceProxy {

    @GetMapping("/livres/livreId") //
    ResponseEntity<?> getLivreById(@RequestParam("livreId") Long livreId);

    @GetMapping("/ouvrages/ouvrage/id")
    OuvrageBean getOuvrageById(@RequestParam("ouvrageId") Long ouvrageId);

    @PostMapping("/bibliotheques/id")
    ResponseEntity<?>getBibliothequeById(@RequestParam("bibliothequeId") Long bibliothequeId);
}
