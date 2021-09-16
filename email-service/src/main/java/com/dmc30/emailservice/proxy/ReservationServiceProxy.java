package com.dmc30.emailservice.proxy;

import com.dmc30.emailservice.service.bean.ReservationBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "reservation-service", url = "localhost:9004")
public interface ReservationServiceProxy {

    @GetMapping("reservations/")
    List<ReservationBean> getReservations();

    @GetMapping("reservations/listeAttente/{livreId}&{bibliothequeId}")
    List<ReservationBean> getReservationsByLivreAndBibliothequeId(@PathVariable(name = "livreId") Long livreId,
                                                                  @PathVariable(name = "bibliothequeId") Long bibliothequeId);

    @GetMapping("/reservations/expiree/{userId}")
    List<ReservationBean> getReservationsExpireesByUserId(@PathVariable(name = "userId") Long userId);

    @GetMapping("/reservations/livre/{livreId}")
    List<ReservationBean> getReservationsByLivreId(@PathVariable(name = "livreId") Long livreId);

    @PutMapping("/reservations/update")
    void updateReservation(@RequestBody ReservationBean reservation);
}
