package com.example.HealthArc.SupportClasses.Doctor;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

@Component
public class TimeRange {
    private String startTime;

    private String endTime;

}
