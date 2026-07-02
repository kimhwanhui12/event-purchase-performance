package hwan.perfscale.domain.main.repository;

import hwan.perfscale.domain.main.entity.MainBanner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MainBannerRepository extends JpaRepository<MainBanner, Long> {

    /**
     * 활성화된 배너를 노출 순서대로 조회
     */
    List<MainBanner> findAllByIsActiveTrueOrderByDisplayOrderAsc();
}
