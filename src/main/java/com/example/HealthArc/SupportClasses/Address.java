package com.example.HealthArc.SupportClasses;


import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
public class Address {
    private String street;

    private String city;

    private String state;

    private String zipCode;

    private String country;
}
