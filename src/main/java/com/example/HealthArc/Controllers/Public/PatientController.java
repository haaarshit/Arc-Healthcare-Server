package com.example.HealthArc.Controllers.Public;

import com.example.HealthArc.Models.Patient;
import com.example.HealthArc.Services.Doctor.DoctorService;
import com.example.HealthArc.Services.Patient.PatientService;
import com.example.HealthArc.SupportClasses.UserRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/public/patient")
public class PatientController {
    @Autowired
    PatientService patientService;
    // patient signup
    @PostMapping("/add")
    ResponseEntity<?> addPatient(@RequestBody Patient patient, HttpServletResponse response){
        return patientService.addPatient(patient,response);
    }
    // patient login
    @PostMapping("/login")
    ResponseEntity<?> loginPatient(@RequestBody UserRequest request,HttpServletResponse response){
        return patientService.loginPatient(request,response);
    }
    // get patient by id

}