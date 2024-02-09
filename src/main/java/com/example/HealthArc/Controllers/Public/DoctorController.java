package com.example.HealthArc.Controllers.Public;

import com.example.HealthArc.Models.Doctor;
import com.example.HealthArc.Services.Doctor.DoctorService;
import com.example.HealthArc.SupportClasses.UserRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    @PostMapping("/login")
    ResponseEntity<?> loginDoctor(@RequestBody UserRequest userRequest) {
        return doctorService.loginDoctor(userRequest);
    }

    // ****************************  get all doctors    ***********************************//
    @GetMapping("/all")
    ResponseEntity<?> getAllDoctor() {
        return doctorService.getAllDoctors();
    }
    @GetMapping("/city")
    ResponseEntity<?> getDoctorByCity(@RequestParam("cityname") String city) {
        return doctorService.getDoctorByCity(city);
    }


    // ****************************  get doctor by id   **********************************//
//    @GetMapping("/{id}")
//    ResponseEntity<?> getDoctorById(@PathVariable("id") String id){
//        return doctorService.getDoctorById(id);
//    }


}
