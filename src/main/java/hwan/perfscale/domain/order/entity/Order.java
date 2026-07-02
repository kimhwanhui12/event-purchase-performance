package hwan.perfscale.domain.order.entity;

import hwan.perfscale.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "status", "totalAmount"})
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 외부 노출용 주문 코드 ("ORD-yyyyMMdd-000123" 형식).
     * IDENTITY로 생성된 id를 그대로 패딩한 값이라 "일자별 001부터 리셋"은 아님 —
     * 진짜 일별 시퀀스가 필요하면 서비스 레이어에서 당일 카운트 기반으로 재구현할 것.
     */
    @Column(name = "order_no", length = 30, unique = true)
    private String orderNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PENDING;

    @Column(name = "total_amount", nullable = false)
    private int totalAmount;

    @Column(name = "receiver_name", length = 100)
    private String receiverName;

    @Column(name = "zip_code", length = 10)
    private String zipCode;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(length = 20)
    private String phone;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @Builder
    public Order(User user, String receiverName, String zipCode, String address, String phone) {
        this.user = user;
        this.totalAmount = 0;
        this.receiverName = receiverName;
        this.zipCode = zipCode;
        this.address = address;
        this.phone = phone;
        this.status = OrderStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    /**
     * 주문 상품 추가 (OrderItem 생성 시 스스로 계산한 소계를 총액에 누적)
     */
    public void addItem(OrderItem item) {
        this.items.add(item);
        this.totalAmount += item.getSubtotal();
    }

    /**
     * 저장 직후 id가 채번된 뒤 주문 코드를 생성
     */
    @PostPersist
    private void generateOrderNo() {
        if (this.orderNo == null) {
            this.orderNo = "ORD-" + this.createdAt.toLocalDate().format(DateTimeFormatter.BASIC_ISO_DATE)
                    + "-" + String.format("%06d", this.id);
        }
    }

    // ─── 상태 전환 메서드 ────────────────────────────────────────────────────

    /**
     * 허용된 상태 전환 규칙: PENDING → PAID → SHIPPING → DELIVERED
     */
    public void changeStatus(OrderStatus newStatus) {
        validateTransition(newStatus);
        this.status = newStatus;
    }

    /**
     * 주문 취소 (PENDING 상태에서만 가능)
     */
    public void cancel() {
        if (this.status != OrderStatus.PENDING) {
            throw new IllegalStateException("PENDING 상태의 주문만 취소할 수 있습니다. 현재 상태: " + this.status);
        }
        this.status = OrderStatus.CANCELLED;
    }

    /**
     * 결제 완료 처리
     */
    public void markAsPaid() {
        if (this.status != OrderStatus.PENDING) {
            throw new IllegalStateException("PENDING 상태의 주문만 결제 완료 처리할 수 있습니다.");
        }
        this.status = OrderStatus.PAID;
    }

    private void validateTransition(OrderStatus newStatus) {
        boolean valid = switch (this.status) {
            case PENDING -> newStatus == OrderStatus.PAID || newStatus == OrderStatus.CANCELLED;
            case PAID -> newStatus == OrderStatus.SHIPPING || newStatus == OrderStatus.CANCELLED;
            case SHIPPING -> newStatus == OrderStatus.DELIVERED;
            default -> false;
        };
        if (!valid) {
            throw new IllegalStateException(
                    "유효하지 않은 상태 전환입니다: " + this.status + " → " + newStatus);
        }
    }

    public boolean isOwner(Long userId) {
        return this.user.getId().equals(userId);
    }
}
