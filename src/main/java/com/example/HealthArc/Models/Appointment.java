package com.example.HealthArc.Models;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "appointments")
public class Appointment {
    @Id
    private String id;
    private String patientId;
    private String doctorId;
    private Date appointmentDate;
    private String startTime;
    private String endTime;
    private String symptoms;
    private String diagnosis;
    private String prescription;
    private String status;
    private String notes;

    private Date followUpDate;
    private String followUpInstructions;
    private String appointmentType;
    private List<String> prescribedMedications;
    private List<String> labTestRequests;
    private Boolean patientConsent;
    private Date created_at;
    private Date updated_at;

}