package com.example.HealthArc.SupportClasses;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    private String street;

    private String city;

    private String state;

    private String zipCode;

}
