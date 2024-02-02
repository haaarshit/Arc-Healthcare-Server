package com.example.HealthArc.Controllers.Protected;

import com.example.HealthArc.Security.JwtService;
import com.example.HealthArc.Services.Doctor.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth/doctor")
public class ProtectedDoctorController {
    @Autowired
    JwtService service;
    @Autowired
    DoctorService doctorService;

    // ****************************  update doctor data ********************************//
    @GetMapping("/profile")
    ResponseEntity<?> getDoctorProfile(@RequestHeader("Authorization") String header){
        String username =  service.extractUsername(header.substring(7));
        return ResponseEntity.ok().body("Authenticated your email is "+username);
    }

}
