package hwan.perfscale.domain.cart.dto;

public record UpdateCartItemResponse(Long cartItemId, int quantity, int updatedPrice) {
}
