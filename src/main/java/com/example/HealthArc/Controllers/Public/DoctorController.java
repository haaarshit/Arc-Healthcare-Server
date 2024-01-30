package com.example.HealthArc.Controllers.Public;

import com.example.HealthArc.Models.Doctor;
import com.example.HealthArc.Services.Doctor.DoctorService;
import com.example.HealthArc.SupportClasses.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/public/doctor")
public class DoctorController {

//    service

    @Autowired
    DoctorService doctorService;

    // *************************** doctor signup *******************************//
    @PostMapping("/add")
    ResponseEntity<?> addDoctor(@RequestBody Doctor doctor){
        return doctorService.addDoctor(doctor);
    }

    //**************************** Doctor login ********************************//
    @GetMapping("/login")
    ResponseEntity<?> loginDoctor(@RequestBody UserRequest userRequest) {
        return doctorService.loginDoctor(userRequest);
    }

    // ****************************  get all doctors    ***********************************//


    // ****************************  get doctor by id   **********************************//


}
