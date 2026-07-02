package hwan.perfscale.domain.cart.dto;

public record AddCartItemRequest(Long productId, Long optionId, int quantity) {
}
