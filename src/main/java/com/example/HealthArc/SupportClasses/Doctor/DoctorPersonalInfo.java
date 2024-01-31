package com.example.HealthArc.SupportClasses.Doctor;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class DoctorPersonalInfo {
    private String firstName;

    private String lastName;

    private String gender;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dateOfBirth;

//    private String nationality;


}
