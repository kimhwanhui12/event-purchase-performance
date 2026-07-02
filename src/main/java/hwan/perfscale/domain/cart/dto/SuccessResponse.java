package hwan.perfscale.domain.cart.dto;

public record SuccessResponse(boolean success) {
    public static SuccessResponse ok() {
        return new SuccessResponse(true);
    }
}
