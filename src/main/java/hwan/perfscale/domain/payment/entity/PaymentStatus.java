package hwan.perfscale.domain.payment.entity;

public enum PaymentStatus {
    READY,      // 결제 준비
    DONE,       // 결제 승인 완료
    FAILED,     // 결제 실패
    CANCELLED   // 결제 취소/환불
}
