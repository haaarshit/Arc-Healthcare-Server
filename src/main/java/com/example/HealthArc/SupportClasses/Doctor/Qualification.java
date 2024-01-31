package com.example.HealthArc.SupportClasses.Doctor;

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
public class Qualification {
    private String degree;

    private String university;

    private Integer year;
}
