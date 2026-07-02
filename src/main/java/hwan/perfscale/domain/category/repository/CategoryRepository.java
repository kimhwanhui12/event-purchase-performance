package hwan.perfscale.domain.category.repository;

import hwan.perfscale.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * 최상위 카테고리(부모 없는) 목록 조회
     */
    List<Category> findAllByParentIsNull();

    /**
     * 특정 부모에 속한 하위 카테고리 목록 조회
     */
    List<Category> findAllByParentId(Long parentId);

    // fetch join + 동적 검색 → CategoryQueryRepository 사용
}
