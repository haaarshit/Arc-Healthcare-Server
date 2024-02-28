package com.example.HealthArc.Services.Patient;

import com.example.HealthArc.Models.Appointment;
import com.example.HealthArc.Models.AppointmentRequest;
import com.example.HealthArc.Models.Doctor;
import com.example.HealthArc.Models.Patient;
import com.example.HealthArc.Repository.Appointment.AppointmentRequestRepository;
import com.example.HealthArc.Repository.Doctor.DoctorRepository;
import com.example.HealthArc.Repository.Patient.PatientRepository;
import com.example.HealthArc.Security.JwtService;
import com.example.HealthArc.Security.SecurityConfig;
import com.example.HealthArc.Security.UserDetailServiceConfig.PatientUserDetailService;
import com.example.HealthArc.Services.Appointment.AppointmentService;
import com.example.HealthArc.Services.Cloudinary.CloudinaryService;
import com.example.HealthArc.Services.Mail.EmailService;
import com.example.HealthArc.SupportClasses.Address;
import com.example.HealthArc.SupportClasses.Doctor.DoctorResponse;
import com.example.HealthArc.SupportClasses.Patient.DoctorProfileForPatient;
import com.example.HealthArc.SupportClasses.Patient.PatientDashboard;
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

import javax.print.Doc;
import java.util.List;
import java.util.Optional;


@Service
public class PatientService {
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private AppointmentRequestRepository appointmentRequestRepository;
    @Autowired
    private PatientUserDetailService patientUserDetailService;

    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private EmailService emailService;

