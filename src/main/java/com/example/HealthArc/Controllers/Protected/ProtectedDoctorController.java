package com.example.HealthArc.Controllers.Protected;

import com.example.HealthArc.Models.Appointment;
import com.example.HealthArc.Security.JwtService;
import com.example.HealthArc.Services.Doctor.DoctorService;
import com.example.HealthArc.SupportClasses.Appointment.UpdateAppointmentRequest;
import com.example.HealthArc.SupportClasses.Doctor.Availability;
import com.example.HealthArc.SupportClasses.Doctor.ProfessionalInfo;
import com.example.HealthArc.SupportClasses.Patient.UpdatePassword;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth/doctor")
@CrossOrigin(origins = "http://localhost:3000")
public class ProtectedDoctorController {
    @Autowired
    JwtService service;
    @Autowired
    DoctorService doctorService;
    /*
    Secured routes
    Only authenticated user with Authorization token can access these routes
    */

    // ****************************  update doctor data ********************************//

    @PutMapping("/update-password")
    ResponseEntity<?> updateDoctorPassword(@RequestBody UpdatePassword password, @RequestHeader("Authorization") String header, HttpServletResponse response){
        return doctorService.updatePassword(password.getOldPassword(),password.getPassword(),header,response);
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

    // ******************** handle appointment
    // create appointment
    @PostMapping("/create-appointment")
    ResponseEntity<?> createAppointment(@RequestBody Appointment appointment,@RequestParam("id") String appointReqId){
        return doctorService.createAppointment(appointment,appointReqId);
    }

    // get doctor dashboard
    @GetMapping("/dashboard")
    ResponseEntity<?> getAllAppointments(@RequestHeader("Authorization") String header){
        return doctorService.getDoctorDashboard(header);
    }

    // returns the patient profile to doctor
    @GetMapping("/profile/{id}")
    ResponseEntity<?> getPatientProfile(@PathVariable("id") String patientId,@RequestHeader("Authorization") String header){
        return doctorService.getPatientProfileForDoctor(patientId,header);
    }

    // doctor can update the appointment properties
    @PutMapping("/appointment/update/{id}")
    ResponseEntity<?> updateAppointment(@PathVariable("id") String id, @RequestBody UpdateAppointmentRequest updateAppoinrmentRequest){
        return doctorService.updateAppointment(id,updateAppoinrmentRequest);
    }
}
