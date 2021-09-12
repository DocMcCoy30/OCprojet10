package com.dmc30.emailservice.proxy;

import com.dmc30.emailservice.service.bean.EmpruntBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "emprunt-service", url = "localhost:9003")
public interface EmpruntServiceProxy {

    @GetMapping("/emprunts/expired")
    List<EmpruntBean> findExpiredemprunts();

    @GetMapping("/emprunts/expiredByUtilisateur")
    List<EmpruntBean> findExpiredempruntsByUtilisateurId(@RequestParam ("utilisateurId") Long utilisateurId);

    @GetMapping("/emprunts/expiredUsers")
    List<Long> findUtilisateurEnRetard();
}
