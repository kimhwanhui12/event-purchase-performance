package hwan.perfscale.domain.order.dto;

import hwan.perfscale.domain.order.entity.Order;
import hwan.perfscale.domain.order.entity.OrderStatus;

public record CancelOrderResponse(String orderId, OrderStatus status) {
    public static CancelOrderResponse from(Order order) {
        return new CancelOrderResponse(order.getOrderNo(), order.getStatus());
    }
}
