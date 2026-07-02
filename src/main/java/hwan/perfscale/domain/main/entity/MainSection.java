package hwan.perfscale.domain.main.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "main_sections")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "title", "sectionType"})
public class MainSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(name = "section_type", length = 50, nullable = false)
    private String sectionType;

    @Column(name = "display_order", nullable = false)
    private int displayOrder;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("displayOrder ASC")
    private List<MainSectionProduct> sectionProducts = new ArrayList<>();

    @Builder
    public MainSection(String title, String sectionType, int displayOrder, boolean isActive) {
        this.title = title;
        this.sectionType = sectionType;
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
