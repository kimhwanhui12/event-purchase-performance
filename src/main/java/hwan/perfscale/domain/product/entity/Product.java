package hwan.perfscale.domain.product.entity;

import hwan.perfscale.domain.brand.entity.Brand;
import hwan.perfscale.domain.category.entity.Category;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "productName", "price"})
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_name", length = 255, nullable = false)
    private String productName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private int price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductGender gender = ProductGender.UNISEX;

    @Column(name = "discount_rate", nullable = false)
    private int discountRate;

    @Column(nullable = false)
    private int rating;

    @Column(name = "review_count", nullable = false)
    private int reviewCount;

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Column(length = 50, nullable = false)
    private String source;

    @Column(name = "source_url", columnDefinition = "TEXT")
    private String sourceUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Builder
    public Product(String productName, Brand brand, Category category, int price,
                   ProductGender gender, int discountRate, int rating, int reviewCount,
                   String imageUrl, String source, String sourceUrl) {
        this.productName = productName;
        this.brand = brand;
        this.category = category;
        this.price = price;
        this.gender = gender != null ? gender : ProductGender.UNISEX;
        this.discountRate = discountRate;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.imageUrl = imageUrl;
        this.source = source;
        this.sourceUrl = sourceUrl;
        this.createdAt = LocalDateTime.now();
    }

    /**
     * 할인 적용 후 실제 판매가 계산
     */
    public int getDiscountedPrice() {
        return (int) (this.price * (1 - this.discountRate / 100.0));
    }
}
