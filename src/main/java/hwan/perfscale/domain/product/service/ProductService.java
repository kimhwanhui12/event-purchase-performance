package hwan.perfscale.domain.product.service;

import hwan.perfscale.domain.product.dto.PageInfo;
import hwan.perfscale.domain.product.dto.ProductDetailResponse;
import hwan.perfscale.domain.product.dto.ProductListResponse;
import hwan.perfscale.domain.product.dto.ProductSummaryResponse;
import hwan.perfscale.domain.product.entity.Product;
import hwan.perfscale.domain.product.entity.ProductGender;
import hwan.perfscale.domain.product.entity.ProductOption;
import hwan.perfscale.domain.product.repository.ProductOptionRepository;
import hwan.perfscale.domain.product.repository.ProductQueryRepository;
import hwan.perfscale.domain.product.repository.ProductRepository;
import hwan.perfscale.domain.product.repository.ProductSearchResult;
import hwan.perfscale.global.exception.BusinessException;
import hwan.perfscale.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;
    private final ProductQueryRepository productQueryRepository;

    public ProductListResponse getProducts(String keyword, Long categoryId, ProductGender gender,
                                            int page, int size) {
        ProductSearchResult result = productQueryRepository.searchWithPaging(
                keyword, null, categoryId, gender, null, page, size);

        List<ProductSummaryResponse> products = result.content().stream()
                .map(ProductSummaryResponse::from)
                .toList();
        return new ProductListResponse(products, new PageInfo(page, result.hasNext()));
    }

    public ProductDetailResponse getProductDetail(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));
        List<ProductOption> options = productOptionRepository.findByProductId(productId);
        return ProductDetailResponse.of(product, options);
    }
}
