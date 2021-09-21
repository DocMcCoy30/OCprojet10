package com.dmc30.empruntservice.service.impl;

import com.dmc30.empruntservice.data.entity.Emprunt;
import com.dmc30.empruntservice.data.entity.Ouvrage;
import com.dmc30.empruntservice.data.repository.EmpruntRepository;
import com.dmc30.empruntservice.data.repository.OuvrageRepository;
import com.dmc30.empruntservice.service.dto.CreateEmpruntDto;
import com.dmc30.empruntservice.service.dto.EmpruntDto;
import com.dmc30.empruntservice.service.dto.OuvrageDto;
import com.dmc30.empruntservice.web.exception.TechnicalException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmpruntServiceImplTest {

    @InjectMocks
    EmpruntServiceImpl empruntService;
    @Mock
    EmpruntRepository empruntRepositoryMock;
    @Mock
    OuvrageRepository ouvrageRepositoryMock;


    @DisplayName("findEmpruntEnCours_ShouldReturnAListOf2EmpruntDtos_If bibliothequeIs1AndExpiredIsFalse")
    @Test
    public void findEmpruntEnCours_Case1() throws TechnicalException {
        //GIVEN
        List<Emprunt> empruntsStub = getListOfEmpruntStub();
        Ouvrage ouvrageStub = getAnOuvrageStub();
        doReturn(empruntsStub).when(empruntRepositoryMock).findAll();
        doReturn(ouvrageStub).when(ouvrageRepositoryMock).getById(anyLong());
        //WHEN
        List<EmpruntDto> actualResult = empruntService.findEmpruntEnCours(1L);
        //THEN
        verify(empruntRepositoryMock, times(1)).findAll();
        verify(ouvrageRepositoryMock, times(2)).getById(anyLong());
        assertThat(actualResult.size()).isEqualTo(2);
        assertThat(actualResult.get(0).getId()).isEqualTo(1);
        assertThat(actualResult.get(1).getId()).isEqualTo(2);
    }

    @DisplayName("findEmpruntEnCours_ShouldReturnAnEmptyList_If bibliothequeIs2AndExpiredIsFalse")
    @Test
    public void findEmpruntEnCours_Case2() throws TechnicalException {
        //GIVEN
        List<Emprunt> empruntsStub = getListOfEmpruntStub();
        Ouvrage ouvrageStub = getAnOuvrageStub();
        doReturn(empruntsStub).when(empruntRepositoryMock).findAll();
        doReturn(ouvrageStub).when(ouvrageRepositoryMock).getById(anyLong());
        //WHEN
        List<EmpruntDto> actualResult = empruntService.findEmpruntEnCours(2L);
        //THEN
        verify(empruntRepositoryMock, times(1)).findAll();
        verify(ouvrageRepositoryMock, times(2)).getById(anyLong());
        assertThat(actualResult.size()).isEqualTo(0);
    }

    @DisplayName("findEmpruntEnCours_ShouldReturnAListOf1Emprunt_If1EmpruntIsExpired")
    @Test
    public void findEmpruntEnCours_Case3() throws TechnicalException {
        //GIVEN
        List<Emprunt> empruntsStub = getListOfEmpruntStub();
        empruntsStub.get(0).setRestitution(true);
        Ouvrage ouvrageStub = getAnOuvrageStub();
        doReturn(empruntsStub).when(empruntRepositoryMock).findAll();
        doReturn(ouvrageStub).when(ouvrageRepositoryMock).getById(anyLong());
        //WHEN
        List<EmpruntDto> actualResult = empruntService.findEmpruntEnCours(1L);
        //THEN
        verify(empruntRepositoryMock, times(1)).findAll();
        verify(ouvrageRepositoryMock, times(2)).getById(anyLong());
        assertThat(actualResult.size()).isEqualTo(1);
    }

    @DisplayName("findEmpruntEnCours_ShouldThrowAnException_WhenResultIsNotFound")
    @Test
    public void findEmpruntEnCours_ExceptionCase() {
        //GIVEN
        doThrow(EntityNotFoundException.class).when(empruntRepositoryMock).findAll();
        //WHEN
        assertThrows(EntityNotFoundException.class,
                () -> empruntRepositoryMock.findAll());
        Throwable exception = assertThrows(TechnicalException.class,
                () -> empruntService.findEmpruntEnCours(1L));
        //THEN
        assertThat(exception.getMessage()).isEqualTo("La ressource demandée est introuvable");
    }

    @DisplayName("findEmpruntByUtilisateurId_ShouldReturnAListOfEmpruntDto")
    @Test
    public void findEmpruntByUtilisateurId() throws TechnicalException {
        //GIVEN
        List<Emprunt> empruntsStub = getListOfEmpruntStub();
        doReturn(empruntsStub).when(empruntRepositoryMock).findEmpruntByUtilisateurId(anyLong());
        //WHEN
        List<EmpruntDto> actualResult = empruntService.findEmpruntByUtilisateurId(anyLong());
        //THEN
        verify(empruntRepositoryMock, times(1)).findEmpruntByUtilisateurId(anyLong());
        assertThat(actualResult.size()).isEqualTo(2);
        assertThat(actualResult.get(0).getId()).isEqualTo(1);
        assertThat(actualResult.get(1).getId()).isEqualTo(2);
    }

    @DisplayName("findEmpruntByUtilisateurId_ShouldThrowAnException_WhenResultIsNotFound")
    @Test
    public void findEmpruntByUtilisateurId_ExceptionCase() {
        //GIVEN
        doThrow(EntityNotFoundException.class).when(empruntRepositoryMock).findEmpruntByUtilisateurId(anyLong());
        //WHEN
        assertThrows(EntityNotFoundException.class,
                () -> empruntRepositoryMock.findEmpruntByUtilisateurId(1L));
        Throwable exception = assertThrows(TechnicalException.class,
                () -> empruntService.findEmpruntByUtilisateurId(1L));
        //THEN
        assertThat(exception.getMessage()).isEqualTo("La ressource demandée est introuvable");
    }

    @DisplayName("retournerEmprunt")
    @Test
    public void retournerEmprunt() throws TechnicalException {
        Ouvrage ouvrageStub = getAnOuvrageStub();
        Emprunt empruntStub = getAnEmpruntStub();

        doReturn(empruntStub).when(empruntRepositoryMock).getById(anyLong());
        doReturn(empruntStub).when(empruntRepositoryMock).save(any(Emprunt.class));
        doReturn(ouvrageStub).when(ouvrageRepositoryMock).findByIdInterne(anyString());
        doReturn(ouvrageStub).when(ouvrageRepositoryMock).save(any(Ouvrage.class));

        empruntService.retournerEmprunt(1l, "TEST");
        verify(empruntRepositoryMock, times(1)).getById(anyLong());
        verify(ouvrageRepositoryMock, times(1)).findByIdInterne(anyString());

    }

    @DisplayName("retournerEmprunt_ShouldThrowAnException_WhenResultIsNotFound")
    @Test
    public void retournerEmprunt_ExceptionCase() {
        //GIVEN
        doThrow(EntityNotFoundException.class).when(empruntRepositoryMock).getById(anyLong());
        //WHEN
        assertThrows(EntityNotFoundException.class,
                () -> empruntRepositoryMock.getById(1L));
        Throwable exception = assertThrows(TechnicalException.class,
                () -> empruntService.retournerEmprunt(1L, "TEST"));
        //THEN
        assertThat(exception.getMessage()).isEqualTo("La ressource demandée est introuvable");
    }

    @DisplayName("prolongerEmprunt")
    @Test
    public void prolongerEmprunt() throws TechnicalException {
        //GIVEN
        Emprunt empruntStub = getAnEmpruntStub();
        //WHEN
        doReturn(empruntStub).when(empruntRepositoryMock).getById(anyLong());
        empruntService.prolongerEmprunt(empruntStub.getId());
        //THEN
        verify(empruntRepositoryMock, times(1)).getById(empruntStub.getId());
        verify(empruntRepositoryMock, times(1)).save(empruntStub);
    }

    @DisplayName("prolongerEmprunt_ShouldThrowAnException_WhenResultIsNotFound")
    @Test
    public void prolongerEmprunt_ExceptionCase() {
        //GIVEN
        doThrow(EntityNotFoundException.class).when(empruntRepositoryMock).getById(anyLong());
        //WHEN
        assertThrows(EntityNotFoundException.class,
                () -> empruntRepositoryMock.getById(1L));
        Throwable exception = assertThrows(TechnicalException.class,
                () -> empruntService.prolongerEmprunt(1L));
        //THEN
        assertThat(exception.getMessage()).isEqualTo("La ressource demandée est introuvable");
    }

    @DisplayName("findExpiredEmprunts_ShouldReturnAListOfEmpruntDto")
    @Test
    public void findExpiredEmprunts() throws TechnicalException {
        //GIVEN
        List<Emprunt> empruntsStub = getListOfEmpruntStub();
        doReturn(empruntsStub).when(empruntRepositoryMock).findExpiredEmprunts();
        //WHEN
        List<EmpruntDto> actualResult = empruntService.findExpiredEmprunts();
        //THEN
        verify(empruntRepositoryMock, times(1)).findExpiredEmprunts();
        assertThat(actualResult.size()).isEqualTo(2);
        assertThat(actualResult.get(0).getId()).isEqualTo(1);
        assertThat(actualResult.get(1).getId()).isEqualTo(2);
    }

    @DisplayName("findExpiredEmprunts_ShouldThrowAnException_WhenResultIsNotFound")
    @Test
    public void findExpiredEmprunts_ExceptionCase(){
        //GIVEN
        doThrow(EntityNotFoundException.class).when(empruntRepositoryMock).findExpiredEmprunts();
        //WHEN
        assertThrows(EntityNotFoundException.class,
                () -> empruntRepositoryMock.findExpiredEmprunts());
        Throwable exception = assertThrows(TechnicalException.class,
                () -> empruntService.findExpiredEmprunts());
        //THEN
        assertThat(exception.getMessage()).isEqualTo("La ressource demandée est introuvable");
    }

    @DisplayName("findExpiredempruntsByUtilisateurId_ShouldReturnAListOfEmpruntDto")
    @Test
    public void findExpiredempruntsByUtilisateurId() throws TechnicalException {
        //GIVEN
        List<Emprunt> empruntsStub = getListOfEmpruntsExpireStub();
        doReturn(empruntsStub).when(empruntRepositoryMock).findExpiredEmpruntsByUtilisateurId(anyLong());
        //WHEN
        List<EmpruntDto> actualResult = empruntService.findExpiredEmpruntsByUtilisateurId(anyLong());
        //THEN
        verify(empruntRepositoryMock, times(1)).findExpiredEmpruntsByUtilisateurId(anyLong());
        assertThat(actualResult.size()).isEqualTo(2);
        assertThat(actualResult.get(0).getId()).isEqualTo(1);
        assertThat(actualResult.get(1).getId()).isEqualTo(2);
        assertThat(actualResult.get(0).getDateRestitution()).isBefore(new Date());
    }

    @DisplayName("findExpiredEmpruntsByUtilisateurId_ShouldThrowAnException_WhenResultIsNotFound")
    @Test
    public void findExpiredEmpruntsByUtilisateurId_ExceptionCase() {
        //GIVEN
        doThrow(EntityNotFoundException.class).when(empruntRepositoryMock)
                .findExpiredEmpruntsByUtilisateurId(anyLong());
        //WHEN
        assertThrows(EntityNotFoundException.class,
                () -> empruntRepositoryMock.findExpiredEmpruntsByUtilisateurId(0L));
        Throwable exception = assertThrows(TechnicalException.class,
                () -> empruntService.findExpiredEmpruntsByUtilisateurId(0L));
        //THEN
        assertThat(exception.getMessage()).isEqualTo("La ressource demandée est introuvable");
    }

    @DisplayName("findUtilisateurEnRetard_ShouldReturnAListOfUserIds")
    @Test
    public void findUtilisateurEnRetard() throws TechnicalException {
        //GIVEN
        Long[] userIdsList = {1L, 2L, 3L};
        List<Long> userIdsListStub = Arrays.asList(userIdsList);
        //WHEN
        doReturn(userIdsListStub).when(empruntRepositoryMock).findUtilisateurEnRetard();
        List<Long> actualResult = empruntService.findUtilisateurEnRetard();
        //THEN
        verify(empruntRepositoryMock, times(1)).findUtilisateurEnRetard();
        assertThat(actualResult.get(0)).isEqualTo(1);
    }

    @DisplayName("findUtilisateurEnRetard_ShouldThrowAnException_WhenResultIsNotFound")
    @Test
    public void findUtilisateurEnRetard_ExceptionCase() {
        //GIVEN
        doThrow(EntityNotFoundException.class).when(empruntRepositoryMock)
                .findUtilisateurEnRetard();
        //WHEN
        assertThrows(EntityNotFoundException.class,
                () -> empruntRepositoryMock.findUtilisateurEnRetard());
        Throwable exception = assertThrows(TechnicalException.class,
                () -> empruntService.findUtilisateurEnRetard());
        //THEN
        assertThat(exception.getMessage()).isEqualTo("La ressource demandée est introuvable");
    }

    @DisplayName("getEmpruntEnCoursByOuvrageId_ShouldReturnAnEmpruntDto")
    @Test
    public void getEmpruntEnCoursByOuvrageId() throws TechnicalException {
        //GIVEN
        Emprunt empruntStub = getAnEmpruntStub();
        doReturn(empruntStub).when(empruntRepositoryMock).getEmpruntEnCoursByOuvrageId(anyLong());
        //WHEN
        EmpruntDto actualResult = empruntService.getEmpruntEnCoursByOuvrageId(10L);
        //THEN
        verify(empruntRepositoryMock, times(1)).getEmpruntEnCoursByOuvrageId(10L);
        assertThat(actualResult.getId()).isEqualTo(100L);
        assertThat(actualResult.getUtilisateurId()).isEqualTo(1L);
    }

    @DisplayName("getEmpruntEnCoursByOuvrageId_ShouldThrowAnException_WhenResultIsNotFound")
    @Test
    public void getEmpruntEnCoursByOuvrageId_ExceptionCase() {
        //GIVEN
        doThrow(EntityNotFoundException.class).when(empruntRepositoryMock)
                .getEmpruntEnCoursByOuvrageId(anyLong());
        //WHEN
        assertThrows(EntityNotFoundException.class,
                () -> empruntRepositoryMock.getEmpruntEnCoursByOuvrageId(anyLong()));
        Throwable exception = assertThrows(TechnicalException.class,
                () -> empruntService.getEmpruntEnCoursByOuvrageId(anyLong()));
        //THEN
        assertThat(exception.getMessage()).isEqualTo("La ressource demandée est introuvable");
    }

    @DisplayName("getEmpruntsRestitues_ShouldReturnAListOfEmpruntDto")
    @Test
    public void getEmpruntsRestitues() throws TechnicalException {
        //GIVEN
        List<Emprunt> empruntsStub = getListOfEmpruntStub();
        doReturn(empruntsStub).when(empruntRepositoryMock).getEmpruntsRestitues();
        //WHEN
        List<EmpruntDto> actualResult = empruntService.getEmpruntsRestitues();
        //THEN
        verify(empruntRepositoryMock, times(1)).getEmpruntsRestitues();
        assertThat(actualResult.size()).isEqualTo(2);
        assertThat(actualResult.get(0).getId()).isEqualTo(1);
        assertThat(actualResult.get(1).getId()).isEqualTo(2);
    }

    @DisplayName("getEmpruntsRestitues_ShouldThrowAnException_WhenResultIsNotFound")
    @Test
    public void getEmpruntsRestitues_ExceptionCase() {
        //GIVEN
        doThrow(EntityNotFoundException.class).when(empruntRepositoryMock)
                .getEmpruntsRestitues();
        //WHEN
        assertThrows(EntityNotFoundException.class,
                () -> empruntRepositoryMock.getEmpruntsRestitues());
        Throwable exception = assertThrows(TechnicalException.class,
                () -> empruntService.getEmpruntsRestitues());
        //THEN
        assertThat(exception.getMessage()).isEqualTo("La ressource demandée est introuvable");
    }

    @DisplayName("createEmprunt_ShouldReturnAnEmpruntDto")
    @Test
    public void createEmprunt() throws TechnicalException {
        //GIVEN
        Ouvrage ouvrageStub = getAnOuvrageStub();
        CreateEmpruntDto createEmpruntDtoStub = getACreateEmpruntDtoStub();
        doReturn(ouvrageStub).when(ouvrageRepositoryMock).getById(anyLong());
        OuvrageDto ouvrageDtoStub = getAnOuvrageDtoStub();
        //WHEN
        EmpruntDto actualResult = empruntService.createEmprunt(createEmpruntDtoStub);
        //THEN
        verify(ouvrageRepositoryMock, times(1)).getById(anyLong());
        verify(ouvrageRepositoryMock, times(1)).save(any(Ouvrage.class));
        verify(empruntRepositoryMock, times(1)).save(any(Emprunt.class));
        assertThat(actualResult.getOuvrageId()).isEqualTo(100L);
        assertThat(actualResult.getUtilisateurId()).isEqualTo(10L);
        assertThat(actualResult.isRestitution()).isFalse();
    }

    @DisplayName("createEmprunt_ShouldThrowAnException_WhenResultIsNotFound")
    @Test
    public void createEmprunt_ExceptionCase() {
        //GIVEN
        CreateEmpruntDto createEmpruntDtoStub = getACreateEmpruntDtoStub();
        doThrow(EntityNotFoundException.class).when(ouvrageRepositoryMock)
                .getById(anyLong());
        //WHEN
        assertThrows(EntityNotFoundException.class,
                () -> ouvrageRepositoryMock.getById(anyLong()));
        Throwable exception = assertThrows(TechnicalException.class,
                () -> empruntService.createEmprunt(createEmpruntDtoStub));
        //THEN
        assertThat(exception.getMessage()).isEqualTo("La ressource demandée est introuvable");
    }

    //----------------- STUBS -----------------------------

    private Emprunt getAnEmpruntStub() {
        return new Emprunt(100L, new Date(), new Date(), new Date()
                , false, false, 10L, 1L);
    }

    private List<Emprunt> getListOfEmpruntStub() {
        List<Emprunt> emprunts = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            Long empruntId = (long) i;
            Long ouvrageId = (long) i + 10;
            Long userId = (long) i;
            emprunts.add((new Emprunt(empruntId, new Date(), new Date(), null,
                    false, false, ouvrageId, userId)));
        }
        return emprunts;
    }

    private List<Emprunt> getListOfEmpruntsExpireStub() {
        List<Emprunt> emprunts = new ArrayList<>();
        // creating a Calendar object
        Calendar c1 = Calendar.getInstance();
        // set Month
        // MONTH starts with 0 i.e. ( 0 - Jan)
        c1.set(Calendar.MONTH, 01);
        // set Date
        c1.set(Calendar.DATE, 01);
        // set Year
        c1.set(Calendar.YEAR, 2021);
        // creating a date object with specified time.
        Date date = c1.getTime();
        for (int i = 1; i <= 2; i++) {
            Long empruntId = (long) i;
            Long ouvrageId = (long) i + 10;
            Long userId = (long) i;
            emprunts.add((new Emprunt(empruntId, date, date, null,
                    false, false, ouvrageId, userId)));
        }
        return emprunts;
    }

    private EmpruntDto getAnEmpruntDtoStub() {
        return new EmpruntDto(100L, new Date(), new Date(), new Date()
                , false, false, 10L, 1L);
    }

    private Ouvrage getAnOuvrageStub() {
        return new Ouvrage(100L, "TEST", false, 1L);
    }

    private OuvrageDto getAnOuvrageDtoStub() {
        return new OuvrageDto(100L, "TEST", false, 1L);
    }

    private CreateEmpruntDto getACreateEmpruntDtoStub() {
        return new CreateEmpruntDto(10L, "TEST", "TEST", "TEST", "TEST", 100L, "TEST", "TEST", "TEST");
    }

}
