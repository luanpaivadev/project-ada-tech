package com.luanpaiva.order_service.adapters.out.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RestaurantDTO {

    private UUID id;
    private String name;
    private RestaurantDTO.AddressDTO address;

    @Getter
    @Setter
    public static class AddressDTO {

        private String zipCode;
        private String street;
        private String complement;
        private String unit;
        private String neighborhood;
        private String city;
        private String stateCode;
        private String state;
    }
}
