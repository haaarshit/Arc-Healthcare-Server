package com.example.HealthArc.SupportClasses.Doctor;

import com.example.HealthArc.Models.Appointment;
import com.example.HealthArc.Models.AppointmentRequest;
import com.example.HealthArc.Models.Doctor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDashboard {
    private DoctorResponse doctorInfo;
    private List<Appointment> appointmentList;
    private List<AppointmentRequest> appointmentRequestList;
}

