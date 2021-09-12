package com.dmc30.empruntservice.service.impl;

import com.dmc30.empruntservice.data.entity.Emprunt;
import com.dmc30.empruntservice.data.entity.Ouvrage;
import com.dmc30.empruntservice.data.repository.OuvrageRepository;
import com.dmc30.empruntservice.data.repository.EmpruntRepository;
import com.dmc30.empruntservice.service.dto.CreateEmpruntDto;
import com.dmc30.empruntservice.service.dto.OuvrageDto;
import com.dmc30.empruntservice.service.dto.EmpruntDto;
import com.dmc30.empruntservice.service.contract.EmpruntService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class EmpruntServiceImpl implements EmpruntService {

    OuvrageRepository ouvrageRepository;
    EmpruntRepository empruntRepository;

    @Autowired
    public EmpruntServiceImpl(OuvrageRepository ouvrageRepository, EmpruntRepository empruntRepository) {
        this.ouvrageRepository = ouvrageRepository;
        this.empruntRepository = empruntRepository;
    }

    /**
     * Crée un nouvel emprunt dans la base de données
     *
     * @param createEmpruntDto les paramètres de l'emprunt
     */
    @Override
    public EmpruntDto createEmprunt(CreateEmpruntDto createEmpruntDto) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date dateEmprunt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dateEmprunt);
        c.add(Calendar.DAY_OF_MONTH, 31);
        Date dateRestitution = c.getTime();
        Ouvrage ouvrage = ouvrageRepository.getById(createEmpruntDto.getOuvrageId());
        ouvrage.setEmprunte(true);
        ouvrageRepository.save(ouvrage);
        EmpruntDto empruntDto = new EmpruntDto();
        empruntDto.setDateEmprunt(dateEmprunt);
        empruntDto.setDateRestitution(dateRestitution);
        empruntDto.setProlongation(false);
        empruntDto.setRestitution(false);
        empruntDto.setOuvrageId(ouvrage.getId());
        empruntDto.setUtilisateurId(createEmpruntDto.getAbonneId());
        empruntRepository.save(modelMapper.map(empruntDto, Emprunt.class));
        return empruntDto;
    }

    //DONE : javadoc

    /**
     * Récupère les emprunts en cours pour une bibliotheque
     * @param bibliothequeId l'identifiant de la bibliotheque
     * @return la liste des emprunts en cours
     */
    @Override
    public List<EmpruntDto> findEmpruntEnCours(Long bibliothequeId) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Date currentDate = new Date();
        List<EmpruntDto> empruntDtoList = new ArrayList<>();
        List<EmpruntDto> empruntDtoByBibliothequeList = new ArrayList<>();
        List<EmpruntDto> empruntDtoEnCoursList = new ArrayList<>();
        List<Emprunt> emprunts = empruntRepository.findAll();
        for (Emprunt emprunt : emprunts) {
            EmpruntDto empruntDto = modelMapper.map(emprunt, EmpruntDto.class);
            empruntDtoList.add(empruntDto);
        }
        for (EmpruntDto empruntDtoByBibliotheque : empruntDtoList) {
            Long ouvrageId = empruntDtoByBibliotheque.getOuvrageId();
            OuvrageDto ouvrageDto = modelMapper.map(ouvrageRepository.getById(ouvrageId), OuvrageDto.class);
            if (ouvrageDto.getBibliothequeId().equals(bibliothequeId)) {
                empruntDtoByBibliothequeList.add(empruntDtoByBibliotheque);
            }
        }
        for (EmpruntDto empruntDtoEnCours : empruntDtoByBibliothequeList) {
            if (!empruntDtoEnCours.isRestitution()) {
                empruntDtoEnCoursList.add(empruntDtoEnCours);
            }
        }
        return empruntDtoEnCoursList;
    }

    /**
     * Récupère la liste des emprunts pour un utilisateur
     * @param utilisateurId l'identifiant d'un utilisateur
     * @return la liste
     */
    @Override
    public List<EmpruntDto> findEmpruntByUtilisateurId(Long utilisateurId) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        List<EmpruntDto>empruntDtos = new ArrayList<>();
        List<Emprunt> empruntsByUtilisateur = empruntRepository.findEmpruntByUtilisateurId(utilisateurId);
        for (Emprunt empruntByUtilisateur :empruntsByUtilisateur) {
            EmpruntDto empruntDtoByUtilisateur = modelMapper.map(empruntByUtilisateur, EmpruntDto.class);
            empruntDtos.add(empruntDtoByUtilisateur);
        }
        return empruntDtos;
    }

    /**
     * Enregistre le retour d'un emprunt par un abonné
     * @param empruntId l'identfiant de l'emprunt
     * @param ouvrageId l'identifiant de l'ouvrage
     */
    @Override
    public void retournerEmprunt(Long empruntId, String ouvrageId) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Emprunt emprunt = new Emprunt();
        Optional<Emprunt> result1 = empruntRepository.findById(empruntId);
        if (result1.isPresent()) {
            emprunt = result1.get();
        }
        emprunt.setDateRestitution(new Date());
        emprunt.setRestitution(true);
        empruntRepository.save(emprunt);
        Ouvrage ouvrage = new Ouvrage();
        Optional<Ouvrage> result2 = ouvrageRepository.findByIdInterne(ouvrageId);
        if (result2.isPresent()) {
            ouvrage = result2.get();
        }
        ouvrage.setEmprunte(false);
        ouvrageRepository.save(ouvrage);
    }

    /**
     * Prolonge la durée d'un emprunt
     * @param empruntId l'identifiant de l'emprunt concerné
     */
    @Override
    public void prolongerEmprunt(Long empruntId) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Emprunt emprunt = new Emprunt();
        Optional<Emprunt> result1 = empruntRepository.findById(empruntId);
        if (result1.isPresent()) {
            emprunt = result1.get();
        }
        Date dateRestitutionPrevue = emprunt.getDateRestitution();
        Calendar c = Calendar.getInstance();
        c.setTime(dateRestitutionPrevue);
        c.add(Calendar.DAY_OF_MONTH, 31);
        Date dateProlongation = c.getTime();
        emprunt.setDateProlongation(dateProlongation);
        emprunt.setProlongation(true);
        empruntRepository.save(emprunt);
    }

    /**
     * Retourne la liste des emprunts expirés
     * @return la liste
     */
    @Override
    public List<EmpruntDto> findExpiredemprunts() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        List<Emprunt> expiredEmprunts = empruntRepository.findExpiredemprunts();
        List<EmpruntDto>expiredempruntsDto = new ArrayList<>();
        for (Emprunt expiredEmprunt : expiredEmprunts) {
            EmpruntDto expiredEmpruntDto = modelMapper.map(expiredEmprunt, EmpruntDto.class);
            expiredempruntsDto.add(expiredEmpruntDto);
        }
        return expiredempruntsDto;
    }

    /**
     * Retourne la liste des emprunts expirés pour un utilisateur
     * @param utilisateurId l'identifiant de l'utilisateur
     * @return la liste
     */
    @Override
    public List<EmpruntDto> findExpiredempruntsByUtilisateurId(Long utilisateurId) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        List<Emprunt> expiredEmprunts = empruntRepository.findExpiredempruntsByUtilisateurId(utilisateurId);
        List<EmpruntDto>expiredempruntsDto = new ArrayList<>();
        for (Emprunt expiredEmprunt : expiredEmprunts) {
            EmpruntDto expiredEmpruntDto = modelMapper.map(expiredEmprunt, EmpruntDto.class);
            expiredempruntsDto.add(expiredEmpruntDto);
        }
        return expiredempruntsDto;
    }

    /**
     * Retourne la liste des utilisateurs en retard pour le retour d'un emprunt
     * @return la liste
     */
    @Override
    public List<Long> findUtilisateurEnRetard() {
        return empruntRepository.findUtilisateurEnRetard();
    }

    /**
     * Récupère un emprunt en cours par l'identifiant de l'ouvrage
     * @param ouvrageId l'identifiant de l'ouvrage emprunté
     * @return l'ouvrage en cours d'emprunt
     */
    @Override
    public EmpruntDto getEmpruntEnCoursByOuvrageId(Long ouvrageId) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Emprunt emprunt = empruntRepository.getEmpruntEnCoursByOuvrageId(ouvrageId);
        EmpruntDto empruntDto = modelMapper.map(emprunt, EmpruntDto.class);
        return empruntDto;
    }
}
