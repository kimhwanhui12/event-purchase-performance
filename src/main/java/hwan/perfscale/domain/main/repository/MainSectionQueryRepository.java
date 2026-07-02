package hwan.perfscale.domain.main.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hwan.perfscale.domain.main.entity.MainSection;
import hwan.perfscale.domain.main.entity.QMainSection;
import hwan.perfscale.domain.main.entity.QMainSectionProduct;
import hwan.perfscale.domain.product.entity.QProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static hwan.perfscale.domain.main.entity.QMainSection.mainSection;
import static hwan.perfscale.domain.main.entity.QMainSectionProduct.mainSectionProduct;
import static hwan.perfscale.domain.product.entity.QProduct.product;

@Repository
@RequiredArgsConstructor
public class MainSectionQueryRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * 활성화된 섹션 + 섹션상품 + 상품 + 브랜드 한 번에 조회 (N+1 방지)
     * JPQL DISTINCT + fetch join은 페이징 불가 → QueryDSL로 명확하게 제어
     */
    public List<MainSection> findAllActiveWithProducts() {
        return queryFactory
                .selectFrom(mainSection)
                .leftJoin(mainSection.sectionProducts, mainSectionProduct).fetchJoin()
                .leftJoin(mainSectionProduct.product, product).fetchJoin()
                .where(mainSection.isActive.isTrue())
                .orderBy(mainSection.displayOrder.asc(), mainSectionProduct.displayOrder.asc())
                .distinct()
                .fetch();
    }

    /**
     * 특정 섹션 타입에 해당하는 활성 섹션 + 상품 조회
     */
    public List<MainSection> findActiveByTypeWithProducts(String sectionType) {
        return queryFactory
                .selectFrom(mainSection)
                .leftJoin(mainSection.sectionProducts, mainSectionProduct).fetchJoin()
                .leftJoin(mainSectionProduct.product, product).fetchJoin()
                .where(
                        mainSection.isActive.isTrue(),
                        sectionType != null ? mainSection.sectionType.eq(sectionType) : null
                )
                .orderBy(mainSection.displayOrder.asc(), mainSectionProduct.displayOrder.asc())
                .distinct()
                .fetch();
    }
}
