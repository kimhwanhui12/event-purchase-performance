package hwan.perfscale.domain.order.entity;

public enum OrderStatus {
    PENDING,    // 주문 생성 / 결제 대기
    PAID,       // 결제 완료
    SHIPPING,   // 배송 중
    DELIVERED,  // 배송 완료
    CANCELLED   // 취소 / 환불
}
