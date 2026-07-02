package hwan.perfscale.domain.order.dto;

import hwan.perfscale.domain.order.entity.OrderItem;

public record OrderItemResponse(
        Long productId,
        String productName,
        String optionName,
        int quantity,
        int price
) {
    public static OrderItemResponse from(OrderItem item) {
        return new OrderItemResponse(
                item.getProduct() != null ? item.getProduct().getId() : null,
                item.getSnapshotName(),
                item.getSnapshotOptionName(),
                item.getQuantity(),
                item.getSnapshotPrice()
        );
    }
}
