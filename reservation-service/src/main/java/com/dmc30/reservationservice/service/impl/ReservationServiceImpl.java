package com.dmc30.reservationservice.service.impl;

import com.dmc30.reservationservice.data.repository.ReservationRepository;
import com.dmc30.reservationservice.model.dto.ReservationDto;
import com.dmc30.reservationservice.model.mappers.ReservationMapper;
import com.dmc30.reservationservice.service.contract.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

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
        return null;
    }

    @Override
    public List<ReservationDto> getAllReservations() {
        return null;
    }

    @Override
    public ReservationDto getReservationById(Long resrvationId) {
        return null;
    }
}
