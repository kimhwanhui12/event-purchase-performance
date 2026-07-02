package hwan.perfscale.domain.cart.entity;

import hwan.perfscale.domain.product.entity.Product;
import hwan.perfscale.domain.product.entity.ProductOption;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cart_items",
       uniqueConstraints = @UniqueConstraint(
               name = "uk_cart_product_option",
               columnNames = {"cart_id", "product_id", "option_id"}
       ))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "quantity"})
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id", nullable = false)
    private ProductOption option;

    @Column(nullable = false)
    private int quantity;

    @Builder
    public CartItem(Cart cart, Product product, ProductOption option, int quantity) {
        this.cart = cart;
        this.product = product;
        this.option = option;
        this.quantity = quantity;
    }

    // ─── 수정 메서드 ────────────────────────────────────────────────────────

    public void increaseQuantity(int amount) {
        this.quantity += amount;
    }

    public void updateQuantity(int quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException("수량은 1 이상이어야 합니다.");
        }
        this.quantity = quantity;
    }
}
