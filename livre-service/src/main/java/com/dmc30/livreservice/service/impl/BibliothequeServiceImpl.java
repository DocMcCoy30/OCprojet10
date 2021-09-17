package com.dmc30.livreservice.service.impl;

import com.dmc30.livreservice.data.entity.bibliotheque.Bibliotheque;
import com.dmc30.livreservice.data.repository.BibliothequeRepository;
import com.dmc30.livreservice.web.exception.ErrorMessage;
import com.dmc30.livreservice.service.contract.BibliothequeService;
import com.dmc30.livreservice.service.dto.bibliotheque.BibliothequeDto;
import com.dmc30.livreservice.web.exception.TechnicalException;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BibliothequeServiceImpl implements BibliothequeService {

    BibliothequeRepository bibliothequeRepository;

    @Autowired
    public BibliothequeServiceImpl(BibliothequeRepository bibliothequeRepository) {
        this.bibliothequeRepository = bibliothequeRepository;
    }

    /**
     * Récupère la liste de toutes les bibliothèques de la BD
     *
     * @return la liste des bibliothèques
     */
    @Override
    public List<BibliothequeDto> findAll() throws TechnicalException {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        List<BibliothequeDto> bibliothequeDtos = new ArrayList<>();
        try {
            List<Bibliotheque> bibliotheques = bibliothequeRepository.findAll();
            for (Bibliotheque bibliotheque : bibliotheques) {
                bibliothequeDtos.add(modelMapper.map(bibliotheque, BibliothequeDto.class));
            }
            return bibliothequeDtos;
        } catch (Exception e) {
            throw new TechnicalException(ErrorMessage.TECHNICAL_ERROR.getErrorMessage());
        }
    }

    /**
     * Cherche un objet bibliothèque dans la BD par son identifiant. Si l'identifiant n'est pas précisé, renvoie la 1ère bibliothèque enregistrée.
     *
     * @param bibliothequeId L'identifiant de la bibliothèque rcherchée
     * @return un objet bibliothèque
     */
    @Override
    public BibliothequeDto findById(Long bibliothequeId) throws TechnicalException {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        BibliothequeDto bibliothequeDto;
        if (bibliothequeId == null) {
            bibliothequeId = 1L;
        }
        try {
            Bibliotheque bibliotheque = bibliothequeRepository.findBibliothequeById(bibliothequeId);
            if (bibliotheque != null) {
                bibliothequeDto = modelMapper.map(bibliotheque, BibliothequeDto.class);
            } else {
                throw new TechnicalException(ErrorMessage.INTROUVABLE_EXCEPTION.getErrorMessage());
            }
        } catch (IllegalArgumentException e1) {
            throw new TechnicalException(ErrorMessage.INTROUVABLE_EXCEPTION.getErrorMessage());
        } catch (Exception e2) {
            throw new TechnicalException(ErrorMessage.TECHNICAL_ERROR.getErrorMessage());
        }
        return bibliothequeDto;
    }
}
