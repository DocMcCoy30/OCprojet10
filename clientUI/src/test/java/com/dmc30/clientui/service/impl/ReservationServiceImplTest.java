package com.dmc30.clientui.service.impl;

import com.dmc30.clientui.proxy.LivreServiceProxy;
import com.dmc30.clientui.proxy.ReservationServiceProxy;
import com.dmc30.clientui.service.contract.EmpruntService;
import com.dmc30.clientui.service.contract.OuvrageService;
import com.dmc30.clientui.service.contract.UserService;
import com.dmc30.clientui.shared.bean.bibliotheque.EmpruntBean;
import com.dmc30.clientui.shared.bean.commun.AdresseBean;
import com.dmc30.clientui.shared.bean.reservation.ReservationBean;
import com.dmc30.clientui.shared.bean.utilisateur.RoleBean;
import com.dmc30.clientui.shared.bean.utilisateur.UtilisateurBean;
import com.dmc30.clientui.web.exception.TechnicalException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

//DONE: T3 tests unitaires ReservationService : règles métier dans clientUI

@ExtendWith(MockitoExtension.class)
public class ReservationServiceImplTest {

    @InjectMocks
    @Spy
    ReservationServiceImpl reservationService;
    @Mock
    UserService userService;
    @Mock
    EmpruntService empruntService;
    @Mock
    LivreServiceProxy livreServiceProxy;
    @Mock
    ReservationServiceProxy reservationServiceProxy;
    @Mock
    OuvrageService ouvrageService;


    @Tag("reservationPossibleCheck1")
    @DisplayName("reservationPossibleCheck1_ShouldReturnTrue_IfLivreIsNotEmprunte")
    @Test
    public void reservationPossibleCheck1_CaseTrue1() throws TechnicalException {
        //GIVEN
        UtilisateurBean userBeanStub = getAUserStub();
        List<EmpruntBean> empruntBeansStub = getListOfEmpruntBeanStub();
        doReturn(userBeanStub).when(userService).getUtilisateurByUsername(anyString());
        doReturn(empruntBeansStub).when(empruntService).getEmpruntByUtilisateurId(anyLong());
        doReturn(10L).when(livreServiceProxy).getLivreIdByOuvrageId(anyLong());
        //WHEN
        boolean actualResult = reservationService.reservationPossibleCheck1(anyLong(), anyString());
        //THEN
        verify(userService, times(1)).getUtilisateurByUsername(anyString());
        verify(empruntService, times(1)).getEmpruntByUtilisateurId(anyLong());
        verify(livreServiceProxy, times(2)).getLivreIdByOuvrageId(anyLong());
        assertThat(actualResult).isTrue();
    }

    @Tag("reservationPossibleCheck1")
    @DisplayName("reservationPossibleCheck1_ShouldReturnTrue_IfListOfLivreEmprunteIsEmpty")
    @Test
    public void reservationPossibleCheck1_CaseTrue2() throws TechnicalException {
        //GIVEN
        UtilisateurBean userBeanStub = getAUserStub();
        List<EmpruntBean> empruntBeansStub = new ArrayList<>();
        doReturn(userBeanStub).when(userService).getUtilisateurByUsername(anyString());
        doReturn(empruntBeansStub).when(empruntService).getEmpruntByUtilisateurId(anyLong());
        //WHEN
        boolean actualResult = reservationService.reservationPossibleCheck1(anyLong(), anyString());
        //THEN
        assertThat(actualResult).isTrue();
    }

    @Tag("reservationPossibleCheck1")
    @DisplayName("reservationPossibleCheck1_ShouldReturnFalse_IfLivreIsEmprunte")
    @Test
    public void reservationPossibleCheck1_CaseFalse() throws TechnicalException {
        //GIVEN
        UtilisateurBean userBeanStub = getAUserStub();
        List<EmpruntBean> empruntBeansStub = getListOfEmpruntBeanStub();
        doReturn(userBeanStub).when(userService).getUtilisateurByUsername(anyString());
        doReturn(empruntBeansStub).when(empruntService).getEmpruntByUtilisateurId(anyLong());
        doReturn(10L).when(livreServiceProxy).getLivreIdByOuvrageId(anyLong());
        //WHEN
        boolean actualResult = reservationService.reservationPossibleCheck1(10L, "TEST");
        //THEN
        verify(userService, times(1)).getUtilisateurByUsername(anyString());
        verify(empruntService, times(1)).getEmpruntByUtilisateurId(anyLong());
        verify(livreServiceProxy, times(1)).getLivreIdByOuvrageId(anyLong());
        assertThat(actualResult).isFalse();
    }

