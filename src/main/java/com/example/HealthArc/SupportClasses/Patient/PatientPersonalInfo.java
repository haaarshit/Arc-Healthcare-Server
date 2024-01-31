package com.example.HealthArc.SupportClasses.Patient;

import com.example.HealthArc.SupportClasses.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientPersonalInfo {
    private String firstName;
    private String lastName;
    private String gender;
    private Date dateOfBirth;
    private String nationality;
    private Address address;
}
