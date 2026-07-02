package hwan.perfscale.domain.cart.repository;

import hwan.perfscale.domain.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByCartId(Long cartId);

    @Query("SELECT ci FROM CartItem ci " +
           "JOIN FETCH ci.product p " +
           "JOIN FETCH p.brand " +
           "JOIN FETCH ci.option " +
           "WHERE ci.cart.id = :cartId")
    List<CartItem> findByCartIdWithDetails(@Param("cartId") Long cartId);

    Optional<CartItem> findByCartIdAndProductIdAndOptionId(Long cartId, Long productId, Long optionId);

    void deleteByCartIdAndIdIn(Long cartId, List<Long> itemIds);
}
