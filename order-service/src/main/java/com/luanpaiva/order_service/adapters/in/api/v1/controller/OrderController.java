package com.luanpaiva.order_service.adapters.in.api.v1.controller;

import com.luanpaiva.order_service.adapters.in.model.OrderInput;
import com.luanpaiva.order_service.adapters.in.model.PaymentWebhookPayload;
import com.luanpaiva.order_service.adapters.out.model.OrderDTO;
import com.luanpaiva.order_service.core.exception.BadRequestException;
import com.luanpaiva.order_service.core.model.Order;
import com.luanpaiva.order_service.core.model.StatusOrder;
import com.luanpaiva.order_service.core.ports.in.OrderServicePort;
import com.luanpaiva.order_service.core.ports.out.PaymentGatewayServicePort;
import com.luanpaiva.order_service.core.ports.out.SendMessagePort;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.isBlank;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/orders")
public class OrderController {

    private static final String X_IDEMPOTENCY_KEY_HEADER = "x-idempotency-key";
    private static final String CLIENT_SECRET_HEADER = "client-secret";

    private final OrderServicePort orderServicePort;
    private final ModelMapper mapper;
    private final SendMessagePort sendMessagePort;
    private final PaymentGatewayServicePort paymentGatewayServicePort;

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> findById(@PathVariable UUID orderId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String customerEmail = (String) authentication.getPrincipal();
        Order order = orderServicePort.findById(orderId, customerEmail);
        OrderDTO orderDTO = mapper.map(order, OrderDTO.class);
        return ResponseEntity.ok(orderDTO);
    }

    @GetMapping("/internal/{orderId}")
    public ResponseEntity<OrderDTO> internalFindOrderById(@PathVariable UUID orderId) {
        Order order = orderServicePort.findById(orderId);
        OrderDTO orderDTO = mapper.map(order, OrderDTO.class);
        return ResponseEntity.ok(orderDTO);
    }

    @PostMapping
    public ResponseEntity<OrderDTO> createNewOrder(@RequestBody OrderInput orderInput) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String customerEmail = (String) authentication.getPrincipal();
        Order order = orderServicePort.createNewOrder(orderInput, customerEmail);
        OrderDTO orderDTO = mapper.map(order, OrderDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderDTO);
    }

    @GetMapping("/in-separation")
    public ResponseEntity<Page<OrderDTO>> findOrdersInSeparation(Pageable pageable) {
        Page<OrderDTO> orders = orderServicePort.findOrdersInSeparation(pageable);
        return ResponseEntity.ok().body(orders);
    }

    @PutMapping("/update-status/{orderId}")
    public ResponseEntity<Void> updateOrderInDelivery(@PathVariable UUID orderId,
                                                      @RequestParam StatusOrder statusOrder) {
        orderServicePort.updateStatusOrder(orderId, statusOrder);
        sendMessagePort.send("notification.status_order", statusOrder, null);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/webhook/payment")
    public ResponseEntity<Void> paymentWebhook(@RequestBody PaymentWebhookPayload payload,
                                               HttpServletRequest request) {
        String key = request.getHeader(X_IDEMPOTENCY_KEY_HEADER);
        if (isBlank(key)) {
            throw new BadRequestException("x-idempotency-key not found");
        }
        paymentGatewayServicePort.validateClientSecret(request.getHeader(CLIENT_SECRET_HEADER));
        orderServicePort.validateXIdempotencyKey(key);
        orderServicePort.validatePayment(payload, key);
        return ResponseEntity.noContent().build();
    }
}
