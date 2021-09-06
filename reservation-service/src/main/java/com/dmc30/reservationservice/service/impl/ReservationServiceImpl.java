package com.dmc30.reservationservice.service.impl;

import com.dmc30.reservationservice.data.entity.Reservation;
import com.dmc30.reservationservice.data.repository.ReservationRepository;
import com.dmc30.reservationservice.model.dto.ReservationDto;
import com.dmc30.reservationservice.model.mappers.ReservationMapper;
import com.dmc30.reservationservice.service.contract.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReservationServiceImpl implements ReservationService {

    private ReservationRepository reservationRepository;
    private ReservationMapper reservationMapper;

    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository, ReservationMapper reservationMapper) {
        this.reservationRepository = reservationRepository;
        this.reservationMapper = reservationMapper;
    }

    @Override
    public ReservationDto createReservation(ReservationDto reservationDto) {
        Reservation newReservation = reservationMapper.reservationDtoToReservation(reservationDto);
        Reservation savedReservation = reservationRepository.save(newReservation);
        ReservationDto savedReservationDto = reservationMapper.reservationToReservationDto(savedReservation);
        return savedReservationDto;
    }

    @Override
    public List<ReservationDto> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        List<ReservationDto> reservationDtos =
                reservations.stream()
                        .map(reservation -> reservationMapper.reservationToReservationDto(reservation))
                        .collect(Collectors.toList());
        return reservationDtos;
    }

    @Override
    public ReservationDto getReservationById(Long reservationId) {
        Reservation reservation = reservationRepository.getById(reservationId);
        ReservationDto reservationDto = reservationMapper.reservationToReservationDto(reservation);
        return reservationDto;
    }
}
