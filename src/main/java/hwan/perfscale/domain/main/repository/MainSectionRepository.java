package hwan.perfscale.domain.main.repository;

import hwan.perfscale.domain.main.entity.MainSection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MainSectionRepository extends JpaRepository<MainSection, Long> {

    /**
     * 활성화된 섹션을 노출 순서대로 단순 조회
     */
    List<MainSection> findAllByIsActiveTrueOrderByDisplayOrderAsc();

    // fetch join + 동적 조건 조회 → MainSectionQueryRepository 사용
}
