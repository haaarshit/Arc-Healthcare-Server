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
public class Review {

    private String patientName;

    private Integer rating;

    private String comment;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;

}