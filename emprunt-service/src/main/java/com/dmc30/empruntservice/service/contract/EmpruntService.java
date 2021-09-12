package com.dmc30.empruntservice.service.contract;

import com.dmc30.empruntservice.service.dto.CreateEmpruntDto;
import com.dmc30.empruntservice.service.dto.EmpruntDto;

import java.util.List;

public interface EmpruntService {

    EmpruntDto createEmprunt(CreateEmpruntDto createEmpruntDto);

    List<EmpruntDto> findEmpruntEnCours(Long bibliothequeId);

    List<EmpruntDto> findEmpruntByUtilisateurId(Long utilisateurId);

    void retournerEmprunt(Long empruntId, String ouvrageId);

    void prolongerEmprunt(Long empruntId);

    List<EmpruntDto> findExpiredemprunts();

    List<EmpruntDto> findExpiredempruntsByUtilisateurId(Long utilisateurId);

    List<Long> findUtilisateurEnRetard();

    EmpruntDto getEmpruntEnCoursByOuvrageId(Long ouvrageId);
}
