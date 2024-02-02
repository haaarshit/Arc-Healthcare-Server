package com.example.HealthArc.Services.Doctor;

import com.example.HealthArc.Models.Doctor;
import com.example.HealthArc.Repository.Doctor.DoctorRepository;
import com.example.HealthArc.Repository.Patient.PatientRepository;
import com.example.HealthArc.Security.JwtService;
import com.example.HealthArc.Security.SecurityConfig;
import com.example.HealthArc.Security.UserDetailServiceConfig.DoctorUserDetailService;
import com.example.HealthArc.SupportClasses.Doctor.DoctorListResponse;
import com.example.HealthArc.SupportClasses.Doctor.DoctorResponse;
import com.example.HealthArc.SupportClasses.Doctor.Review;
import com.example.HealthArc.SupportClasses.PrintErrorMessage;
import com.example.HealthArc.SupportClasses.UserRequest;
import com.sun.net.httpserver.Headers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.Cookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.print.Doc;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private DoctorUserDetailService doctorUserDetailService;
    @Autowired
    JwtService jwtService;
    @Autowired
    SecurityConfig securityConfig;
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // ************************ Signup ***************************
    public ResponseEntity<?>  addDoctor(Doctor doctor){
        String email = doctor.getEmail();
        try {
            Optional<Doctor> isAlreadyExist = doctorRepository.findByEmail(email);
            if (isAlreadyExist.isPresent() || patientRepository.findByEmail(email).isPresent()) {
                return ResponseEntity.badRequest().body("User Already Exist");
            }

            String avatar = doctor.getAvatar();
            // todo => upload to cloudinary
            String avatarUrl = "";// cloudinary url
            String hashedPassword = passwordEncoder.encode(doctor.getPassword());
            doctor.setPassword(hashedPassword);
            doctor.setAvatar(avatarUrl);
            Doctor saved = doctorRepository.save(doctor);

            UserDetails userDetails = doctorUserDetailService.loadUserByUsername(email);
            String token = this.jwtService.generateToken(userDetails);

            HttpHeaders header = new HttpHeaders();
            header.set("token",token);

            DoctorResponse response = new DoctorResponse().returnResponse(saved);
            return ResponseEntity.ok().headers(header).body(response);
        }
        catch (Exception e){
            new PrintErrorMessage(e);
            return ResponseEntity.badRequest().body("Got some problem while registering the user");
        }
    }

    // ********************** Login ****************************
    public ResponseEntity<?> loginDoctor(UserRequest request){
        String email = request.getEmail();
        String password = request.getPassword();
        this.doAuthenticate(email,password);
        UserDetails userDetails = doctorUserDetailService.loadUserByUsername(email);
        String token = this.jwtService.generateToken(userDetails);
        HttpHeaders header = new HttpHeaders();
        header.set("token",token);
        System.out.println("token "+token);
        // todo => add token in cookie
        try{
            Optional<Doctor> isDoctor = doctorRepository.findByEmail(email);
            if(isDoctor.isEmpty()){
                return ResponseEntity.badRequest().body(null);
            }
            Doctor doctor = isDoctor.get();
            DoctorResponse response = new DoctorResponse().returnResponse(doctor);
            return ResponseEntity.ok().headers(header).body(response);
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

    // ******************* Update Doctor **********************
    public ResponseEntity<?> addReview(Review review,String id){
        try{
            Optional<Doctor> isDoctor = doctorRepository.findById(id);
            if(isDoctor.isEmpty()) return ResponseEntity.badRequest().body("Doctor Not found");
            Doctor doctor = isDoctor.get();
            doctor.getReviews().add(review);
            doctorRepository.save(doctor);
            return ResponseEntity.ok().body(doctor);
        }
        catch (Exception e){
            new PrintErrorMessage(e);
            return ResponseEntity.internalServerError().body("Got some error");
        }
    }

    // ****************** Authentication methods ***************************
    private void doAuthenticate(String email, String password) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
        try {
            securityConfig.doctorAuthenticationProvider().authenticate(authentication);
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
