package com.example.HealthArc.Services.Doctor;

import com.example.HealthArc.Models.Doctor;
import com.example.HealthArc.Repository.Doctor.DoctorRepository;
import com.example.HealthArc.Repository.Patient.PatientRepository;
import com.example.HealthArc.SupportClasses.DateUtil;
import com.example.HealthArc.SupportClasses.Doctor.DoctorListResponse;
import com.example.HealthArc.SupportClasses.Doctor.DoctorResponse;
import com.example.HealthArc.SupportClasses.PrintErrorMessage;
import com.example.HealthArc.SupportClasses.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {
    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    DateUtil dateUtil;
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    // ************************ Signup ***************************
    public ResponseEntity<?>  addDoctor(Doctor doctor){
        String email = doctor.getEmail();
        HttpHeaders header = new HttpHeaders();
        try {
            Optional<Doctor> isAlreadyExist = doctorRepository.findByEmail(email);
            if (isAlreadyExist.isPresent() || patientRepository.findByEmail(email).isPresent()) {
                return ResponseEntity.badRequest().body("User Already Exist");
            }
            // convert string to date

            String avatar = doctor.getAvatar();
            // todo => upload to cloudinary
            String avatarUrl = "";// cloudinary url
            String hashedPassword = passwordEncoder.encode(doctor.getPassword());
            doctor.setPassword(hashedPassword);
            doctor.setAvatar(avatarUrl);

            Doctor saved = doctorRepository.save(doctor);
            DoctorResponse response = new DoctorResponse().returnResponse(saved);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        catch (Exception e){
            new PrintErrorMessage(e);
            return ResponseEntity.badRequest().body("Got some problem while registering the user");
        }
    }

    // ********************** Login ****************************
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
                DoctorResponse response = new DoctorResponse().returnResponse(doctor);
                 return new ResponseEntity<>(response,HttpStatus.OK);
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

    // ******************** getAllDoctors ***********************
    public ResponseEntity<?> getAllDoctors(){

        // todo => write custom query to fetch only necessary data from all document
        try{
        List<Doctor> doctors = doctorRepository.findAll();
        List<DoctorListResponse> response = new DoctorListResponse().returnResponse(doctors);
        return ResponseEntity.ok().body(response);
        }
        catch (Exception e){
            new PrintErrorMessage(e);
            return ResponseEntity.internalServerError().body("Internal Server Error");
        }
    }

    // ******************** getDoctorById ***********************
    public ResponseEntity<?> getDoctorById(String id){
        try{
            Optional<Doctor> doctor = doctorRepository.findById(id);
            if(doctor.isEmpty()){
                return ResponseEntity.badRequest().body("Doctor not found");
            }
            DoctorResponse response = new DoctorResponse().returnResponse(doctor.get());

            return ResponseEntity.ok().body(response);
        }
        catch (Exception e){
            new PrintErrorMessage(e);
            return ResponseEntity.internalServerError().body("Internal Server Error");
        }
    }
}
