package com.dmc30.clientui.proxy;

import com.dmc30.clientui.shared.bean.reservation.ReservationBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
}
