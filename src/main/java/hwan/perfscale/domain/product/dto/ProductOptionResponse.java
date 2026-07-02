package hwan.perfscale.domain.product.dto;

import hwan.perfscale.domain.product.entity.ProductOption;
import hwan.perfscale.domain.product.entity.ProductStatus;

public record ProductOptionResponse(
        Long optionId,
        String optionName,
        int additionalPrice,
        int stockQuantity,
        ProductStatus status
) {
    public static ProductOptionResponse from(ProductOption option) {
        return new ProductOptionResponse(
                option.getId(),
                option.getOptionName(),
                option.getAdditionalPrice(),
                option.getStockQuantity(),
                option.getStatus()
        );
    }
}
