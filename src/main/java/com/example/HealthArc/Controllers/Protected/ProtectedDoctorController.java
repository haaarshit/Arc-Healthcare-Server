package com.example.HealthArc.Controllers.Protected;

import com.example.HealthArc.Security.JwtService;
import com.example.HealthArc.Services.Doctor.DoctorService;
import com.example.HealthArc.SupportClasses.Doctor.Availability;
import com.example.HealthArc.SupportClasses.Doctor.ProfessionalInfo;
import com.example.HealthArc.SupportClasses.Patient.UpdatePassword;
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

    @PutMapping("/update-password")
    ResponseEntity<?> updateDoctorPassword(@RequestBody UpdatePassword password, @RequestHeader("Authorization") String header){
        return doctorService.updatePassword(password.getOldPassword(),password.getPassword(),header);
    }

    @PutMapping("/update-avatar")
    ResponseEntity<?> updateDoctorAvatar(@RequestBody String avatarBase64, @RequestHeader("Authorization") String header){
        return doctorService.updateAvatar(avatarBase64,header);
    }

    @PutMapping("/update-professionalInfo")
    ResponseEntity<?> updateProfessionalInfo(@RequestBody ProfessionalInfo professionalInfo, @RequestHeader("Authorization") String header){
        return doctorService.updateProfessionalInfo(professionalInfo,header);
    }

    @PutMapping("/update-avaibility")
    ResponseEntity<?> updateAvailability(@RequestBody Availability availability, @RequestHeader("Authorization") String header){
        return doctorService.updateAvailability(availability,header);
    }

}
