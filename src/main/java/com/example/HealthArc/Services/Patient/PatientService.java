package com.example.HealthArc.Services.Patient;

import com.example.HealthArc.Models.Doctor;
import com.example.HealthArc.Models.Patient;
import com.example.HealthArc.Repository.Doctor.DoctorRepository;
import com.example.HealthArc.Repository.Patient.PatientRepository;
import com.example.HealthArc.Security.JwtService;
import com.example.HealthArc.Security.SecurityConfig;
import com.example.HealthArc.Security.UserDetailServiceConfig.PatientUserDetailService;
import com.example.HealthArc.SupportClasses.Patient.PatientResponse;
import com.example.HealthArc.SupportClasses.PrintErrorMessage;
import com.example.HealthArc.SupportClasses.UserRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Optional;


@Service
public class PatientService {
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private PatientUserDetailService patientUserDetailService;

    @Autowired
    JwtService jwtService;
    @Autowired
    SecurityConfig securityConfig;
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // ************************ Signup ***************************
    public ResponseEntity<?> addPatient(Patient patient){
        String email = patient.getEmail();
        try {
            Optional<Doctor> isAlreadyExist = doctorRepository.findByEmail(email);
            if (isAlreadyExist.isPresent() || patientRepository.findByEmail(email).isPresent()) {
                return ResponseEntity.badRequest().body("User Already Exist");
            }
            String avatar = patient.getAvatar();
            // todo => upload to cloudinary
            String avatarUrl = "";// cloudinary url
            String hashedPassword = passwordEncoder.encode(patient.getPassword());
            patient.setPassword(hashedPassword);
            patient.setAvatar(avatarUrl);
            Patient saved = patientRepository.save(patient);

            UserDetails userDetails = patientUserDetailService.loadUserByUsername(email);
            String token = this.jwtService.generateToken(userDetails);

            HttpHeaders header = new HttpHeaders();
            header.set("token",token);

            PatientResponse response = new PatientResponse().returnResponse(saved);
            return ResponseEntity.ok().headers(header).body(response);
        }
        catch (Exception e){
            new PrintErrorMessage(e);
            return ResponseEntity.badRequest().body("Got some problem while registering the user");
        }
    }

    // ********************** Login ****************************
    public ResponseEntity<?> loginPatient(UserRequest request){

        String email = request.getEmail();
        String password = request.getPassword();
        this.doAuthenticate(email,password);
        UserDetails userDetails = patientUserDetailService.loadUserByUsername(email);
        String token = this.jwtService.generateToken(userDetails);
        HttpHeaders header = new HttpHeaders();
        header.set("token",token);
        // todo => add token in cookie
        try{
            Optional<Patient> isPatient = patientRepository.findByEmail(email);
            if(isPatient.isEmpty()){
                return ResponseEntity.badRequest().body("User not found");
            }
            Patient patient = isPatient.get();
            PatientResponse response = new PatientResponse().returnResponse(patient);
            return ResponseEntity.ok().headers(header).body(response);
        }
        catch(Exception e){
            new PrintErrorMessage(e);
            return ResponseEntity.internalServerError().body("Got some problem while authenticating");
        }
    }

    // ****************** Authentication methods ***************************
    private void doAuthenticate(String email, String password) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
        try {
            securityConfig.patientAuthenticationProvider().authenticate(authentication);
        }
        catch (BadCredentialsException e) {
            throw new BadCredentialsException(" Invalid Username or Password  !!");
        }
    }
    @ExceptionHandler(BadCredentialsException.class)
    public String exceptionHandler() {
        return "Credentials Invalid !!";
    }

}
