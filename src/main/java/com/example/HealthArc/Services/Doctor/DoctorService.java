package com.example.HealthArc.Services.Doctor;

import com.example.HealthArc.Models.Doctor;
import com.example.HealthArc.Repository.Doctor.DoctorRepository;
import com.example.HealthArc.Repository.Patient.PatientRepository;
import com.example.HealthArc.SupportClasses.PrintErrorMessage;
import com.example.HealthArc.SupportClasses.UserRequest;
import org.eclipse.angus.mail.iap.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.util.Optional;

@Service
public class DoctorService {
    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    PatientRepository patientRepository;

    public ResponseEntity<Doctor>  addDoctor(Doctor doctor){
        String email = doctor.getEmail();
        try {
            Optional<Doctor> isAlreadyExist = doctorRepository.findByEmail(email);
            if (isAlreadyExist.isPresent() || patientRepository.findByEmail(email).isPresent()) {
                return new ResponseEntity<>(null, HttpStatus.CONFLICT);
            }

            String avatar = doctor.getAvatar();
            // todo => upload to cloudinary
            String avatarUrl = "";// cloudinary url
            // todo=> encrypt password then store
            doctor.setAvatar(avatarUrl);

            Doctor response = doctorRepository.save(doctor);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        catch (Exception e){
            new PrintErrorMessage(e);
            return ResponseEntity.internalServerError().body(null);
        }

    }

    public ResponseEntity<Doctor> loginDoctor(UserRequest request){
        String mail = request.getEmail();
        String password = request.getPassword();

        try{
            Optional<Doctor> isDoctor = doctorRepository.findByEmail(mail);
            if(isDoctor.isEmpty()){
                return ResponseEntity.badRequest().body(null);
            }
            Doctor doctor = isDoctor.get();
            // todo=> decrypt and compare password
            if(doctor.getPassword().equals(password)){
                 return new ResponseEntity<>(doctor,HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(null,HttpStatus.UNAUTHORIZED);
            }

        }
        catch(Exception e){
            new PrintErrorMessage(e);
            return ResponseEntity.internalServerError().body(null);
        }

    }
}
