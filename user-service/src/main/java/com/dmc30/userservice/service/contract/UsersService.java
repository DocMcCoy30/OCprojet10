package com.dmc30.userservice.service.contract;

import com.dmc30.userservice.service.dto.UtilisateurDto;
import com.dmc30.userservice.web.exception.TechnicalException;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UsersService extends UserDetailsService {

    UtilisateurDto createAbonne(UtilisateurDto utilisateurDto, Long paysId) throws TechnicalException;

    UtilisateurDto getUtilisateurById(Long utilisateurId);

    UtilisateurDto getUserDetailsByEmail(String email);

    UtilisateurDto getUtilisateurByPublicId(String publicId);

    UtilisateurDto GetUtilisateurByUsername(String username);

    void updateUtilisateur(UtilisateurDto utilisateurDto);

    UtilisateurDto getUtilisateurByNumAbonne(String numAbonne);

    List<UtilisateurDto> getUtilisateursByNumAbonne(String numAbonne);


    List<UtilisateurDto> getAll();
}
