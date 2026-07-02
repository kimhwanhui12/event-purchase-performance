package hwan.perfscale.domain.order.controller;

import hwan.perfscale.domain.order.dto.*;
import hwan.perfscale.domain.order.service.OrderService;
import hwan.perfscale.global.auth.CurrentUserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@CurrentUserId Long userId, @RequestBody CreateOrderRequest request) {
        OrderResponse response = orderService.createOrder(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public OrderListResponse getOrders(@CurrentUserId Long userId,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size) {
        return orderService.getOrders(userId, page, size);
    }

    @GetMapping("/{orderId}")
    public OrderDetailResponse getOrderDetail(@CurrentUserId Long userId, @PathVariable String orderId) {
        return orderService.getOrderDetail(userId, orderId);
    }

    @PostMapping("/{orderId}/cancel")
    public CancelOrderResponse cancelOrder(@CurrentUserId Long userId, @PathVariable String orderId) {
        return orderService.cancelOrder(userId, orderId);
    }
}
