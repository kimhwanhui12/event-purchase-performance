package hwan.perfscale.domain.main.entity;

import hwan.perfscale.domain.product.entity.Product;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "main_section_products")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "displayOrder"})
public class MainSectionProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", nullable = false)
    private MainSection section;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "display_order", nullable = false)
    private int displayOrder;

    @Builder
    public MainSectionProduct(MainSection section, Product product, int displayOrder) {
        this.section = section;
        this.product = product;
        this.displayOrder = displayOrder;
    }

    public void changeDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }
}
