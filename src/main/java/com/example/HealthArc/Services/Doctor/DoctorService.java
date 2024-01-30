package com.example.HealthArc.Services.Doctor;

import com.example.HealthArc.Models.Doctor;
import com.example.HealthArc.Repository.Doctor.DoctorRepository;
import com.example.HealthArc.Repository.Patient.PatientRepository;
import com.example.HealthArc.SupportClasses.PrintErrorMessage;
import com.example.HealthArc.SupportClasses.UserRequest;
import jakarta.mail.Header;
import org.eclipse.angus.mail.iap.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.util.Optional;

@Service
public class DoctorService {
    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    PatientRepository patientRepository;
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public ResponseEntity<?>  addDoctor(Doctor doctor){
        String email = doctor.getEmail();
        HttpHeaders header = new HttpHeaders();
        try {
            Optional<Doctor> isAlreadyExist = doctorRepository.findByEmail(email);
            if (isAlreadyExist.isPresent() || patientRepository.findByEmail(email).isPresent()) {
                return ResponseEntity.badRequest().body("User Already Exist");
            }

            String avatar = doctor.getAvatar();
            // todo => upload to cloudinary
            String avatarUrl = "";// cloudinary url
            // todo=> encrypt password then store
            String hashedPassword = passwordEncoder.encode(doctor.getPassword());
            doctor.setPassword(hashedPassword);
            doctor.setAvatar(avatarUrl);

            Doctor response = doctorRepository.save(doctor);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        catch (Exception e){
            new PrintErrorMessage(e);
            return ResponseEntity.badRequest().body("Got some problem while registering the user");
        }

    }

    public ResponseEntity<?> loginDoctor(UserRequest request){
        String mail = request.getEmail();
        String password = request.getPassword();

        try{
            Optional<Doctor> isDoctor = doctorRepository.findByEmail(mail);
            if(isDoctor.isEmpty()){
                return ResponseEntity.badRequest().body(null);
            }
            Doctor doctor = isDoctor.get();
            // todo=> decrypt and compare password
            if(passwordEncoder.matches(password,doctor.getPassword())){
                 return new ResponseEntity<>(doctor,HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>("Invalid Credentials",HttpStatus.UNAUTHORIZED);
            }

        }
        catch(Exception e){
            new PrintErrorMessage(e);
            return ResponseEntity.internalServerError().body("Got some problem while authenticating");
        }

    }
}
