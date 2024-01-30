package com.example.HealthArc.Controllers.Public;

import com.example.HealthArc.Models.Doctor;
import com.example.HealthArc.Services.Doctor.DoctorService;
import com.example.HealthArc.SupportClasses.UserRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("public/api/doctor")
public class DoctorController {

//    service
    DoctorService doctorService;

    // *************************** doctor signup *******************************//
    @PostMapping("/add")
    ResponseEntity<Doctor> addDoctor(@RequestBody Doctor doctor){
        return doctorService.addDoctor(doctor);
    }

    //**************************** Doctor login ********************************//
    @GetMapping("/login")
    ResponseEntity<String> loginDoctor(@RequestBody UserRequest userRequest) {
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    // ****************************  get all doctors    ***********************************//


    // ****************************  get doctor by id   **********************************//


}
