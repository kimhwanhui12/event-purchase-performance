package hwan.perfscale.domain.payment.repository;

import hwan.perfscale.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrderId(Long orderId);

    Optional<Payment> findByPaymentKey(String paymentKey);

    @Query("SELECT p FROM Payment p " +
           "JOIN FETCH p.order o " +
           "WHERE p.user.id = :userId " +
           "ORDER BY p.createdAt DESC")
    List<Payment> findByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId);
}
