package hwan.perfscale.domain.order.dto;

import hwan.perfscale.domain.order.entity.Order;

public record DeliveryInfoResponse(String receiverName, String zipCode, String address, String phone) {
    public static DeliveryInfoResponse from(Order order) {
        return new DeliveryInfoResponse(order.getReceiverName(), order.getZipCode(), order.getAddress(), order.getPhone());
    }
}
