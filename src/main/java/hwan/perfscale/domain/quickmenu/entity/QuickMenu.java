package hwan.perfscale.domain.quickmenu.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "quick_menus")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "name", "isActive"})
public class QuickMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(name = "icon_url", columnDefinition = "TEXT")
    private String iconUrl;

    @Column(name = "link_url", columnDefinition = "TEXT")
    private String linkUrl;

    @Column(name = "display_order", nullable = false)
    private int displayOrder;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Builder
    public QuickMenu(String name, String iconUrl, String linkUrl,
                     int displayOrder, boolean isActive) {
        this.name = name;
        this.iconUrl = iconUrl;
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

    public void updateDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }
}
