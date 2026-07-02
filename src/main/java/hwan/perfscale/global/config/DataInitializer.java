package hwan.perfscale.global.config;

import hwan.perfscale.domain.brand.entity.Brand;
import hwan.perfscale.domain.brand.repository.BrandRepository;
import hwan.perfscale.domain.category.entity.Category;
import hwan.perfscale.domain.category.repository.CategoryRepository;
import hwan.perfscale.domain.product.entity.Product;
import hwan.perfscale.domain.product.entity.ProductGender;
import hwan.perfscale.domain.product.entity.ProductOption;
import hwan.perfscale.domain.product.repository.ProductOptionRepository;
import hwan.perfscale.domain.product.repository.ProductRepository;
import hwan.perfscale.domain.user.entity.User;
import hwan.perfscale.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 인증 계층이 없는 동안 API를 curl로 바로 검증할 수 있도록 최소한의 시드 데이터를 생성한다.
 * H2 인메모리 DB(create-drop)라 기동할 때마다 다시 만들어진다.
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return;
        }

        userRepository.save(User.builder()
                .email("tester@perfscale.dev")
                .password("test1234!")
                .name("홍길동")
                .nickname("테스터")
                .phone("010-1234-5678")
                .defaultAddress("서울시 강남구 테헤란로 123")
                .build());

        Brand nike = brandRepository.save(Brand.builder().name("나이키").build());
        Category outer = categoryRepository.save(Category.builder().name("아우터").parent(null).build());

        Product product1 = productRepository.save(Product.builder()
                .productName("오버핏 맨투맨티셔츠")
                .description("편안하게 입을 수 있는 오버핏 디자인입니다.")
                .brand(nike)
                .category(outer)
                .price(69000)
                .gender(ProductGender.MALE)
                .discountRate(30)
                .rating(4)
                .reviewCount(120)
                .imageUrl("https://cdn.image.com/prod101.jpg")
                .source("SEED")
                .sourceUrl(null)
                .build());
        productOptionRepository.save(ProductOption.builder()
                .product(product1).optionName("BLACK / L").stockQuantity(15).additionalPrice(0).build());
        productOptionRepository.save(ProductOption.builder()
                .product(product1).optionName("WHITE / M").stockQuantity(0).additionalPrice(0).build());

        Product product2 = productRepository.save(Product.builder()
                .productName("경량 패딩 조끼")
                .description("가볍게 걸치기 좋은 경량 패딩 조끼입니다.")
                .brand(nike)
                .category(outer)
                .price(89000)
                .gender(ProductGender.UNISEX)
                .discountRate(10)
                .rating(5)
                .reviewCount(45)
                .imageUrl("https://cdn.image.com/prod102.jpg")
                .source("SEED")
                .sourceUrl(null)
                .build());
        productOptionRepository.save(ProductOption.builder()
                .product(product2).optionName("NAVY / FREE").stockQuantity(8).additionalPrice(0).build());
    }
}
