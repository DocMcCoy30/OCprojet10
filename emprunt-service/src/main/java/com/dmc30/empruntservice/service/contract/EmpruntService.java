package com.dmc30.empruntservice.service.contract;

import com.dmc30.empruntservice.service.dto.CreateEmpruntDto;
import com.dmc30.empruntservice.service.dto.EmpruntDto;
import com.dmc30.empruntservice.web.exception.TechnicalException;

import java.util.List;

public interface EmpruntService {

    EmpruntDto createEmprunt(CreateEmpruntDto createEmpruntDto) throws TechnicalException;

    List<EmpruntDto> findEmpruntEnCours(Long bibliothequeId) throws TechnicalException;

    List<EmpruntDto> findEmpruntByUtilisateurId(Long utilisateurId) throws TechnicalException;

    void retournerEmprunt(Long empruntId, String ouvrageId) throws TechnicalException;

    void prolongerEmprunt(Long empruntId) throws TechnicalException;

    List<EmpruntDto> findExpiredEmprunts() throws TechnicalException;

    List<EmpruntDto> findExpiredEmpruntsByUtilisateurId(Long utilisateurId) throws TechnicalException;

    List<Long> findUtilisateurEnRetard() throws TechnicalException;

    EmpruntDto getEmpruntEnCoursByOuvrageId(Long ouvrageId) throws TechnicalException;

    // ---------------------Mail Service Methode --------------------------

    List<EmpruntDto> getEmpruntsRestitues() throws TechnicalException;
}
