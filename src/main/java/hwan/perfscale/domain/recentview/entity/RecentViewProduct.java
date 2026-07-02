package hwan.perfscale.domain.recentview.entity;

import hwan.perfscale.domain.product.entity.Product;
import hwan.perfscale.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "recent_view_products",
       uniqueConstraints = @UniqueConstraint(
               name = "uk_recentview_user_product",
               columnNames = {"user_id", "product_id"}
       ))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "viewedAt"})
public class RecentViewProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "viewed_at", nullable = false)
    private LocalDateTime viewedAt;

    @Builder
    public RecentViewProduct(User user, Product product) {
        this.user = user;
        this.product = product;
        this.viewedAt = LocalDateTime.now();
    }

    /**
     * 재조회 시 새 행을 만들지 않고 조회 시각만 갱신
     */
    public void updateViewedAt() {
        this.viewedAt = LocalDateTime.now();
    }
}
