package com.luanpaiva.order_service.adapters.out.repository.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryAddress {

    private String recipientName;
    private String streetAddress;
    private String addressLine2;
    private String district;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private String phoneNumber;
    private String deliveryInstructions;
}
