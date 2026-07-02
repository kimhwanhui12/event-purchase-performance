package hwan.perfscale.domain.cart.controller;

import hwan.perfscale.domain.cart.dto.*;
import hwan.perfscale.domain.cart.service.CartService;
import hwan.perfscale.global.auth.CurrentUserId;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public CartResponse getCart(@CurrentUserId Long userId) {
        return cartService.getCart(userId);
    }

    @PostMapping
    public AddCartItemResponse addCartItem(@CurrentUserId Long userId, @RequestBody AddCartItemRequest request) {
        return cartService.addCartItem(userId, request);
    }

    @PatchMapping("/{cartItemId}")
    public UpdateCartItemResponse updateCartItem(@CurrentUserId Long userId, @PathVariable Long cartItemId,
                                                  @RequestBody UpdateCartItemRequest request) {
        return cartService.updateCartItemQuantity(userId, cartItemId, request.quantity());
    }

    @DeleteMapping("/clear")
    public SuccessResponse clearCart(@CurrentUserId Long userId) {
        return cartService.clearCart(userId);
    }

    @DeleteMapping("/{cartItemId}")
    public SuccessResponse deleteCartItem(@CurrentUserId Long userId, @PathVariable Long cartItemId) {
        return cartService.deleteCartItem(userId, cartItemId);
    }

    @DeleteMapping
    public SuccessResponse deleteCartItems(@CurrentUserId Long userId, @RequestParam List<Long> cartItemIds) {
        return cartService.deleteCartItems(userId, cartItemIds);
    }

    @PostMapping("/calculate")
    public CalculateCartResponse calculate(@CurrentUserId Long userId, @RequestBody CalculateCartRequest request) {
        return cartService.calculate(userId, request.cartItemIds());
    }
}
