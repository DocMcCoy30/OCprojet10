package com.dmc30.clientui.proxy;

import com.dmc30.clientui.shared.bean.reservation.ReservationBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "reservation-service", url = "localhost:9004")
public interface ReservationServiceProxy {

    @PostMapping("/reservations/")
    ReservationBean createReservation(ReservationBean reservationBean);
}
