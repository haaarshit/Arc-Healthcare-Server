package com.example.HealthArc.SupportClasses.Appointment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class UpdateAppointmentRequest {
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
}
