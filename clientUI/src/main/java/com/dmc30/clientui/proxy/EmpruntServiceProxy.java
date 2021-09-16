package com.dmc30.clientui.proxy;

import com.dmc30.clientui.shared.bean.bibliotheque.CreateEmpruntBean;
import com.dmc30.clientui.shared.bean.bibliotheque.EmpruntBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "emprunt-service", url = "localhost:9003")
public interface EmpruntServiceProxy {

    @PostMapping("/emprunts/create")
    EmpruntBean createEmprunt(@RequestBody CreateEmpruntBean createEmpruntBean);

    @GetMapping("/emprunts/encours")
    List<EmpruntBean> findEmpruntEnCours(@RequestParam Long bibliothequeId);

    @GetMapping("/emprunts/utilisateur")
    List<EmpruntBean> findEmpruntByUtilisateurId(@RequestParam Long utilisateurId);

    @GetMapping("/emprunts/retour")
    void retournerEmprunt(@RequestParam Long empruntId, @RequestParam String ouvrageId);

    @GetMapping("/emprunts/prolongation")
    void prolongerEmprunt(@RequestParam Long empruntId);

    @GetMapping("/emprunts/ouvrage/{ouvrageId}")
    EmpruntBean getEmpruntEnCoursByOuvrageId(@PathVariable(name = "ouvrageId") Long id);
}