    @Tag("reservationPossibleCheck2")
    @DisplayName("reservationPossibleCheck2_ShouldReturnTrue_IfThereIsNoReservationForThisLivre")
    @Test
    public void reservationPossibleCheck2_CaseTrue() {
        //GIVEN
        UtilisateurBean userBeanStub = getAUserStub();
        List<ReservationBean> reservationBeansStub = getListOfReservationsStub();
        doReturn(userBeanStub).when(userService).getUtilisateurByUsername(anyString());
        doReturn(reservationBeansStub).when(reservationServiceProxy).getReservationsByUserId(anyLong());
        //WHEN
        boolean actualResult = reservationService.reservationPossibleCheck2(anyLong(), anyString());
        //THEN
        verify(userService, times(1)).getUtilisateurByUsername(anyString());
        verify(reservationServiceProxy, times(1)).getReservationsByUserId(anyLong());
        assertThat(actualResult).isTrue();
    }

    @Tag("reservationPossibleCheck2")
    @DisplayName("reservationPossibleCheck2_ShouldReturnFalse_IfThereIsAReservationForThisLivre")
    @Test
    public void reservationPossibleCheck2_CaseFalse() {
        //GIVEN
        UtilisateurBean userBeanStub = getAUserStub();
        List<ReservationBean> reservationBeansStub = getListOfReservationsStub();
        reservationBeansStub.get(0).setExpiree(false);
        doReturn(userBeanStub).when(userService).getUtilisateurByUsername(anyString());
        doReturn(reservationBeansStub).when(reservationServiceProxy).getReservationsByUserId(anyLong());
        //WHEN
        boolean actualResult = reservationService.reservationPossibleCheck2(11L, "TEST");
        //THEN
        verify(userService, times(1)).getUtilisateurByUsername(anyString());
        verify(reservationServiceProxy, times(1)).getReservationsByUserId(anyLong());
        assertThat(actualResult).isFalse();
    }

    @Tag("reservationPossibleCheck3")
    @DisplayName("reservationPossibleCheck3_ShouldReturnTrue_IfListeDAttenteIsNotFull")
    @Test
    public void reservationPossibleCheck3_CaseTrue() {
        //GIVEN
        int nbReservation = 2;
        int nbOuvrage = 2;
        doReturn(nbReservation).when(reservationServiceProxy).getNombreDeReservation(anyLong(), anyLong());
        doReturn(nbOuvrage).when(ouvrageService).getNombreDOuvrage(anyLong(), anyLong());
        //WHEN
        boolean actualResult = reservationService.reservationPossibleCheck3(anyLong(), anyLong());
        //THEN
        verify(reservationServiceProxy, times(1)).getNombreDeReservation(anyLong(), anyLong());
        verify(ouvrageService, times(1)).getNombreDOuvrage(anyLong(), anyLong());
        assertThat(actualResult).isTrue();
    }

    @Tag("reservationPossibleCheck3")
    @DisplayName("reservationPossibleCheck3_ShouldReturnFalse_IfListeDAttenteIsFull")
    @Test
    public void reservationPossibleCheck3_CaseFalse() {
        //GIVEN
        int nbReservation = 4;
        int nbOuvrage = 2;
        doReturn(nbReservation).when(reservationServiceProxy).getNombreDeReservation(anyLong(), anyLong());
        doReturn(nbOuvrage).when(ouvrageService).getNombreDOuvrage(anyLong(), anyLong());
        //WHEN
        boolean actualResult = reservationService.reservationPossibleCheck3(anyLong(), anyLong());
        //THEN
        verify(reservationServiceProxy, times(1)).getNombreDeReservation(anyLong(), anyLong());
        verify(ouvrageService, times(1)).getNombreDOuvrage(anyLong(), anyLong());
        assertThat(actualResult).isFalse();
    }

