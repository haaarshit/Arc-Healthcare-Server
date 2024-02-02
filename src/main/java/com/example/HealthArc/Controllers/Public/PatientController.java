package com.example.HealthArc.Controllers.Public;

import com.example.HealthArc.Models.Patient;
import com.example.HealthArc.Services.Patient.PatientService;
import com.example.HealthArc.SupportClasses.UserRequest;
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
    ResponseEntity<?> addPatient(@RequestBody Patient patient){
        return patientService.addPatient(patient);
    }
    // patient login
    @PostMapping("/login")
    ResponseEntity<?> loginPatient(@RequestBody UserRequest request){
        return patientService.loginPatient(request);
    }
    // get patient by id
    // update patient data
}