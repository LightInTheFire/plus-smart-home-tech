package ru.yandex.practicum.order.client;

import java.util.UUID;

import ru.yandex.practicum.order.dto.OrderDto;

import org.osimp.api.ReleaseConnection;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "order", path = "/api/v1/order")
public interface OrderClient {

    @PostMapping("/payment")
    @ReleaseConnection
    OrderDto processPayment(@RequestBody UUID orderId);

    @PostMapping("/payment/failed")
    @ReleaseConnection
    OrderDto processPaymentFailed(@RequestBody UUID orderId);

    @PostMapping("/payment/succeed")
    @ReleaseConnection
    OrderDto processPaymentSucceed(@RequestBody UUID orderId);

    @PostMapping("/delivery")
    @ReleaseConnection
    OrderDto processDelivery(@RequestBody UUID orderId);

    @PostMapping("/delivery/failed")
    @ReleaseConnection
    OrderDto processDeliveryFailed(@RequestBody UUID orderId);

    @PostMapping("/assembly")
    @ReleaseConnection
    OrderDto processAssembled(@RequestBody UUID orderId);
}
