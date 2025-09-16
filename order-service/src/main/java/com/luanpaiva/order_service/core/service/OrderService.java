package com.luanpaiva.order_service.core.service;

import com.luanpaiva.order_service.adapters.in.model.OrderInput;
import com.luanpaiva.order_service.adapters.in.model.PaymentWebhookPayload;
import com.luanpaiva.order_service.adapters.out.model.OrderDTO;
import com.luanpaiva.order_service.adapters.out.model.ProductDTO;
import com.luanpaiva.order_service.core.exception.BadRequestException;
import com.luanpaiva.order_service.core.exception.InternalServerErrorException;
import com.luanpaiva.order_service.core.exception.NotFountException;
import com.luanpaiva.order_service.core.model.Order;
import com.luanpaiva.order_service.core.model.OrderItem;
import com.luanpaiva.order_service.core.model.Product;
import com.luanpaiva.order_service.core.model.StatusOrder;
import com.luanpaiva.order_service.core.ports.in.OrderServicePort;
import com.luanpaiva.order_service.core.ports.out.AuthServicePort;
import com.luanpaiva.order_service.core.ports.out.OrderRepositoryPort;
import com.luanpaiva.order_service.core.ports.out.ProductServicePort;
import com.luanpaiva.order_service.core.ports.out.SendMessagePort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class OrderService implements OrderServicePort {

    private static final String NOTIFICATION_STATUS_ORDER = "notification.status_order";
    private static final String PRODUCT_PAYMENT_APPROVED_QUEUE = "product.payment_approved";

    private final OrderRepositoryPort orderRepositoryPort;
    private final ProductServicePort productServicePort;
    private final SendMessagePort sendMessagePort;
    private final AuthServicePort authServicePort;

    public OrderService(OrderRepositoryPort orderRepositoryPort, ProductServicePort productServicePort,
                        SendMessagePort sendMessagePort, AuthServicePort authServicePort) {
        this.orderRepositoryPort = orderRepositoryPort;
        this.productServicePort = productServicePort;
        this.sendMessagePort = sendMessagePort;
        this.authServicePort = authServicePort;
    }

    @Override
    public Order findById(UUID orderId) {
        return orderRepositoryPort.findById(orderId)
                .orElseThrow(() -> new NotFountException("Order not found."));
    }

    @Override
    public Order findById(UUID orderId, String customerEmail) {
        Order order = findById(orderId);
        if (Objects.equals(order.getCustomerEmail(), customerEmail)) return order;
        throw new BadRequestException("It is not possible to view another customer's order.");
    }

    @Override
    public Order createNewOrder(OrderInput orderInput, String customerEmail) {

        List<OrderItem> items = new ArrayList<>();
        Order order = new Order(UUID.randomUUID());
        String accessToken = authServicePort.getInternalToken("order-service-token");

        orderInput.getItems().forEach(item -> {
            ProductDTO product = productServicePort.checkProductInventory(item.getProductId(), item.getQuantity(), accessToken);
            BigDecimal amount = product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            OrderItem orderItem = OrderItem.builder()
                    .product(Product.builder()
                            .productId(product.getId())
                            .description(product.getDescription())
                            .name(product.getName())
                            .price(product.getPrice())
                            .build())
                    .quantity(item.getQuantity())
                    .amount(amount)
                    .order(order)
                    .build();
            items.add(orderItem);
        });

        BigDecimal totalAmount = items.stream().map(OrderItem::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setCustomerEmail(customerEmail);
        order.setItems(items);
        order.setAddress(orderInput.getAddress());
        order.setTotalAmount(totalAmount);
        order.setPaymentMethod(orderInput.getPaymentMethod());
        order.setStatus(StatusOrder.PAYMENT_PENDING);
        sendMessagePort.send(NOTIFICATION_STATUS_ORDER, StatusOrder.PAYMENT_PENDING, null);
        return orderRepositoryPort.createOrUpdateOrder(order);
    }

    @Override
    public void validatePayment(PaymentWebhookPayload payload) {
        try {
            Order order = findById(payload.getOrderId());
            switch (payload.getStatusPayment()) {
                case APPROVED -> {
                    order.setStatus(StatusOrder.PAYMENT_APPROVED);
                    sendMessagePort.send(PRODUCT_PAYMENT_APPROVED_QUEUE, order, OrderDTO.class);
                    sendMessagePort.send(NOTIFICATION_STATUS_ORDER, StatusOrder.PAYMENT_APPROVED, null);
                }
                case FAILURE -> {
                    order.setStatus(StatusOrder.PAYMENT_FAILURE);
                    sendMessagePort.send(NOTIFICATION_STATUS_ORDER, StatusOrder.PAYMENT_FAILURE, null);
                }
                case REJECTED -> {
                    order.setStatus(StatusOrder.PAYMENT_REJECTED);
                    sendMessagePort.send(NOTIFICATION_STATUS_ORDER, StatusOrder.PAYMENT_REJECTED, null);
                }
            }
            order.setPaymentId(payload.getPaymentId());
            order.setPaymentMessage(payload.getMessage());
            orderRepositoryPort.createOrUpdateOrder(order);
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    @Override
    public void updateStatusOrder(UUID orderId, StatusOrder statusOrder) {
        orderRepositoryPort.updateStatusOrder(orderId, statusOrder);
    }

    @Override
    public Page<OrderDTO> findOrdersInSeparation(Pageable pageable) {
        return orderRepositoryPort.findOrdersInSeparation(pageable);
    }
}
