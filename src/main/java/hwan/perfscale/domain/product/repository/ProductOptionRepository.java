package hwan.perfscale.domain.product.repository;

import hwan.perfscale.domain.product.entity.ProductOption;
import hwan.perfscale.domain.product.entity.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {

    List<ProductOption> findByProductId(Long productId);

    List<ProductOption> findByProductIdAndStatus(Long productId, ProductStatus status);
}
