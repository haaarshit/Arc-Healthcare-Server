package com.example.HealthArc.SupportClasses.Doctor;

import com.example.HealthArc.Models.Appointment;
import com.example.HealthArc.Models.Patient;
import com.example.HealthArc.SupportClasses.Patient.PatientResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientProfileForDoctor {
    private PatientResponse patientInfo;
    List<Appointment> appointmentList;

}
