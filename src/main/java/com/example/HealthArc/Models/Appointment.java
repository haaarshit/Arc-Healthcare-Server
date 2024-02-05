package com.example.HealthArc.Models;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Reference;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.Date;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "appointments")
public class Appointment {
    @Id
    private String id;
    private String patientId;
    private String doctorId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date appointmentDate;
    private String startTime;
    private String endTime;
    private String symptoms;
    private String diagnosis;
    private String prescription;
    private String status;
    private String notes;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date followUpDate;
    private String followUpInstructions;
    private String appointmentType;
    private List<String> prescribedMedications;
    private List<String> labTestRequests;
    private Boolean patientConsent;
    @CreatedDate
    private Date createdAt = new Date();
    @LastModifiedDate
    private Date updatedAt = new Date();

}