    @Autowired
    CloudinaryService cloudinaryService;
    @Autowired
    private SecurityConfig securityConfig;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // ************************ Signup ***************************
    public ResponseEntity<?> addPatient(Patient patient){
        String email = patient.getEmail();
        try {
            Optional<Doctor> isAlreadyExist = doctorRepository.findByEmail(email);
            if (isAlreadyExist.isPresent() || patientRepository.findByEmail(email).isPresent()) {
                return ResponseEntity.badRequest().body("User Already Exist");
            }
            String avatar = patient.getAvatar();
            String avatarUrl = (String) cloudinaryService.upload(avatar).get("url");// cloudinary url;// cloudinary url
            String hashedPassword = passwordEncoder.encode(patient.getPassword());
            patient.setPassword(hashedPassword);
            patient.setAvatar(avatarUrl);
            Patient saved = patientRepository.save(patient);

            UserDetails userDetails = patientUserDetailService.loadUserByUsername(email);
            String token = this.jwtService.generateToken(userDetails);
            HttpHeaders header = new HttpHeaders();
            header.set("token",token);
            PatientResponse response = new PatientResponse().returnResponse(saved);
            emailService.sendSimpleEmail(response.getEmail(),"Account Successfully created","Your account is created successfully");
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

    // ******************** getPatientById ***********************
    public ResponseEntity<?> getPatientById(String id){
        try{
            Optional<Patient> patient = patientRepository.findById(id);
            if(patient.isEmpty()){
                return ResponseEntity.badRequest().body("Doctor not found");
            }
            PatientResponse response = new PatientResponse().returnResponse(patient.get());
            return ResponseEntity.ok().body(response);
        }
        catch (Exception e){
            new PrintErrorMessage(e);
            return ResponseEntity.internalServerError().body("Internal Server Error");
        }
    }

    // ****************** Update Password *************************
    public ResponseEntity<?> updatePassword(String oldPassword,String password, String reqHeader){
        try{
            String username =  jwtService.extractUsername(reqHeader.substring(7));

            Optional<Patient> isPatient = patientRepository.findByEmail(username);

            if(isPatient.isEmpty()){
                return ResponseEntity.badRequest().body("Invalid doctor");
            }
            Patient patient = isPatient.get();

            this.doAuthenticate(patient.getEmail(),oldPassword);

            String hashedPassword = passwordEncoder.encode(password);
            patient.setPassword(hashedPassword);
            Patient response = patientRepository.save(patient);

            UserDetails userDetails = patientUserDetailService.loadUserByUsername(response.getEmail());
            String token = this.jwtService.generateToken(userDetails);

            // todo => send token in cookie
            HttpHeaders header = new HttpHeaders();
            header.set("token",token);
            emailService.sendSimpleEmail(response.getEmail(),"Password Updated","Your password is successfully updated");
            return ResponseEntity.ok().headers(header).body(new PatientResponse().returnResponse(response));
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

            Optional<Patient> isPatient = patientRepository.findByEmail(username);

            if(isPatient.isEmpty()){
                return ResponseEntity.badRequest().body("Invalid doctor");
            }
            Patient patient = isPatient.get();


            String avatarUrl =(String) cloudinaryService.upload(base64).get("url"); // base64 url
            patient.setAvatar(avatarUrl);
            Patient response = patientRepository.save(patient);
            return ResponseEntity.ok().body(new PatientResponse().returnResponse(response));
        }
        catch (Exception e){
            new PrintErrorMessage(e);
            return ResponseEntity.internalServerError().body("Got Some error while updating avatar");
        }
    }

    // ****************** Update Address *************************
    public ResponseEntity<?> updateAddress(Address address, String reqHeader){
        try{
            String username =  jwtService.extractUsername(reqHeader.substring(7));

            Optional<Patient> isPatient = patientRepository.findByEmail(username);

            if(isPatient.isEmpty()){
                return ResponseEntity.badRequest().body("Invalid doctor");
            }
            Patient patient = isPatient.get();
            if(address  != null){
                patient.getPersonalInfo().setAddress(address);
            }
            Patient response = patientRepository.save(patient);
            return ResponseEntity.ok().body(new PatientResponse().returnResponse(response));
        }
        catch (Exception e){
            new PrintErrorMessage(e);
            return ResponseEntity.internalServerError().body("Got Some error while updating address");
        }
    }


    // ****************** APPOINTMENT ***************************
    // ****************** Patient dashboard ****************
    public ResponseEntity<?> getPatientDashboard(String reqHeader){
        try{
            String username =  jwtService.extractUsername(reqHeader.substring(7));
            Optional<Patient> isPatient = patientRepository.findByEmail(username);

            if(isPatient.isEmpty()){
                return ResponseEntity.badRequest().body("Invalid doctor");
            }
            Patient patient = isPatient.get();
            List<Appointment> appointmentList = appointmentService.findAppointmentsByPatient(patient.getId());
            PatientDashboard patientDashboard = new PatientDashboard(new PatientResponse().returnResponse(patient),appointmentList);
            return ResponseEntity.ok().body(patientDashboard);
        }
        catch (Exception e){
            new PrintErrorMessage(e);
            return ResponseEntity.internalServerError().body("Got Some error while updating password");
        }
    }

    // ****************** findAllAppointment of a specific patient by doctor  ****************
    public ResponseEntity<?> getDoctorProfileForPatient(String doctorId,String reqHeader){
        try{
            String username =  jwtService.extractUsername(reqHeader.substring(7));

            Optional<Patient> isPatient = patientRepository.findByEmail(username);

            if(isPatient.isEmpty()){
                return ResponseEntity.badRequest().body("Invalid Patient");
            }
            Patient patient = isPatient.get();
            Doctor doctor = doctorRepository.findById(doctorId).orElseThrow();
            List<Appointment> appointmentList = appointmentService.findAppointmetByPatientIdAndDoctor(patient.getId(),doctorId);
            DoctorProfileForPatient doctorProfileForPatient = new DoctorProfileForPatient(new DoctorResponse().returnResponse(doctor),appointmentList);
            return ResponseEntity.ok().body(doctorProfileForPatient);
        }
        catch (Exception e){
            new PrintErrorMessage(e);
            return ResponseEntity.internalServerError().body("Got Some error while updating password");
        }
    }

    // **************** Add appointment request ******************************
    public ResponseEntity<?> addAppointmentRequest(AppointmentRequest appointmentReq, String reqHeader){
        try{
            String username =  jwtService.extractUsername(reqHeader.substring(7));

            Optional<Patient> isPatient = patientRepository.findByEmail(username);

            if(isPatient.isEmpty()){
                return ResponseEntity.badRequest().body("Invalid Patient");
            }
            Patient patient = isPatient.get();
            if(appointmentReq.getDoctorId()== null)  return ResponseEntity.internalServerError().body("Doctor Id should not be null");
            appointmentReq.setPatientId(patient.getId());
            appointmentRequestRepository.save(appointmentReq);
            return new ResponseEntity<>("Request send successfull",HttpStatus.CREATED);
        }
        catch (Exception e){
            new PrintErrorMessage(e);
            return ResponseEntity.internalServerError().body("Got Some error while creating your request");
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
