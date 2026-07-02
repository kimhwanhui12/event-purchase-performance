package hwan.perfscale.domain.order.dto;

import java.util.List;

public record CreateOrderRequest(List<Long> cartItemIds, CheckoutType checkoutType) {
}
