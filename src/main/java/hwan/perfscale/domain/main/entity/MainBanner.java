package hwan.perfscale.domain.main.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "main_banners")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "title", "isActive"})
public class MainBanner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255, nullable = false)
    private String title;

    @Column(length = 255)
    private String subtitle;

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Column(name = "link_url", columnDefinition = "TEXT")
    private String linkUrl;

    @Column(name = "display_order", nullable = false)
    private int displayOrder;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Builder
    public MainBanner(String title, String subtitle, String imageUrl,
                      String linkUrl, int displayOrder, boolean isActive) {
        this.title = title;
        this.subtitle = subtitle;
        this.imageUrl = imageUrl;
        this.linkUrl = linkUrl;
        this.displayOrder = displayOrder;
        this.isActive = isActive;
    }

    public void activate() {
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }
}
