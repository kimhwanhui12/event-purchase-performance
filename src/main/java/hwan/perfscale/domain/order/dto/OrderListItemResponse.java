package hwan.perfscale.domain.order.dto;

import hwan.perfscale.domain.order.entity.Order;
import hwan.perfscale.domain.order.entity.OrderStatus;

import java.time.LocalDateTime;

public record OrderListItemResponse(
        String orderId,
        LocalDateTime orderDate,
        OrderStatus status,
        int totalAmount,
        String representativeProductName
) {
    /**
     * order.getItems() 지연로딩을 사용 - 목록 건수가 많아지면 N+1이 발생할 수 있어
     * 추후 배치 조회(IN절)로 개선 여지가 있음
     */
    public static OrderListItemResponse from(Order order) {
        var items = order.getItems();
        String representativeProductName = items.isEmpty() ? ""
                : items.get(0).getSnapshotName() + (items.size() > 1 ? " 외 " + (items.size() - 1) + "건" : "");
        return new OrderListItemResponse(
                order.getOrderNo(), order.getCreatedAt(), order.getStatus(), order.getTotalAmount(), representativeProductName);
    }
}
