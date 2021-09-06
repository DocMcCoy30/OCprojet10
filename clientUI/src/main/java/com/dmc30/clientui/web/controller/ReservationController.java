package com.dmc30.clientui.web.controller;

import com.dmc30.clientui.service.contract.ReservationService;
import com.dmc30.clientui.shared.bean.reservation.ReservationBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ReservationController {

    Logger logger = LogManager.getLogger(ReservationController.class);

    ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/createReservation")
    public ModelAndView createReservation(@RequestParam(value = "livreId") Long livreId,
                                          @RequestParam(value = "username") String username) {
        ModelAndView theModel = new ModelAndView("test");
        ReservationBean newReservation = reservationService.createReservation(livreId, username);
        logger.debug("Into createReservation");
        return theModel;
    }
}
