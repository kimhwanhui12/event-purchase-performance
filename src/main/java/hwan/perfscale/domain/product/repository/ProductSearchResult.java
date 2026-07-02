package hwan.perfscale.domain.product.repository;

import hwan.perfscale.domain.product.entity.Product;

import java.util.List;

public record ProductSearchResult(List<Product> content, boolean hasNext) {
}
