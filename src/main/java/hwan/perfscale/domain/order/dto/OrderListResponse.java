package hwan.perfscale.domain.order.dto;

import java.util.List;

public record OrderListResponse(List<OrderListItemResponse> orders) {
}
