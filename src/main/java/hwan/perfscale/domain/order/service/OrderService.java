package hwan.perfscale.domain.order.service;

import hwan.perfscale.domain.cart.entity.Cart;
import hwan.perfscale.domain.cart.entity.CartItem;
import hwan.perfscale.domain.cart.repository.CartItemRepository;
import hwan.perfscale.domain.cart.repository.CartRepository;
import hwan.perfscale.domain.order.dto.*;
import hwan.perfscale.domain.order.entity.Order;
import hwan.perfscale.domain.order.entity.OrderItem;
import hwan.perfscale.domain.order.repository.OrderRepository;
import hwan.perfscale.domain.product.entity.ProductOption;
import hwan.perfscale.domain.product.entity.ProductStatus;
import hwan.perfscale.domain.user.entity.User;
import hwan.perfscale.domain.user.repository.UserRepository;
import hwan.perfscale.global.exception.BusinessException;
import hwan.perfscale.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    @Transactional
    public OrderResponse createOrder(Long userId, CreateOrderRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CART_EMPTY));

        List<CartItem> targetItems = resolveTargetItems(cart, request);
        if (targetItems.isEmpty()) {
            throw new BusinessException(ErrorCode.CART_EMPTY);
        }

        // 배송정보는 명세서 요청 바디에 별도 필드가 없어 유저 프로필 기본값으로 채움
        Order order = Order.builder()
                .user(user)
                .receiverName(user.getName())
                .zipCode(null)
                .address(user.getDefaultAddress())
                .phone(user.getPhone())
                .build();

        for (CartItem cartItem : targetItems) {
            ProductOption option = cartItem.getOption();
            if (option.getStatus() != ProductStatus.AVAILABLE) {
                throw new BusinessException(ErrorCode.PRODUCT_SUSPENDED,
                        cartItem.getProduct().getProductName() + "은(는) 현재 구매할 수 없는 상태입니다.");
            }
            if (option.getStockQuantity() < cartItem.getQuantity()) {
                throw new BusinessException(ErrorCode.OUT_OF_STOCK,
                        cartItem.getProduct().getProductName() + "의 재고가 부족합니다.");
            }
            option.decreaseStock(cartItem.getQuantity());
            order.addItem(OrderItem.builder()
                    .order(order)
                    .product(cartItem.getProduct())
                    .option(option)
                    .quantity(cartItem.getQuantity())
                    .build());
        }

        orderRepository.save(order);
        return OrderResponse.from(order);
    }

    public OrderListResponse getOrders(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Order> orders = orderRepository.findByUserId(userId, pageable);
        return new OrderListResponse(orders.map(OrderListItemResponse::from).getContent());
    }

    public OrderDetailResponse getOrderDetail(Long userId, String orderNo) {
        Order order = getOwnedOrder(userId, orderNo);
        return OrderDetailResponse.from(order);
    }

    @Transactional
    public CancelOrderResponse cancelOrder(Long userId, String orderNo) {
        Order order = getOwnedOrder(userId, orderNo);
        try {
            order.cancel();
        } catch (IllegalStateException e) {
            throw new BusinessException(ErrorCode.ORDER_NOT_CANCELLABLE, e.getMessage());
        }
        for (OrderItem item : order.getItems()) {
            if (item.getOption() != null) {
                item.getOption().increaseStock(item.getQuantity());
            }
        }
        return CancelOrderResponse.from(order);
    }

    private List<CartItem> resolveTargetItems(Cart cart, CreateOrderRequest request) {
        if (request.checkoutType() == CheckoutType.ALL) {
            return cartItemRepository.findByCartIdWithDetails(cart.getId());
        }
        List<Long> ids = request.cartItemIds() != null ? request.cartItemIds() : List.of();
        return cartItemRepository.findAllById(ids).stream()
                .filter(item -> item.getCart().getId().equals(cart.getId()))
                .toList();
    }

    private Order getOwnedOrder(Long userId, String orderNo) {
        Order order = orderRepository.findByOrderNoWithItems(orderNo)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));
        if (!order.isOwner(userId)) {
            throw new BusinessException(ErrorCode.ORDER_FORBIDDEN);
        }
        return order;
    }
}
