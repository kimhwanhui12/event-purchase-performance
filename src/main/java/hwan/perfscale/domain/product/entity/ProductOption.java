package hwan.perfscale.domain.product.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_options")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "optionName", "stockQuantity"})
public class ProductOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    /**
     * 옵션명 예시: "L / 블랙", "XL / 화이트"
     */
    @Column(name = "option_name", length = 100, nullable = false)
    private String optionName;

    @Column(name = "stock_quantity", nullable = false)
    private int stockQuantity;

    /**
     * 기본 상품 가격 대비 추가 금액 (0이면 동일)
     */
    @Column(name = "additional_price", nullable = false)
    private int additionalPrice = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status = ProductStatus.AVAILABLE;

    @Builder
    public ProductOption(Product product, String optionName, int stockQuantity, int additionalPrice) {
        this.product = product;
        this.optionName = optionName;
        this.stockQuantity = stockQuantity;
        this.additionalPrice = additionalPrice;
    }

    // ─── 재고 관리 메서드 ────────────────────────────────────────────────────

    /**
     * 재고 차감 (주문 시 호출). 0이 되면 품절로 자동 전이 (판매중지 상태는 그대로 유지)
     */
    public void decreaseStock(int quantity) {
        if (this.stockQuantity < quantity) {
            throw new IllegalStateException("재고가 부족합니다. 현재 재고: " + this.stockQuantity);
        }
        this.stockQuantity -= quantity;
        if (this.stockQuantity == 0 && this.status == ProductStatus.AVAILABLE) {
            this.status = ProductStatus.SOLD_OUT;
        }
    }

    /**
     * 재고 복구 (주문 취소 시 호출). 품절로 인해 막혔던 경우만 판매중으로 되돌림
     */
    public void increaseStock(int quantity) {
        this.stockQuantity += quantity;
        if (this.status == ProductStatus.SOLD_OUT) {
            this.status = ProductStatus.AVAILABLE;
        }
    }

    /**
     * 판매 중지 (판매자 수동 조작)
     */
    public void suspend() {
        this.status = ProductStatus.SUSPENDED;
    }

    public boolean isAvailable() {
        return this.status == ProductStatus.AVAILABLE;
    }
}
