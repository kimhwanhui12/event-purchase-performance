package hwan.perfscale.domain.product.dto;

import java.util.List;

public record ProductListResponse(List<ProductSummaryResponse> products, PageInfo pageInfo) {
}
