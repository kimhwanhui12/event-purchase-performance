package hwan.perfscale.domain.recentview.repository;

import hwan.perfscale.domain.recentview.entity.RecentViewProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecentViewProductRepository extends JpaRepository<RecentViewProduct, Long> {

    Optional<RecentViewProduct> findByUserIdAndProductId(Long userId, Long productId);
}
