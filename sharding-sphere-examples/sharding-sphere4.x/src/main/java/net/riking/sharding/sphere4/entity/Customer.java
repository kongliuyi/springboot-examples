package net.riking.sharding.sphere4.entity;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Customer {

    private Long id;

    private String phone;

    private String address;


}
