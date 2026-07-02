package hwan.perfscale.domain.cart.dto;

import java.util.List;

public record CartResponse(
        List<CartItemResponse> cartItems,
        int totalProductPrice,
        int totalDeliveryFee,
        int expectedTotalPrice
) {
}
