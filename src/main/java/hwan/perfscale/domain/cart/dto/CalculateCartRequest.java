package hwan.perfscale.domain.cart.dto;

import java.util.List;

public record CalculateCartRequest(List<Long> cartItemIds) {
}
