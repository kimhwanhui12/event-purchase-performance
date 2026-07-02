package hwan.perfscale.domain.product.entity;

public enum ProductStatus {
    AVAILABLE,  // 판매중
    SOLD_OUT,   // 품절 (재고 0, 시스템 자동 전이)
    SUSPENDED   // 판매 중지 (판매자 수동 조작)
}
