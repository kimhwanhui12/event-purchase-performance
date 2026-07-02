package hwan.perfscale.domain.product.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hwan.perfscale.domain.product.entity.Product;
import hwan.perfscale.domain.product.entity.ProductGender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static hwan.perfscale.domain.product.entity.QProduct.product;
import static hwan.perfscale.domain.brand.entity.QBrand.brand;
import static hwan.perfscale.domain.category.entity.QCategory.category;

@Repository
@RequiredArgsConstructor
public class ProductQueryRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * 브랜드 + 카테고리 fetch join (N+1 방지)
     * JPQL보다 타입 안전하게 처리
     */
    public List<Product> findAllWithBrandAndCategory() {
        return queryFactory
                .selectFrom(product)
                .join(product.brand, brand).fetchJoin()
                .join(product.category, category).fetchJoin()
                .fetch();
    }

    /**
     * 동적 조건 상품 검색
     * - keyword: 상품명 LIKE 검색 (null이면 조건 무시)
     * - brandId: 브랜드 필터 (null이면 조건 무시)
     * - categoryId: 카테고리 필터 (null이면 조건 무시)
     * - gender: 성별 필터 (null이면 조건 무시)
     * - maxPrice: 최대 가격 필터 (null이면 조건 무시)
     */
    public List<Product> search(String keyword, Long brandId, Long categoryId, ProductGender gender, Integer maxPrice) {
        return queryFactory
                .selectFrom(product)
                .join(product.brand, brand).fetchJoin()
                .join(product.category, category).fetchJoin()
                .where(
                        productNameContains(keyword),
                        brandIdEq(brandId),
                        categoryIdEq(categoryId),
                        genderEq(gender),
                        priceLoe(maxPrice)
                )
                .orderBy(product.createdAt.desc())
                .fetch();
    }

    /**
     * 무한 스크롤용 페이징 검색.
     * count 쿼리 없이 size+1건을 가져와 마지막 1건 존재 여부로 hasNext를 판단한다.
     */
    public ProductSearchResult searchWithPaging(String keyword, Long brandId, Long categoryId,
                                                 ProductGender gender, Integer maxPrice,
                                                 int page, int size) {
        List<Product> rows = queryFactory
                .selectFrom(product)
                .join(product.brand, brand).fetchJoin()
                .join(product.category, category).fetchJoin()
                .where(
                        productNameContains(keyword),
                        brandIdEq(brandId),
                        categoryIdEq(categoryId),
                        genderEq(gender),
                        priceLoe(maxPrice)
                )
                .orderBy(product.createdAt.desc())
                .offset((long) page * size)
                .limit(size + 1L)
                .fetch();

        boolean hasNext = rows.size() > size;
        List<Product> content = hasNext ? rows.subList(0, size) : rows;
        return new ProductSearchResult(content, hasNext);
    }

    /**
     * 특정 카테고리 상품을 평점 높은 순으로 조회
     */
    public List<Product> findTopRatedByCategory(Long categoryId, int limit) {
        return queryFactory
                .selectFrom(product)
                .join(product.brand, brand).fetchJoin()
                .join(product.category, category).fetchJoin()
                .where(categoryIdEq(categoryId))
                .orderBy(product.rating.desc(), product.reviewCount.desc())
                .limit(limit)
                .fetch();
    }

    // ─── BooleanExpression 조건 메서드 (null-safe) ───────────────────

    private BooleanExpression productNameContains(String keyword) {
        return (keyword != null && !keyword.isBlank())
                ? product.productName.containsIgnoreCase(keyword)
                : null;
    }

    private BooleanExpression brandIdEq(Long brandId) {
        return brandId != null ? product.brand.id.eq(brandId) : null;
    }

    private BooleanExpression categoryIdEq(Long categoryId) {
        return categoryId != null ? product.category.id.eq(categoryId) : null;
    }

    private BooleanExpression genderEq(ProductGender gender) {
        return gender != null ? product.gender.eq(gender) : null;
    }

    private BooleanExpression priceLoe(Integer maxPrice) {
        return maxPrice != null ? product.price.loe(maxPrice) : null;
    }
}
