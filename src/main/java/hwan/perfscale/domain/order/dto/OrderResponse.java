package hwan.perfscale.domain.order.dto;

import hwan.perfscale.domain.order.entity.Order;

import java.util.List;

public record OrderResponse(String orderId, int totalAmount, List<OrderItemResponse> orderItems) {
    public static OrderResponse from(Order order) {
        return new OrderResponse(
                order.getOrderNo(),
                order.getTotalAmount(),
                order.getItems().stream().map(OrderItemResponse::from).toList()
        );
    }
}
