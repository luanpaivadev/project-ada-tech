package com.luanpaiva.order_service.service;

import com.luanpaiva.order_service.adapters.in.model.OrderInput;
import com.luanpaiva.order_service.adapters.in.model.OrderItemInput;
import com.luanpaiva.order_service.adapters.out.model.OrderDTO;
import com.luanpaiva.order_service.adapters.out.model.ProductDTO;
import com.luanpaiva.order_service.adapters.out.repository.model.DeliveryAddress;
import com.luanpaiva.order_service.adapters.out.repository.model.PaymentMethod;
import com.luanpaiva.order_service.core.model.Order;
import com.luanpaiva.order_service.core.model.OrderItem;
import com.luanpaiva.order_service.core.model.StatusOrder;
import com.luanpaiva.order_service.core.ports.out.AuthServicePort;
import com.luanpaiva.order_service.core.ports.out.CacheServicePort;
import com.luanpaiva.order_service.core.ports.out.OrderRepositoryPort;
import com.luanpaiva.order_service.core.ports.out.ProductServicePort;
import com.luanpaiva.order_service.core.ports.out.SendMessagePort;
import com.luanpaiva.order_service.core.service.OrderService;
import com.luanpaiva.order_service.core.utils.Queues;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RequiredArgsConstructor
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private AuthServicePort authServicePort;

    @Mock
    private ProductServicePort productServicePort;

    @Mock
    private SendMessagePort sendMessagePort;

    @Mock
    private OrderRepositoryPort orderRepositoryPort;

    @Mock
    private CacheServicePort<String, Object> cacheServicePort;

    private OrderService orderService;

    private UUID productId;
    private ProductDTO productDTO;
    private OrderInput orderInput;

    @BeforeEach
    void setUp() {

        orderService = new OrderService(
                orderRepositoryPort,
                productServicePort,
                sendMessagePort,
                authServicePort,
                cacheServicePort
        );

        productId = UUID.fromString("188038d2-a173-46c3-aceb-2f3a8d921369");

        productDTO = ProductDTO.builder()
                .id(productId)
                .name("Produto Teste")
                .description("Descrição")
                .price(BigDecimal.valueOf(50.0))
                .build();

        OrderItemInput itemInput = OrderItemInput.builder()
                .productId(productId)
                .quantity(2)
                .build();

        DeliveryAddress address = new DeliveryAddress();
        address.setCity("São Paulo");
        address.setStreetAddress("Rua A, 123");

        orderInput = OrderInput.builder()
                .items(List.of(itemInput))
                .address(address)
                .paymentMethod(PaymentMethod.CREDIT_CARD)
                .build();
    }

    @Test
    void shouldCreateNewOrderSuccessfully() {

        String customerEmail = "cliente@teste.com";
        String tokenMock = "fake-token";

        when(authServicePort.getInternalToken("order-service-token"))
                .thenReturn(tokenMock);

        when(productServicePort.checkProductInventory(productId, 2, tokenMock))
                .thenReturn(productDTO);

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        when(orderRepositoryPort.createOrUpdateOrder(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Order result = orderService.createNewOrder(orderInput, customerEmail);

        assertNotNull(result);
        assertEquals(customerEmail, result.getCustomerEmail());
        assertEquals(PaymentMethod.CREDIT_CARD, result.getPaymentMethod());
        assertEquals(StatusOrder.PAYMENT_PENDING, result.getStatus());
        assertEquals(1, result.getItems().size());
        assertEquals(BigDecimal.valueOf(100.0), result.getTotalAmount());

        OrderItem createdItem = result.getItems().getFirst();
        assertEquals(productId, createdItem.getProduct().getProductId());
        assertEquals(2, createdItem.getQuantity());
        assertEquals(BigDecimal.valueOf(100.0), createdItem.getAmount());

        verify(authServicePort).getInternalToken("order-service-token");
        verify(productServicePort).checkProductInventory(productId, 2, tokenMock);
        verify(sendMessagePort).send(eq(Queues.NOTIFICATION_STATUS_ORDER_QUEUE), any(Order.class), eq(OrderDTO.class));
        verify(orderRepositoryPort).createOrUpdateOrder(orderCaptor.capture());

        Order savedOrder = orderCaptor.getValue();
        assertEquals(result.getTotalAmount(), savedOrder.getTotalAmount());
    }

}
