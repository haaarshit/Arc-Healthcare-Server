package com.example.HealthArc.Controllers.Protected;

import com.example.HealthArc.Services.Doctor.DoctorService;
import com.example.HealthArc.SupportClasses.Doctor.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth/patient")
public class ProtectedPatientController {

    @Autowired
    DoctorService doctorService;


    // get all patients
    @GetMapping("/profile")
    ResponseEntity<?> getPatientProfile(){
        return ResponseEntity.ok().body("AUTH email ");
    }
    // get patient by id
    // update patient data

    // ************************ Add Review *******************************//
    @PostMapping("/add-review")
    ResponseEntity<?> addDoctorReview(@RequestBody Review review, @RequestParam String id){
        return doctorService.addReview(review,id);
    }
}