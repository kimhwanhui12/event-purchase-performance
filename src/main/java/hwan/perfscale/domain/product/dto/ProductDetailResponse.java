package hwan.perfscale.domain.product.dto;

import hwan.perfscale.domain.product.entity.Product;
import hwan.perfscale.domain.product.entity.ProductGender;
import hwan.perfscale.domain.product.entity.ProductOption;

import java.util.List;

public record ProductDetailResponse(
        Long productId,
        String productName,
        String brandName,
        int originPrice,
        int discountPrice,
        String description,
        ProductGender gender,
        List<ProductOptionResponse> options
) {
    public static ProductDetailResponse of(Product product, List<ProductOption> options) {
        return new ProductDetailResponse(
                product.getId(),
                product.getProductName(),
                product.getBrand().getName(),
                product.getPrice(),
                product.getDiscountedPrice(),
                product.getDescription(),
                product.getGender(),
                options.stream().map(ProductOptionResponse::from).toList()
        );
    }
}
