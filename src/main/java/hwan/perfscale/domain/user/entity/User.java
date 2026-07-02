package hwan.perfscale.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "email", "nickname"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255, unique = true, nullable = false)
    private String email;

    /** 소셜 로그인 사용자는 null 허용 */
    @Column(length = 255)
    private String password;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 100, unique = true, nullable = false)
    private String nickname;

    @Column(length = 20)
    private String phone;

    @Column(name = "profile_image_url", columnDefinition = "TEXT")
    private String profileImageUrl;

    @Column(name = "default_address", columnDefinition = "TEXT")
    private String defaultAddress;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.ROLE_USER;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OAuthProvider provider = OAuthProvider.LOCAL;

    /** OAuth 제공자의 사용자 식별자 */
    @Column(name = "provider_id", length = 255)
    private String providerId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public User(String email, String password, String name, String nickname,
                String phone, String profileImageUrl, String defaultAddress,
                UserRole role, OAuthProvider provider, String providerId) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.phone = phone;
        this.profileImageUrl = profileImageUrl;
        this.defaultAddress = defaultAddress;
        this.role = role != null ? role : UserRole.ROLE_USER;
        this.provider = provider != null ? provider : OAuthProvider.LOCAL;
        this.providerId = providerId;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // ─── 수정 메서드 ──────────────────────────────────────────────────────────

    public void updateProfile(String nickname, String phone, String defaultAddress, String profileImageUrl) {
        if (nickname != null && !nickname.isBlank()) this.nickname = nickname;
        if (phone != null) this.phone = phone;
        if (defaultAddress != null) this.defaultAddress = defaultAddress;
        if (profileImageUrl != null) this.profileImageUrl = profileImageUrl;
        this.updatedAt = LocalDateTime.now();
    }

    public void changePassword(String encodedPassword) {
        this.password = encodedPassword;
        this.updatedAt = LocalDateTime.now();
    }
}
