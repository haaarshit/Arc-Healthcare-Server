package com.example.HealthArc.Services.Doctor;

import com.example.HealthArc.Models.Doctor.Doctor;
import com.example.HealthArc.Repository.Doctor.DoctorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DoctorService {
    DoctorRepository doctorRepository;

    ResponseEntity<String> addDoctor(Doctor doctor){
        Optional<Doctor> isAlreadyExist = doctorRepository.findByEmail(doctor.getEmail());
        if(isAlreadyExist.isPresent()){
            return new ResponseEntity<>("User Already exist with username", HttpStatus.CONFLICT);
        }
        // ToDo => add one more check to find if any Patient exist with the username or not
        doctorRepository.save(doctor);

        // dummy response
        return new ResponseEntity<>("created successfully",HttpStatus.OK);
    }
}
