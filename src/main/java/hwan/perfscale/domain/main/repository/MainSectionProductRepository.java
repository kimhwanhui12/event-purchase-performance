package hwan.perfscale.domain.main.repository;

import hwan.perfscale.domain.main.entity.MainSectionProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MainSectionProductRepository extends JpaRepository<MainSectionProduct, Long> {

    /**
     * 특정 섹션에 속한 상품 목록을 노출 순서대로 조회
     */
    List<MainSectionProduct> findAllBySectionIdOrderByDisplayOrderAsc(Long sectionId);

    /**
     * 섹션 + 상품 조합의 중복 여부 확인
     */
    boolean existsBySectionIdAndProductId(Long sectionId, Long productId);
}
