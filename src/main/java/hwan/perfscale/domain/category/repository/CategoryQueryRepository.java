package hwan.perfscale.domain.category.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hwan.perfscale.domain.category.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static hwan.perfscale.domain.category.entity.QCategory.category;

@Repository
@RequiredArgsConstructor
public class CategoryQueryRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * 최상위 카테고리 + 하위 카테고리 한 번에 fetch join
     * JPQL의 컬렉션 fetch join은 MultipleBagFetchException 위험이 있어
     * QueryDSL로 명시적으로 제어
     */
    public List<Category> findAllRootWithChildren() {
        // alias 분리: parent용 category, children용 child
        var child = new hwan.perfscale.domain.category.entity.QCategory("child");

        return queryFactory
                .selectFrom(category)
                .leftJoin(category.children, child).fetchJoin()
                .where(category.parent.isNull())
                .distinct()
                .fetch();
    }

    /**
     * 특정 이름을 포함하는 카테고리 검색 (대소문자 무시)
     */
    public List<Category> searchByName(String keyword) {
        return queryFactory
                .selectFrom(category)
                .where(
                        keyword != null && !keyword.isBlank()
                                ? category.name.containsIgnoreCase(keyword)
                                : null
                )
                .fetch();
    }
}