    @Tag("globalReservationPossibleCheck")
    @DisplayName("globalReservationPossibleCheck_ShouldReturnTrue_IfAllRGAreRespected")
    @Test
    public void globalReservationPossibleCheck_CaseTrue() throws TechnicalException {
        //GIVEN
        doReturn(true).when(reservationService).reservationPossibleCheck1(anyLong(), anyString());
        doReturn(true).when(reservationService).reservationPossibleCheck2(anyLong(), anyString());
        doReturn(true).when(reservationService).reservationPossibleCheck3(anyLong(), anyLong());
        //WHEN
        boolean actualResult = reservationService.globalReservationPossibleCheck(anyLong(), anyString(), anyLong());
        //THEN
        assertThat(actualResult).isTrue();
    }

    @Tag("globalReservationPossibleCheck")
    @DisplayName("globalReservationPossibleCheck_ShouldThrowAnException_IfRG1IsNotRespected")
    @Test
    public void globalReservationPossibleCheck_CaseFalse1() throws TechnicalException {
        //GIVEN
        doReturn(false).when(reservationService).reservationPossibleCheck1(anyLong(), anyString());
        //WHEN
        Throwable exception = assertThrows(TechnicalException.class,
                () -> reservationService.globalReservationPossibleCheck(anyLong(), anyString(), anyLong()));
        //THEN
        assertThat(exception.getMessage()).isEqualTo("Réservation impossible : vous avez un emprunt en cours pour ce livre.");
    }

    @Tag("globalReservationPossibleCheck")
    @DisplayName("globalReservationPossibleCheck_ShouldThrowAnException_IfRG2IsNotRespected")
    @Test
    public void globalReservationPossibleCheck_CaseFalse2() throws TechnicalException {
        //GIVEN
        doReturn(true).when(reservationService).reservationPossibleCheck1(anyLong(), anyString());
        doReturn(false).when(reservationService).reservationPossibleCheck2(anyLong(), anyString());
        //WHEN
        Throwable exception = assertThrows(TechnicalException.class,
                () -> reservationService.globalReservationPossibleCheck(anyLong(), anyString(), anyLong()));
        //THEN
        assertThat(exception.getMessage()).isEqualTo("Une réservation est déjà enregistrée pour ce livre.");
    }

    @Tag("globalReservationPossibleCheck")
    @DisplayName("globalReservationPossibleCheck_ShouldThrowAnException_IfRG3IsNotRespected")
    @Test
    public void globalReservationPossibleCheck_CaseFalse3() throws TechnicalException {
        //GIVEN
        doReturn(true).when(reservationService).reservationPossibleCheck1(anyLong(), anyString());
        doReturn(true).when(reservationService).reservationPossibleCheck2(anyLong(), anyString());
        doReturn(false).when(reservationService).reservationPossibleCheck3(anyLong(), anyLong());
        //WHEN
        Throwable exception = assertThrows(TechnicalException.class,
                () -> reservationService.globalReservationPossibleCheck(anyLong(), anyString(), anyLong()));
        //THEN
        assertThat(exception.getMessage()).isEqualTo("Réservation impossible : la liste d'attente est pleine.");
    }

    //------------- STUBS ----------------------

    private EmpruntBean getAnEmpruntBeanStub() {
        return new EmpruntBean(100L, new Date(), new Date(), new Date()
                , false, false, 10L, 1L);
    }

    private List<EmpruntBean> getListOfEmpruntBeanStub() {
        List<EmpruntBean> emprunts = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            Long empruntId = (long) i;
            Long ouvrageId = (long) i + 10;
            Long userId = (long) i;
            emprunts.add((new EmpruntBean(empruntId, new Date(), new Date(), null,
                    false, false, ouvrageId, userId)));
        }
        return emprunts;
    }

    private UtilisateurBean getAUserStub() {
        return new UtilisateurBean(0L, "TEST", "TEST", "TEST", "TEST", "TEST", "TEST", "TEST"
                , new ArrayList<RoleBean>(), "TEST", new AdresseBean(), "TEST", new Date(), "TEST",
                "TEST", new Date(), new Date());
    }

    private List<ReservationBean> getListOfReservationsStub() {
        List<ReservationBean> reservations = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            Long reservationId = (long) i;
            Long userId = (long) i + 5;
            Long livreId = (long) i + 10;
            Long bibliothequeId = (long) i;
            reservations.add(new ReservationBean(reservationId, new Date(),
                    ZonedDateTime.now(), false, userId, livreId, bibliothequeId));
        }
        return reservations;
    }


}
