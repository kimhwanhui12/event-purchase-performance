package hwan.perfscale.domain.quickmenu.repository;

import hwan.perfscale.domain.quickmenu.entity.QuickMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuickMenuRepository extends JpaRepository<QuickMenu, Long> {

    /**
     * 활성화된 퀵 메뉴를 노출 순서대로 조회
     */
    List<QuickMenu> findAllByIsActiveTrueOrderByDisplayOrderAsc();
}
