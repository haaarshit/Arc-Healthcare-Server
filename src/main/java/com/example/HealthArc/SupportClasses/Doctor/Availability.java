package com.example.HealthArc.SupportClasses.Doctor;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Availability {

    private boolean isAvailable;

    private List<String> availableDays;

    private TimeRange availableTime;
}
