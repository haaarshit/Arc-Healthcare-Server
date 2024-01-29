package com.example.HealthArc.Controllers;

import com.example.HealthArc.Models.Doctor;
import com.example.HealthArc.SupportClasses.UserRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("api/v1/doctor")
public class DoctorController {

    // *************************** doctor signup *******************************//
    @PostMapping("/add")
    ResponseEntity<String> addDoctor(@RequestBody Doctor doctor){
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    //**************************** Doctor login ********************************//
    @GetMapping("/login")
    ResponseEntity<String> loginDoctor(@RequestBody UserRequest userRequest) {
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    // ****************************  get all doctors    ***********************************//

    // ****************************  add doctor         ***********************************//

    // ****************************  get doctor by id   **********************************//

    // ****************************  update doctor data ********************************//

}
