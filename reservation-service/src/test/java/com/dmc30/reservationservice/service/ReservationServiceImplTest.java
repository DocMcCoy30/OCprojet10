package com.dmc30.reservationservice.service;


import com.dmc30.reservationservice.data.entity.Reservation;
import com.dmc30.reservationservice.data.repository.ReservationRepository;
import com.dmc30.reservationservice.model.dto.ReservationDto;
import com.dmc30.reservationservice.model.mappers.ReservationMapper;
import com.dmc30.reservationservice.service.impl.ReservationServiceImpl;
import com.dmc30.reservationservice.web.exception.TechnicalException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ReservationServiceImplTest {

    @InjectMocks
    @Spy
    ReservationServiceImpl reservationService;

    @Mock
    ReservationRepository reservationRepositoryMock;

    @Mock
    ReservationMapper reservationMapperMock;

    @Test
    void mapperInitTest() {
        assertThat(reservationService).isNotNull();
    }

    @Test
    @DisplayName("getAllReservations_ShouldReturnAListOfReservationDtos")
    public void getAllReservations() {
        //GIVEN
        List<Reservation> reservationsStub = getListOfReservationsStub();
        ReservationDto reservationDtoStub = getAReservationDtoStub();
        doReturn(reservationsStub).when(reservationRepositoryMock).findAll();
        doReturn(reservationDtoStub).when(reservationMapperMock)
                .reservationToReservationDto(any(Reservation.class));
        //WHEN
        List<ReservationDto> actualReservationDtos = reservationService.getAllReservations();
        //THEN
        verify(reservationRepositoryMock, times(1)).findAll();
        verify(reservationMapperMock, times(4)).reservationToReservationDto(any(Reservation.class));
        assertThat(actualReservationDtos).size().isEqualTo(4);
        assertThat(actualReservationDtos.get(1).getBibliothequeId()).isEqualTo(1);
        assertThat(actualReservationDtos.get(3).getId()).isEqualTo(11);
    }

    @Test()
    @DisplayName("getReservationById_ShouldReturnAReservationDtoByItsId")
    public void getReservationById() throws TechnicalException {
        //GIVEN
        Reservation reservationStub = getAReservationStub();
        ReservationDto reservationDtoStub = getAReservationDtoStub();
        doReturn(reservationStub).when(reservationRepositoryMock)
                .getById(anyLong());
        doReturn(reservationDtoStub).when(reservationMapperMock)
                .reservationToReservationDto(any(Reservation.class));
        //WHEN
        ReservationDto actualReservationDto = reservationService.getReservationById(1l);
        //THEN
        verify(reservationRepositoryMock, times(1)).getById(anyLong());
        verify(reservationMapperMock, times(1)).reservationToReservationDto(any(Reservation.class));
        assertThat(actualReservationDto.getId()).isEqualTo(11);
        assertThat(actualReservationDto.getLivreId()).isEqualTo(111);
    }

    @Test()
    @DisplayName("getReservationById_ShouldThrowATechnicalException_WhenReturnIsNull")
    public void getReservationById_ExceptionCase() {
        //GIVEN
        doThrow(EntityNotFoundException.class)
                .when(reservationRepositoryMock)
                .getById(anyLong());
        //WHEN
        assertThrows(EntityNotFoundException.class,
                () -> reservationRepositoryMock.getById(anyLong()));
        Throwable exception = assertThrows(TechnicalException.class,
                () -> reservationService.getReservationById(anyLong()));
        //THEN
        assertThat(exception.getMessage()).isEqualTo("La ressource demandée est introuvable");
    }

    @Test()
    @DisplayName("getReservationByUserId_ShouldReturnAListOfReservationDtoByTheirUserId")
    public void getReservationsByUserId() throws TechnicalException {
        //GIVEN
        List<Reservation> reservationsStub = getListOfReservationsStub();
        ReservationDto reservationDtoStub = getAReservationDtoStub();
        doReturn(reservationsStub).when(reservationRepositoryMock)
                .getReservationsByUtilisateurIdOrderByDateReservation(anyLong());
        doReturn(reservationDtoStub).when(reservationMapperMock)
                .reservationToReservationDto(any(Reservation.class));
        //WHEN
        List<ReservationDto> actualReservationDtos = reservationService.getReservationsByUserId(anyLong());
        //THEN
        verify(reservationRepositoryMock, times(1))
                .getReservationsByUtilisateurIdOrderByDateReservation(anyLong());
        verify(reservationMapperMock, times(4)).reservationToReservationDto(any(Reservation.class));
        assertThat(actualReservationDtos).size().isEqualTo(4);
        assertThat(actualReservationDtos.get(1).getBibliothequeId()).isEqualTo(1);
        assertThat(actualReservationDtos.get(3).getId()).isEqualTo(11);
    }

    @Test()
    @DisplayName("getReservationsByUserId_ShouldThrowATechnicalException_WhenReturnIsNull")
    public void getReservationsByUserId_ExceptionCase() {
        //GIVEN
        doThrow(EntityNotFoundException.class)
                .when(reservationRepositoryMock)
                .getReservationsByUtilisateurIdOrderByDateReservation(anyLong());
        //WHEN
        assertThrows(EntityNotFoundException.class,
                () -> reservationRepositoryMock.getReservationsByUtilisateurIdOrderByDateReservation(anyLong()));
        Throwable exception = assertThrows(TechnicalException.class,
                () -> reservationService.getReservationsByUserId(anyLong()));
        //THEN
        assertThat(exception.getMessage()).isEqualTo("La ressource demandée est introuvable");
    }

    @Test
    @DisplayName("getNombreDeReservation_ShouldReturnAnInteger")
    public void getNombreDeReservation() throws TechnicalException {
        //GIVEN
        int nbReservationStub = 10;
        doReturn(nbReservationStub).when(reservationRepositoryMock)
                .getNombreDeReservationByLivreIdAndBibliothequeId(anyLong(), anyLong());
        //WHEN
        int actualNbReservation = reservationService.getNombreDeReservation(anyLong(), anyLong());
        //THEN
        verify(reservationRepositoryMock, times(1)).getNombreDeReservationByLivreIdAndBibliothequeId(anyLong(), anyLong());
        assertThat(actualNbReservation).isEqualTo(10);
    }

    @Test
    @DisplayName("getNombreDeReservation_ShouldThrowATechnicalException_WhenReturnIsNull")
    public void getNombreDeReservation_ExceptionCase() {
        //GIVEN
        doThrow(EntityNotFoundException.class).when(reservationRepositoryMock)
                .getNombreDeReservationByLivreIdAndBibliothequeId(anyLong(), anyLong());
        //WHEN
        assertThrows(EntityNotFoundException.class,
                () -> reservationRepositoryMock.getNombreDeReservationByLivreIdAndBibliothequeId(anyLong(), anyLong()));
        Throwable exception = assertThrows(TechnicalException.class,
                () -> reservationService.getNombreDeReservation(anyLong(), anyLong()));
        //THEN
        assertThat(exception.getMessage()).isEqualTo("La ressource demandée est introuvable");
    }

    @Test
    @DisplayName("getReservationByLivreIdAndAndBibliothequeIdOrderByDateReservation_ShouldReturnAListOfReservationDto")
    public void getReservationByLivreIdAndAndBibliothequeIdOrderByDateReservation() throws TechnicalException {
        //GIVEN
        List<Reservation> reservationsStub = getListOfReservationsStub();
        ReservationDto reservationDtoStub = getAReservationDtoStub();
        doReturn(reservationsStub).when(reservationRepositoryMock)
                .getReservationByLivreIdAndAndBibliothequeIdOrderByDateReservation(anyLong(), anyLong());
        doReturn(reservationDtoStub).when(reservationMapperMock)
                .reservationToReservationDto(any(Reservation.class));
        //WHEN
        List<ReservationDto> actualReservationDtos = reservationService
                .getReservationByLivreIdAndAndBibliothequeIdOrderByDateReservation(anyLong(), anyLong());
        //THEN
        verify(reservationMapperMock, atLeast(2)).reservationToReservationDto(any(Reservation.class));
        verify(reservationRepositoryMock, times(1))
                .getReservationByLivreIdAndAndBibliothequeIdOrderByDateReservation(anyLong(), anyLong());
        assertThat(actualReservationDtos).size().isEqualTo(4);
        assertThat(actualReservationDtos.get(1).getBibliothequeId()).isEqualTo(1);
        assertThat(actualReservationDtos.get(3).getId()).isEqualTo(11);
    }

    @Test
    @DisplayName("getReservationByLivreIdAndAndBibliothequeIdOrderByDateReservation_ShouldThrowATechnicalException_WhenReturnIsNull")
    public void getReservationByLivreIdAndAndBibliothequeIdOrderByDateReservation_ExceptionCase() throws TechnicalException {
        //GIVEN
        doThrow(EntityNotFoundException.class)
                .when(reservationRepositoryMock)
                .getReservationByLivreIdAndAndBibliothequeIdOrderByDateReservation(anyLong(), anyLong());
        //WHEN
        assertThrows(EntityNotFoundException.class,
                () -> reservationRepositoryMock.getReservationByLivreIdAndAndBibliothequeIdOrderByDateReservation(anyLong(), anyLong()));
        Throwable exception = assertThrows(TechnicalException.class,
                () -> reservationService.getReservationByLivreIdAndAndBibliothequeIdOrderByDateReservation(anyLong(), anyLong()));
        //THEN
        assertThat(exception.getMessage()).isEqualTo("La ressource demandée est introuvable");
    }

    @Test
    @DisplayName("getReservationByLivreIdOrderByDateReservation_ShouldReturnAListOfReservationDto")
    public void getReservationByLivreIdOrderByDateReservation() throws TechnicalException {
        //GIVEN
        List<Reservation> reservationsStub = getListOfReservationsStub();
        ReservationDto reservationDtoStub = getAReservationDtoStub();
        doReturn(reservationsStub).when(reservationRepositoryMock)
                .getReservationsByLivreIdOrderByDateReservation(anyLong());
        doReturn(reservationDtoStub).when(reservationMapperMock)
                .reservationToReservationDto(any(Reservation.class));
        //WHEN
        List<ReservationDto> actualReservationDtos = reservationService
                .getReservationsByLivreIdOrderByDateReservation(anyLong());
        //THEN
        verify(reservationMapperMock, atLeast(2)).reservationToReservationDto(any(Reservation.class));
        verify(reservationRepositoryMock, times(1))
                .getReservationsByLivreIdOrderByDateReservation(anyLong());
        assertThat(actualReservationDtos).size().isEqualTo(4);
        assertThat(actualReservationDtos.get(1).getBibliothequeId()).isEqualTo(1);
        assertThat(actualReservationDtos.get(3).getId()).isEqualTo(11);
    }

    @Test
    @DisplayName("getReservationByLivreIdOrderByDateReservation_ShouldThrowATechnicalException_WhenReturnIsNull")
    public void getReservationByLivreIdOrderByDateReservation_ExceptionCase() {
        //GIVEN
        doThrow(EntityNotFoundException.class)
                .when(reservationRepositoryMock)
                .getReservationsByLivreIdOrderByDateReservation(anyLong());
        //WHEN
        assertThrows(EntityNotFoundException.class,
                () -> reservationRepositoryMock.getReservationsByLivreIdOrderByDateReservation(anyLong()));
        Throwable exception = assertThrows(TechnicalException.class,
                () -> reservationService.getReservationsByLivreIdOrderByDateReservation(anyLong()));
        //THEN
        assertThat(exception.getMessage()).isEqualTo("La ressource demandée est introuvable");
    }

    @Test
    @DisplayName("createReservation_ShouldReturnAReservationDto")
    public void createReservation() throws TechnicalException {
        //GIVEN
        Reservation reservationStub = getAReservationStub();
        ReservationDto reservationDtoStub = getAReservationDtoStub();
        doReturn(reservationStub).when(reservationMapperMock).reservationDtoToReservation(reservationDtoStub);
        doReturn(reservationStub).when(reservationRepositoryMock).save(reservationStub);
        doReturn(reservationDtoStub).when(reservationMapperMock).reservationToReservationDto(reservationStub);
        //WHEN
        ReservationDto actualReservation = reservationService.createReservation(reservationDtoStub);
        //THEN
        verify(reservationMapperMock, times(1)).reservationDtoToReservation(any(ReservationDto.class));
        verify(reservationMapperMock, times(1)).reservationToReservationDto(any(Reservation.class));
        verify(reservationRepositoryMock, times(1)).save(any(Reservation.class));
        assertThat(actualReservation.getId()).isEqualTo(11);
        assertThat(actualReservation.getLivreId()).isEqualTo(111);
    }

    @Test
    @DisplayName("updateReservation_ShouldUpdateAReservation")
    public void updateReservation() {
        //GIVEN
        ReservationDto reservationDtoStub = getAReservationDtoStub();
        Reservation reservationStub = getAReservationStub();
        doReturn(reservationStub).when(reservationMapperMock).reservationDtoToReservation(any(ReservationDto.class));
        //WHEN
        reservationService.updateReservation(reservationDtoStub);
        //THEN
        verify(reservationMapperMock, times(1)).reservationDtoToReservation(any(ReservationDto.class));
        verify(reservationRepositoryMock, times(1)).save(any(Reservation.class));
    }

    @Test
    @DisplayName("deleteReservation_shouldDeleteAReservation")
    public void deleteReservation() {
        //GIVEN
        //WHEN
        reservationService.deleteReservation(anyLong());
        //THEN
        verify(reservationRepositoryMock, times(1)).deleteById(anyLong());
        }


    //------------------- STUBS -------------------------

    private Reservation getAReservationStub() {
        return new Reservation(22L, new Date(),
                ZonedDateTime.now(), false, false,
                new Date(), ZonedDateTime.now(),
                101L, 102L, 4L);
    }

    private List<Reservation> getListOfReservationsStub() {
        List<Reservation> reservations = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            Long reservationId = (long) i;
            Long userId = (long) i + 5;
            Long livreId = (long) i + 10;
            Long bibliothequeId = (long) i;
            reservations.add(new Reservation(reservationId, new Date(),
                    ZonedDateTime.now(), false, false,
                    new Date(), ZonedDateTime.now(),
                    userId, livreId, bibliothequeId));
        }
        return reservations;
    }

    private ReservationDto getAReservationDtoStub() {
        return new ReservationDto(11L, new Date(),
                ZonedDateTime.now(), false, false,
                new Date(), ZonedDateTime.now(),
                1L, 111L, 1L);
    }

    private List<ReservationDto> getListOfReservationDtostub() {
        List<ReservationDto> reservationDtos = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            Long reservationId = (long) i;
            Long userId = (long) i + 5;
            Long livreId = (long) i + 10;
            Long bibliothequeId = (long) i;
            reservationDtos.add(new ReservationDto(reservationId, new Date(),
                    ZonedDateTime.now(), false, false,
                    new Date(), ZonedDateTime.now(),
                    userId, livreId, bibliothequeId));
        }
        return reservationDtos;
    }
}
