package hwan.perfscale.domain.product.repository;

import hwan.perfscale.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * 브랜드별 상품 단순 조회
     */
    List<Product> findAllByBrandId(Long brandId);

    /**
     * 카테고리별 상품 단순 조회
     */
    List<Product> findAllByCategoryId(Long categoryId);

    // 복잡한 fetch join, 동적 검색 → ProductQueryRepository 사용
}
