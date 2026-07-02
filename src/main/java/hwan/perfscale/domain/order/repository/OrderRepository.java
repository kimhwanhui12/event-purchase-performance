package hwan.perfscale.domain.order.repository;

import hwan.perfscale.domain.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderNo(String orderNo);

    Page<Order> findByUserId(Long userId, Pageable pageable);

    @Query("SELECT o FROM Order o " +
           "JOIN FETCH o.items i " +
           "JOIN FETCH i.product " +
           "JOIN FETCH i.option " +
           "WHERE o.id = :id")
    Optional<Order> findByIdWithItems(@Param("id") Long id);
}
