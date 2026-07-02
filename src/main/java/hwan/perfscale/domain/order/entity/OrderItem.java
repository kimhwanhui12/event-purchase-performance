package hwan.perfscale.domain.order.entity;

import hwan.perfscale.domain.product.entity.Product;
import hwan.perfscale.domain.product.entity.ProductOption;
import jakarta.persistence.*;
import lombok.*;

/**
 * 주문 시점 상품 정보를 스냅샷으로 저장
 * → 이후 상품 정보 변경이 주문 내역에 영향 없음
 */
@Entity
@Table(name = "order_items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "snapshotName", "snapshotPrice", "quantity"})
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    /** 주문 당시 상품 참조 (삭제돼도 스냅샷은 유지) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id")
    private ProductOption option;

    // ─── 스냅샷 필드 (주문 당시 데이터 고정) ────────────────────────────────

    @Column(name = "snapshot_name", length = 255, nullable = false)
    private String snapshotName;

    @Column(name = "snapshot_price", nullable = false)
    private int snapshotPrice;

    @Column(name = "snapshot_option_name", length = 100)
    private String snapshotOptionName;

    @Column(name = "snapshot_image_url", columnDefinition = "TEXT")
    private String snapshotImageUrl;

    @Column(nullable = false)
    private int quantity;

    @Builder
    public OrderItem(Order order, Product product, ProductOption option, int quantity) {
        this.order = order;
        this.product = product;
        this.option = option;
        this.quantity = quantity;
        // 스냅샷 저장
        this.snapshotName = product.getProductName();
        this.snapshotPrice = product.getDiscountedPrice() + option.getAdditionalPrice();
        this.snapshotOptionName = option.getOptionName();
        this.snapshotImageUrl = product.getImageUrl();
    }

    /**
     * 해당 OrderItem의 소계 (단가 × 수량)
     */
    public int getSubtotal() {
        return this.snapshotPrice * this.quantity;
    }
}
