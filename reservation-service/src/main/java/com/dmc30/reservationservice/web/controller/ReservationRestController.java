package com.dmc30.reservationservice.web.controller;

import com.dmc30.reservationservice.model.dto.ReservationDto;
import com.dmc30.reservationservice.service.contract.ReservationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationRestController {

    Logger logger = LogManager.getLogger(ReservationRestController.class);

    ReservationService reservationService;

    @Autowired
    public ReservationRestController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    //TODO : javadoc

    @GetMapping("/")
    public List<ReservationDto> getAllReservations() {
        return reservationService.getAllReservations();
    }

    @GetMapping(path = "/{reservationId}")
    public ReservationDto getReservationById(@PathVariable(name = "reservationId") Long reservationId) {
        return reservationService.getReservationById(reservationId);
    }

    @PostMapping("/")
    public ReservationDto createReservation(@RequestBody ReservationDto reservationDto) {
        return reservationService.createReservation(reservationDto);
    }

    @GetMapping("/user/{userId}")
    public List<ReservationDto> getReservationsByUserId(@PathVariable(name = "userId")Long userId) {
        return reservationService.getReservationsByUserId(userId);
    }

    @GetMapping("/nbResa/{livreId}&{bibliothequeId}")
    Integer getNombreDeReservation(@PathVariable(name = "livreId")Long livreId,
                                   @PathVariable(name = "bibliothequeId") Long bibliothequeId) {
        Integer nbReservation = reservationService.getNombreDeReservation(livreId, bibliothequeId);
        return nbReservation;
    }
}
