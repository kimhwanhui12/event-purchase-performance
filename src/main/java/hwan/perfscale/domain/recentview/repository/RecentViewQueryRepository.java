package hwan.perfscale.domain.recentview.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hwan.perfscale.domain.recentview.entity.RecentViewProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static hwan.perfscale.domain.recentview.entity.QRecentViewProduct.recentViewProduct;
import static hwan.perfscale.domain.product.entity.QProduct.product;
import static hwan.perfscale.domain.brand.entity.QBrand.brand;

@Repository
@RequiredArgsConstructor
public class RecentViewQueryRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * 사용자가 최근 조회한 상품을 최신순으로 상위 N개 조회 (상품 + 브랜드 fetch join)
     */
    public List<RecentViewProduct> findRecentByUserId(Long userId, int limit) {
        return queryFactory
                .selectFrom(recentViewProduct)
                .join(recentViewProduct.product, product).fetchJoin()
                .join(product.brand, brand).fetchJoin()
                .where(recentViewProduct.user.id.eq(userId))
                .orderBy(recentViewProduct.viewedAt.desc())
                .limit(limit)
                .fetch();
    }
}
