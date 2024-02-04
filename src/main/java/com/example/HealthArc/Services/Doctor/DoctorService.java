package com.example.HealthArc.Services.Doctor;

import com.example.HealthArc.Models.Doctor;
import com.example.HealthArc.Repository.Doctor.DoctorRepository;
import com.example.HealthArc.Repository.Patient.PatientRepository;
import com.example.HealthArc.Security.JwtService;
import com.example.HealthArc.Security.SecurityConfig;
import com.example.HealthArc.Security.UserDetailServiceConfig.DoctorUserDetailService;
import com.example.HealthArc.Services.Mail.EmailService;
import com.example.HealthArc.SupportClasses.Doctor.*;
import com.example.HealthArc.SupportClasses.PrintErrorMessage;
import com.example.HealthArc.SupportClasses.UserRequest;
import com.example.HealthArc.SupportClasses.WorkExperience;
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
    EmailService emailService;
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
            emailService.sendSimpleEmail(response.getEmail(),"Account Successfully created","Your account is created successfully");
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

    // ******************* Add Review **********************
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

    // ****************** Update Password *************************
    public ResponseEntity<?> updatePassword(String oldPassword,String password, String reqHeader){
        try{
            String username =  jwtService.extractUsername(reqHeader.substring(7));

            Optional<Doctor> isDoctor = doctorRepository.findByEmail(username);

            if(isDoctor.isEmpty()){
                return ResponseEntity.badRequest().body("Invalid doctor");
            }
            Doctor doctor = isDoctor.get();

            this.doAuthenticate(doctor.getEmail(),oldPassword);

            String hashedPassword = passwordEncoder.encode(password);
            doctor.setPassword(hashedPassword);
            Doctor response = doctorRepository.save(doctor);

            UserDetails userDetails = doctorUserDetailService.loadUserByUsername(response.getEmail());
            String token = this.jwtService.generateToken(userDetails);

            // todo => send token in cookie
            HttpHeaders header = new HttpHeaders();
            header.set("token",token);

            emailService.sendSimpleEmail(response.getEmail(),"Password Updated","Your password is successfully updated");

            return ResponseEntity.ok().headers(header).body(new DoctorResponse().returnResponse(response));
        }
        catch (Exception e){
            new PrintErrorMessage(e);
            return ResponseEntity.internalServerError().body("Got Some error while updating password");
        }
    }

    // ****************** Update Avatar *************************
    public ResponseEntity<?> updateAvatar(String base64, String reqHeader){
        try{
            String username =  jwtService.extractUsername(reqHeader.substring(7));

            Optional<Doctor> isDoctor = doctorRepository.findByEmail(username);

            if(isDoctor.isEmpty()){
                return ResponseEntity.badRequest().body("Invalid doctor");
            }
            Doctor doctor = isDoctor.get();

            // todo => upload to cloudinary
            String avatarUrl = "avatar"; // base64 url
            doctor.setAvatar(avatarUrl);
            Doctor response = doctorRepository.save(doctor);

            return ResponseEntity.ok().body(new DoctorResponse().returnResponse(response));
        }
        catch (Exception e){
            new PrintErrorMessage(e);
            return ResponseEntity.internalServerError().body("Got Some error while updating password");
        }
    }

    // ****************** Update Professional info (add) *************************
    public ResponseEntity<?> updateProfessionalInfo(ProfessionalInfo professionalInfo, String reqHeader){
        try{
            String username =  jwtService.extractUsername(reqHeader.substring(7));

            Optional<Doctor> isDoctor = doctorRepository.findByEmail(username);

            if(isDoctor.isEmpty()){
                return ResponseEntity.badRequest().body("Invalid doctor");
            }
            Doctor doctor = isDoctor.get();

            Optional.ofNullable(professionalInfo.getQualifications()).ifPresent(doctor.getProfessionalInfo().getQualifications()::addAll);
            Optional.ofNullable(professionalInfo.getWorkExperience()).ifPresent(doctor.getProfessionalInfo().getWorkExperience()::addAll);
            Optional.ofNullable(professionalInfo.getSpecializations()).ifPresent(doctor.getProfessionalInfo().getSpecializations()::addAll);

//            if(professionalInfo.getQualifications()!=null){
//                doctor.getProfessionalInfo().getQualifications().addAll(professionalInfo.getQualifications());
//            }
//            if(professionalInfo.getWorkExperience()!=null){
//                doctor.getProfessionalInfo().getWorkExperience().addAll(professionalInfo.getWorkExperience());
//            }
//            if(professionalInfo.getSpecializations()!=null){
//                doctor.getProfessionalInfo().getSpecializations().addAll(professionalInfo.getSpecializations());
//            }

            Doctor response = doctorRepository.save(doctor);
            return ResponseEntity.ok().body(new DoctorResponse().returnResponse(response));
        }
        catch (Exception e){
            new PrintErrorMessage(e);
            return ResponseEntity.internalServerError().body("Got Some error while updating professional info");
        }
    }

    // ****************** Update Availability  *************************
    public ResponseEntity<?> updateAvailability(Availability availability, String reqHeader){
        try{
            String username =  jwtService.extractUsername(reqHeader.substring(7));

            Optional<Doctor> isDoctor = doctorRepository.findByEmail(username);

            if(isDoctor.isEmpty()){
                return ResponseEntity.badRequest().body("Invalid doctor");
            }
            Doctor doctor = isDoctor.get();

            if(availability.getAvailableDays()!=null){
                doctor.getAvailability().setAvailableDays(availability.getAvailableDays());
            }
            if(availability.getAvailableTime()!=null){
                doctor.getAvailability().setAvailableTime(availability.getAvailableTime());
            }

            Doctor response = doctorRepository.save(doctor);
            return ResponseEntity.ok().body(new DoctorResponse().returnResponse(response));
        }
        catch (Exception e){
            new PrintErrorMessage(e);
            return ResponseEntity.internalServerError().body("Got Some error while updating password");
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