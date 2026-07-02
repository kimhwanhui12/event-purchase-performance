package hwan.perfscale.domain.product.dto;

import hwan.perfscale.domain.product.entity.Product;

public record ProductSummaryResponse(
        Long productId,
        String productName,
        String brandName,
        int discountPrice,
        String imageUrl,
        boolean isDiscounted
) {
    public static ProductSummaryResponse from(Product product) {
        return new ProductSummaryResponse(
                product.getId(),
                product.getProductName(),
                product.getBrand().getName(),
                product.getDiscountedPrice(),
                product.getImageUrl(),
                product.getDiscountRate() > 0
        );
    }
}
