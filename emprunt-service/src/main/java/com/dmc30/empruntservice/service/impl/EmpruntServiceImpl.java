package com.dmc30.empruntservice.service.impl;

import com.dmc30.empruntservice.data.entity.Ouvrage;
import com.dmc30.empruntservice.data.entity.Pret;
import com.dmc30.empruntservice.data.repository.OuvrageRepository;
import com.dmc30.empruntservice.data.repository.PretRepository;
import com.dmc30.empruntservice.service.dto.CreateEmpruntDto;
import com.dmc30.empruntservice.service.dto.OuvrageDto;
import com.dmc30.empruntservice.service.dto.PretDto;
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
    PretRepository pretRepository;

    @Autowired
    public EmpruntServiceImpl(OuvrageRepository ouvrageRepository, PretRepository pretRepository) {
        this.ouvrageRepository = ouvrageRepository;
        this.pretRepository = pretRepository;
    }

    /**
     * Crée un nouvel emprunt dans la base de données
     *
     * @param createEmpruntDto les paramètres de l'emprunt
     */
    @Override
    public PretDto createEmprunt(CreateEmpruntDto createEmpruntDto) {
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
        PretDto pretDto = new PretDto();
        pretDto.setDateEmprunt(dateEmprunt);
        pretDto.setDateRestitution(dateRestitution);
        pretDto.setProlongation(false);
        pretDto.setRestitution(false);
        pretDto.setOuvrageId(ouvrage.getId());
        pretDto.setUtilisateurId(createEmpruntDto.getAbonneId());
        pretRepository.save(modelMapper.map(pretDto, Pret.class));
        return pretDto;
    }

    //DONE : javadoc

    /**
     * Récupère les emprunts en cours pour une bibliotheque
     * @param bibliothequeId l'identifiant de la bibliotheque
     * @return la liste des emprunts en cours
     */
    @Override
    public List<PretDto> findEmpruntEnCours(Long bibliothequeId) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Date currentDate = new Date();
        List<PretDto> pretDtoList = new ArrayList<>();
        List<PretDto> pretDtoByBibliothequeList = new ArrayList<>();
        List<PretDto> pretDtoEnCoursList = new ArrayList<>();
        List<Pret> prets = pretRepository.findAll();
        for (Pret pret : prets) {
            PretDto pretDto = modelMapper.map(pret, PretDto.class);
            pretDtoList.add(pretDto);
        }
        for (PretDto pretDtoByBibliotheque : pretDtoList) {
            Long ouvrageId = pretDtoByBibliotheque.getOuvrageId();
            OuvrageDto ouvrageDto = modelMapper.map(ouvrageRepository.getById(ouvrageId), OuvrageDto.class);
            if (ouvrageDto.getBibliothequeId().equals(bibliothequeId)) {
                pretDtoByBibliothequeList.add(pretDtoByBibliotheque);
            }
        }
        for (PretDto pretDtoEnCours : pretDtoByBibliothequeList) {
            if (!pretDtoEnCours.isRestitution()) {
                pretDtoEnCoursList.add(pretDtoEnCours);
            }
        }
        return pretDtoEnCoursList;
    }

    /**
     * Récupère la liste des emprunts pour un utilisateur
     * @param utilisateurId l'identifiant d'un utilisateur
     * @return la liste
     */
    @Override
    public List<PretDto> findEmpruntByUtilisateurId(Long utilisateurId) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        List<PretDto>empruntsByUtilisateur = new ArrayList<>();
        List<Pret> pretsByUtilisateur = pretRepository.findEmpruntByUtilisateurId(utilisateurId);
        for (Pret pretByUtilisateur:pretsByUtilisateur) {
            PretDto pretDtoByUtilisateur = modelMapper.map(pretByUtilisateur, PretDto.class);
            empruntsByUtilisateur.add(pretDtoByUtilisateur);
        }
        return empruntsByUtilisateur;
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
        Pret pret = new Pret();
        Optional<Pret> result1 = pretRepository.findById(empruntId);
        if (result1.isPresent()) {
            pret = result1.get();
        }
        pret.setDateRestitution(new Date());
        pret.setRestitution(true);
        pretRepository.save(pret);
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
        Pret pret = new Pret();
        Optional<Pret> result1 = pretRepository.findById(empruntId);
        if (result1.isPresent()) {
            pret = result1.get();
        }
        Date dateRestitutionPrevue = pret.getDateRestitution();
        Calendar c = Calendar.getInstance();
        c.setTime(dateRestitutionPrevue);
        c.add(Calendar.DAY_OF_MONTH, 31);
        Date dateProlongation = c.getTime();
        pret.setDateProlongation(dateProlongation);
        pret.setProlongation(true);
        pretRepository.save(pret);
    }

    /**
     * Retourne la liste des emprunts expirés
     * @return la liste
     */
    @Override
    public List<PretDto> findExpiredPrets() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        List<Pret> expiredPrets = pretRepository.findExpiredPrets();
        List<PretDto>expiredPretsDto = new ArrayList<>();
        for (Pret expiredPret:expiredPrets) {
            PretDto expiredPretDto = modelMapper.map(expiredPret, PretDto.class);
            expiredPretsDto.add(expiredPretDto);
        }
        return expiredPretsDto;
    }

    /**
     * Retourne la liste des emprunts expirés pour un utilisateur
     * @param utilisateurId l'identifiant de l'utilisateur
     * @return la liste
     */
    @Override
    public List<PretDto> findExpiredPretsByUtilisateurId(Long utilisateurId) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        List<Pret> expiredPrets = pretRepository.findExpiredPretsByUtilisateurId(utilisateurId);
        List<PretDto>expiredPretsDto = new ArrayList<>();
        for (Pret expiredPret:expiredPrets) {
            PretDto expiredPretDto = modelMapper.map(expiredPret, PretDto.class);
            expiredPretsDto.add(expiredPretDto);
        }
        return expiredPretsDto;
    }

    /**
     * Retourne la liste des utilisateurs en retard pour le retour d'un emprunt
     * @return la liste
     */
    @Override
    public List<Long> findUtilisateurEnRetard() {
        return pretRepository.findUtilisateurEnRetard();
    }

    /**
     * Récupère un emprunt en cours par l'identifiant de l'ouvrage
     * @param ouvrageId l'identifiant de l'ouvrage emprunté
     * @return l'ouvrage en cours d'emprunt
     */
    @Override
    public PretDto getEmpruntEnCoursByOuvrageId(Long ouvrageId) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Pret pret = pretRepository.getEmpruntEnCoursByOuvrageId(ouvrageId);
        PretDto pretDto = modelMapper.map(pret, PretDto.class);
        return pretDto;
    }
}
