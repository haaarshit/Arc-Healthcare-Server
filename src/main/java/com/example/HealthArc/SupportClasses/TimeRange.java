package com.example.HealthArc.SupportClasses;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class TimeRange {
    private String startTime;

    private String endTime;

}
