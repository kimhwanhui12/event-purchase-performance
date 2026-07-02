package hwan.perfscale.domain.cart.dto;

import hwan.perfscale.domain.cart.entity.CartItem;
import hwan.perfscale.domain.product.entity.ProductStatus;

public record CartItemResponse(
        Long cartItemId,
        Long productId,
        String productName,
        Long optionId,
        String optionName,
        int quantity,
        int price,
        ProductStatus status
) {
    public static CartItemResponse from(CartItem cartItem) {
        int unitPrice = cartItem.getProduct().getDiscountedPrice() + cartItem.getOption().getAdditionalPrice();
        return new CartItemResponse(
                cartItem.getId(),
                cartItem.getProduct().getId(),
                cartItem.getProduct().getProductName(),
                cartItem.getOption().getId(),
                cartItem.getOption().getOptionName(),
                cartItem.getQuantity(),
                unitPrice,
                cartItem.getOption().getStatus()
        );
    }
}
