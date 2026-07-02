package hwan.perfscale.domain.cart.service;

import hwan.perfscale.domain.cart.dto.*;
import hwan.perfscale.domain.cart.entity.Cart;
import hwan.perfscale.domain.cart.entity.CartItem;
import hwan.perfscale.domain.cart.repository.CartItemRepository;
import hwan.perfscale.domain.cart.repository.CartRepository;
import hwan.perfscale.domain.product.entity.Product;
import hwan.perfscale.domain.product.entity.ProductOption;
import hwan.perfscale.domain.product.repository.ProductOptionRepository;
import hwan.perfscale.domain.product.repository.ProductRepository;
import hwan.perfscale.domain.user.entity.User;
import hwan.perfscale.domain.user.repository.UserRepository;
import hwan.perfscale.global.exception.BusinessException;
import hwan.perfscale.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {

    private static final int DELIVERY_FEE = 3000;

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;
    private final UserRepository userRepository;

    @Transactional
    public Cart getOrCreateCart(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
                    return cartRepository.save(Cart.builder().user(user).build());
                });
    }

    public CartResponse getCart(Long userId) {
        Cart cart = getOrCreateCart(userId);
        List<CartItem> items = cartItemRepository.findByCartIdWithDetails(cart.getId());

        List<CartItemResponse> cartItems = items.stream().map(CartItemResponse::from).toList();
        int totalProductPrice = cartItems.stream().mapToInt(i -> i.price() * i.quantity()).sum();
        int totalDeliveryFee = cartItems.isEmpty() ? 0 : DELIVERY_FEE;
        return new CartResponse(cartItems, totalProductPrice, totalDeliveryFee, totalProductPrice + totalDeliveryFee);
    }

    @Transactional
    public AddCartItemResponse addCartItem(Long userId, AddCartItemRequest request) {
        if (request.quantity() < 1) {
            throw new BusinessException(ErrorCode.INVALID_QUANTITY);
        }
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));
        ProductOption option = productOptionRepository.findById(request.optionId())
                .filter(o -> o.getProduct().getId().equals(product.getId()))
                .orElseThrow(() -> new BusinessException(ErrorCode.OPTION_NOT_FOUND));

        Cart cart = getOrCreateCart(userId);
        CartItem cartItem = cartItemRepository
                .findByCartIdAndProductIdAndOptionId(cart.getId(), product.getId(), option.getId())
                .map(existing -> {
                    existing.increaseQuantity(request.quantity());
                    return existing;
                })
                .orElseGet(() -> cartItemRepository.save(CartItem.builder()
                        .cart(cart).product(product).option(option).quantity(request.quantity()).build()));

        return new AddCartItemResponse(cartItem.getId());
    }

    @Transactional
    public UpdateCartItemResponse updateCartItemQuantity(Long userId, Long cartItemId, int quantity) {
        if (quantity < 1) {
            throw new BusinessException(ErrorCode.INVALID_QUANTITY);
        }
        CartItem cartItem = getOwnedCartItem(userId, cartItemId);
        cartItem.updateQuantity(quantity);

        int unitPrice = cartItem.getProduct().getDiscountedPrice() + cartItem.getOption().getAdditionalPrice();
        return new UpdateCartItemResponse(cartItem.getId(), cartItem.getQuantity(), unitPrice * cartItem.getQuantity());
    }

    @Transactional
    public SuccessResponse deleteCartItem(Long userId, Long cartItemId) {
        CartItem cartItem = getOwnedCartItem(userId, cartItemId);
        cartItemRepository.delete(cartItem);
        return SuccessResponse.ok();
    }

    @Transactional
    public SuccessResponse deleteCartItems(Long userId, List<Long> cartItemIds) {
        Cart cart = getOrCreateCart(userId);
        cartItemRepository.deleteByCartIdAndIdIn(cart.getId(), cartItemIds);
        return SuccessResponse.ok();
    }

    @Transactional
    public SuccessResponse clearCart(Long userId) {
        Cart cart = getOrCreateCart(userId);
        cartItemRepository.deleteByCartId(cart.getId());
        return SuccessResponse.ok();
    }

    public CalculateCartResponse calculate(Long userId, List<Long> cartItemIds) {
        List<CartItem> items = cartItemRepository.findAllById(cartItemIds).stream()
                .filter(item -> item.getCart().getUser().getId().equals(userId))
                .toList();
        int totalPrice = items.stream()
                .mapToInt(i -> (i.getProduct().getDiscountedPrice() + i.getOption().getAdditionalPrice()) * i.getQuantity())
                .sum();
        return new CalculateCartResponse(totalPrice);
    }

    private CartItem getOwnedCartItem(Long userId, Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CART_ITEM_NOT_FOUND));
        if (!cartItem.getCart().getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.CART_ITEM_NOT_FOUND);
        }
        return cartItem;
    }
}
