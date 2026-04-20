package ru.yandex.practicum.payment.client;

import java.math.BigDecimal;
import java.util.UUID;

import ru.yandex.practicum.order.dto.OrderDto;
import ru.yandex.practicum.payment.dto.PaymentDto;

import org.osimp.api.ReleaseConnection;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment", path = "/api/v1/payment")
public interface PaymentClient {

    @PostMapping
    @ReleaseConnection
    PaymentDto payment(@RequestBody OrderDto order);

    @PostMapping("/totalCost")
    @ReleaseConnection
    BigDecimal getTotalCost(@RequestBody OrderDto order);

    @PostMapping("/productCost")
    @ReleaseConnection
    BigDecimal productCost(@RequestBody OrderDto order);

    @PostMapping("/refund")
    @ReleaseConnection
    void paymentSuccess(@RequestBody UUID paymentId);

    @PostMapping("/failed")
    @ReleaseConnection
    void paymentFailed(@RequestBody UUID paymentId);
}
