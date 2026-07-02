package hwan.perfscale.domain.order.dto;

import hwan.perfscale.domain.order.entity.Order;
import hwan.perfscale.domain.order.entity.OrderStatus;

import java.util.List;

public record OrderDetailResponse(
        String orderId,
        OrderStatus status,
        int totalAmount,
        List<OrderItemResponse> orderItems,
        DeliveryInfoResponse deliveryInfo
) {
    public static OrderDetailResponse from(Order order) {
        return new OrderDetailResponse(
                order.getOrderNo(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getItems().stream().map(OrderItemResponse::from).toList(),
                DeliveryInfoResponse.from(order)
        );
    }
}
