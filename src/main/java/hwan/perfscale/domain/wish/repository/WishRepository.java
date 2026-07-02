package hwan.perfscale.domain.wish.repository;

import hwan.perfscale.domain.wish.entity.Wish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WishRepository extends JpaRepository<Wish, Long> {

    boolean existsByUserIdAndProductId(Long userId, Long productId);

    void deleteByUserIdAndProductId(Long userId, Long productId);

    @Query("SELECT w FROM Wish w " +
           "JOIN FETCH w.product p " +
           "JOIN FETCH p.brand " +
           "WHERE w.user.id = :userId " +
           "ORDER BY w.createdAt DESC")
    List<Wish> findByUserIdWithProductDetails(@Param("userId") Long userId);
}
