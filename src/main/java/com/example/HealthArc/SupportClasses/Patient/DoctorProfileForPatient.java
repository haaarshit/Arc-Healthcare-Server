package com.example.HealthArc.SupportClasses.Patient;

import com.example.HealthArc.Models.Appointment;
import com.example.HealthArc.SupportClasses.Doctor.DoctorResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorProfileForPatient {
    private DoctorResponse doctorInfo;
    private List<Appointment> appointmentList;
}
