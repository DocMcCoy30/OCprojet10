package com.dmc30.clientui.proxy;

import com.dmc30.clientui.shared.bean.reservation.ReservationBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "reservation-service", url = "localhost:9004")
public interface ReservationServiceProxy {

    @PostMapping("/reservations/")
    ReservationBean createReservation(ReservationBean reservationBean);

    @GetMapping("/reservations/user/{userId}")
    List<ReservationBean> getReservationsByUserId(@PathVariable(name = "userId") Long userId);

    @GetMapping("/reservations/nbResa/{livreId}&{bibliothequeId}")
    Integer getNombreDeReservation(@PathVariable(name = "livreId")Long livreId,
                               @PathVariable(name = "bibliothequeId") Long bibliothequeId);

    @GetMapping("/reservations/listeAttente/{livreId}&{bibliothequeId}")
    List<ReservationBean> getReservationByLivreIdAndAndBibliothequeIdOrderByDateReservation(@PathVariable(name = "livreId") Long livreId,
                                                                      @PathVariable(name = "bibliothequeId")Long bibliothequeId);

    @DeleteMapping("/reservations/{reservationId}")
    String deleteReservation(@PathVariable Long reservationId);

}
