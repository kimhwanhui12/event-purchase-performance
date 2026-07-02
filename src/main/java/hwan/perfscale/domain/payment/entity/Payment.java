package hwan.perfscale.domain.payment.entity;

import hwan.perfscale.domain.order.entity.Order;
import hwan.perfscale.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "status", "amount"})
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", unique = true, nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** 승인 전에는 null */
    @Column(name = "payment_key", length = 255, unique = true)
    private String paymentKey;

    @Column(nullable = false)
    private int amount;

    /** 승인 후 채워짐 (카드/간편결제 등) */
    @Column(length = 50)
    private String method;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status = PaymentStatus.READY;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "canceled_at")
    private LocalDateTime canceledAt;

    @Column(name = "cancel_reason", length = 255)
    private String cancelReason;

    @Column(name = "canceled_amount", nullable = false)
    private int canceledAmount = 0;

    /** 토스 errorCode 저장 */
    @Column(name = "fail_reason", length = 255)
    private String failReason;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public Payment(Order order, User user, int amount) {
        this.order = order;
        this.user = user;
        this.amount = amount;
        this.status = PaymentStatus.READY;
        this.createdAt = LocalDateTime.now();
    }

    // ─── 상태 전환 메서드 ────────────────────────────────────────────────────

    /**
     * 결제 승인 완료 처리 (READY → DONE)
     */
    public void confirm(String paymentKey, String method, LocalDateTime approvedAt) {
        if (this.status != PaymentStatus.READY) {
            throw new IllegalStateException("READY 상태의 결제만 승인할 수 있습니다. 현재 상태: " + this.status);
        }
        this.paymentKey = paymentKey;
        this.method = method;
        this.approvedAt = approvedAt;
        this.status = PaymentStatus.DONE;
    }

    /**
     * 결제 실패 처리 (READY → FAILED)
     */
    public void fail(String failReason) {
        if (this.status != PaymentStatus.READY) {
            throw new IllegalStateException("READY 상태의 결제만 실패 처리할 수 있습니다. 현재 상태: " + this.status);
        }
        this.failReason = failReason;
        this.status = PaymentStatus.FAILED;
    }

    /**
     * 환불/결제 취소 (DONE → CANCELLED)
     */
    public void cancel(String cancelReason, int cancelAmount) {
        if (this.status != PaymentStatus.DONE) {
            throw new IllegalStateException("DONE 상태의 결제만 취소할 수 있습니다. 현재 상태: " + this.status);
        }
        this.cancelReason = cancelReason;
        this.canceledAmount = cancelAmount;
        this.canceledAt = LocalDateTime.now();
        this.status = PaymentStatus.CANCELLED;
    }

    public boolean isAlreadyDone() {
        return this.status == PaymentStatus.DONE;
    }
}
