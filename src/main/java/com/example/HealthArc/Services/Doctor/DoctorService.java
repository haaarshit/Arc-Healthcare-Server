package com.example.HealthArc.Services.Doctor;

import com.example.HealthArc.Models.Appointment;
import com.example.HealthArc.Models.AppointmentRequest;
import com.example.HealthArc.Models.Doctor;
import com.example.HealthArc.Models.Patient;
import com.example.HealthArc.Repository.Appointment.AppointmentRequestRepository;
import com.example.HealthArc.Repository.Doctor.DoctorRepository;
import com.example.HealthArc.Repository.Patient.PatientRepository;
import com.example.HealthArc.Security.JwtService;
import com.example.HealthArc.Security.SecurityConfig;
import com.example.HealthArc.Security.UserDetailServiceConfig.DoctorUserDetailService;
import com.example.HealthArc.Services.Appointment.AppointmentService;
import com.example.HealthArc.Services.Cloudinary.CloudinaryConfig;
import com.example.HealthArc.Services.Cloudinary.CloudinaryService;
import com.example.HealthArc.Services.Mail.EmailService;
import com.example.HealthArc.SupportClasses.Appointment.UpdateAppointmentRequest;
import com.example.HealthArc.SupportClasses.Doctor.*;
import com.example.HealthArc.SupportClasses.Patient.PatientResponse;
import com.example.HealthArc.SupportClasses.PrintErrorMessage;
import com.example.HealthArc.SupportClasses.UserRequest;
import com.example.HealthArc.SupportClasses.WorkExperience;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.xml.crypto.Data;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {
    private final MongoTemplate mongoTemplate;

    public DoctorService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private AppointmentRequestRepository appointmentRequestRepository;
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    EmailService emailService;
    @Autowired
    CloudinaryService cloudinaryService;
    @Autowired
    private DoctorUserDetailService doctorUserDetailService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private SecurityConfig securityConfig;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // ************************ Signup ***************************
    public ResponseEntity<?>  addDoctor(Doctor doctor, HttpServletResponse servletResponse ){
        try {

            String email = doctor.getEmail();
            Optional<Doctor> isAlreadyExist = doctorRepository.findByEmail(email);
            if (isAlreadyExist.isPresent() || patientRepository.findByEmail(email).isPresent()) {
                return ResponseEntity.badRequest().body("User Already Exist");
            }

            String avatar = doctor.getAvatar();

            String avatarUrl =(String) cloudinaryService.upload(avatar).get("url");// cloudinary url
            String hashedPassword = passwordEncoder.encode(doctor.getPassword());
            doctor.setPassword(hashedPassword);
            doctor.setAvatar(avatarUrl);
            doctor.getAddress().setCity(doctor.getAddress().getCity().toLowerCase());
            Doctor saved = doctorRepository.save(doctor);


            DoctorResponse response = new DoctorResponse().returnResponse(saved);
            emailService.sendSimpleEmail(response.getEmail(),"Account Successfully created","Your account is created successfully! Welcome to Health Arc");
            return ResponseEntity.ok().body(response);
        }
        catch (Exception e){
            new PrintErrorMessage(e);
            return ResponseEntity.badRequest().body("Got some problem while registering the user");
        }
    }

    // ********************** Login ****************************
    public ResponseEntity<?> loginDoctor(UserRequest request,HttpServletResponse servletResponse){
        String email = request.getEmail();
        String password = request.getPassword();
        this.doAuthenticate(email,password);
        UserDetails userDetails = doctorUserDetailService.loadUserByUsername(email);
        String token = this.jwtService.generateToken(userDetails);

        // cookie
//        Cookie cookie = new Cookie("token",token);
//        cookie.setMaxAge(7 * 24 * 60 * 60);
//        cookie.setSecure(true);
//        cookie.setHttpOnly(true);
//        servletResponse.addCookie(cookie);

        HttpHeaders header = new HttpHeaders();
        header.set("token",token);

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
            doctor.setTotalRating(doctor.getTotalRating() + review.getRating());
            doctor.setPeopleRated(doctor.getPeopleRated()+1); // can find average rating from = total_rating / people_rated
            doctorRepository.save(doctor);

            return ResponseEntity.ok().body(doctor);
        }
        catch (Exception e){
            new PrintErrorMessage(e);
            return ResponseEntity.internalServerError().body("Got some error");
        }
    }

    // ****************** Update Password *************************
    public ResponseEntity<?> updatePassword(String oldPassword,String password, String reqHeader,HttpServletResponse servletResponse){
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

            // cookie
//            Cookie cookie = new Cookie("token",token);
//            cookie.setMaxAge(7 * 24 * 60 * 60);
//            cookie.setSecure(true);
//            cookie.setHttpOnly(true);
//            servletResponse.addCookie(cookie);
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

            String avatarUrl =(String) cloudinaryService.upload(base64).get("url");// cloudinary url
            doctor.setAvatar(avatarUrl);
            Doctor response = doctorRepository.save(doctor);
            return ResponseEntity.ok().body(new DoctorResponse().returnResponse(response));
        }
        catch (Exception e){
            new PrintErrorMessage(e);
            return ResponseEntity.internalServerError().body("Got Some error while updating password");
        }
    }

    // ****************** Update Qualifications (add) *************************

    // add qualification
    public ResponseEntity<?>  addQualification(Qualification qualification, String reqHeader){
        try{
            String username =  jwtService.extractUsername(reqHeader.substring(7));

            Optional<Doctor> isDoctor = doctorRepository.findByEmail(username);

            if(isDoctor.isEmpty()){
                return ResponseEntity.badRequest().body("Invalid doctor");
            }
            Doctor doctor = isDoctor.get();

            Optional.ofNullable(qualification).ifPresent(doctor.getProfessionalInfo().getQualifications()::add);

            Doctor response = doctorRepository.save(doctor);
            return ResponseEntity.ok().body(new DoctorResponse().returnResponse(response));

        }
        catch (Exception e){
            new PrintErrorMessage(e);
            return ResponseEntity.internalServerError().body("Got Some error while updating professional info");
        }
    }


    // add work experience
    public ResponseEntity<?>  addWorkExperiece(WorkExperience workExperience, String reqHeader){
        try{
            String username =  jwtService.extractUsername(reqHeader.substring(7));

            Optional<Doctor> isDoctor = doctorRepository.findByEmail(username);

            if(isDoctor.isEmpty()){
                return ResponseEntity.badRequest().body("Invalid doctor");
            }
            Doctor doctor = isDoctor.get();

            Optional.ofNullable(workExperience).ifPresent(doctor.getProfessionalInfo().getWorkExperience()::add);

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

    // ******************** APPOINTMENT *************************
    // ****************** createAppointment ************************
    public ResponseEntity<?> createAppointment(Appointment appointment,String appointmentRequestId,String reqHeader){
        try{
            String email =  jwtService.extractUsername(reqHeader.substring(7));
            Doctor doctor = doctorRepository.findByEmail(email).orElseThrow();
            Patient patient = patientRepository.findById(appointmentRequestRepository.findById(appointmentRequestId).orElseThrow().getPatientId()).orElseThrow();
            if(!doctor.getId().equals(appointment.getDoctorId())) throw new Exception("Invalid doctor id");
            appointment.setPatientName(patient.getPersonalInfo().getFirstName() +" " + patient.getPersonalInfo().getLastName());
            appointment.setPatientEmail(patient.getEmail());
            appointment.setStatus("pending");
            Appointment appointment1 = appointmentService.createAppointment(appointment);
            appointmentRequestRepository.deleteById(appointmentRequestId);
            return new ResponseEntity<>(appointment1,HttpStatus.CREATED);
        }
        catch (Exception e){
            new PrintErrorMessage(e);
            return ResponseEntity.internalServerError().body("Got Some error while updating password");
        }
        
    }
    public ResponseEntity<?> rejectAppointment(String appointmentRequestId,String reqHeader){
        try{
            String email =  jwtService.extractUsername(reqHeader.substring(7));
            Doctor doctor = doctorRepository.findByEmail(email).orElseThrow();
            AppointmentRequest appointmentRequest = appointmentRequestRepository.findById(appointmentRequestId).orElseThrow();
            if(!appointmentRequest.getDoctorId().equals(doctor.getId())){
                throw new Error("Can't delete other's requets");
            }
            Patient patient = patientRepository.findById(appointmentRequestRepository.findById(appointmentRequestId).orElseThrow().getPatientId()).orElseThrow();
            String drName= doctor.getPersonalInfo().getFirstName() +" "+ doctor.getPersonalInfo().getLastName();
            String appointDate = appointmentRequest.getCreatedAt().toString();
            String message = String.format("Your request for Appointment from Doctor %s on date %s is declined !! We're sorry for inconvenience 🥲",drName,appointDate);
            emailService.sendSimpleEmail(patient.getEmail(),"Appointment Rejection",message);
            appointmentRequestRepository.deleteById(appointmentRequestId);
            return new ResponseEntity<>("Appointment rejected",HttpStatus.CREATED);
        }
        catch (Exception e){
            new PrintErrorMessage(e);
            return ResponseEntity.internalServerError().body("Got Some error while updating password");
        }

    }

    // ****************** findAllAppointmentByDoctor ****************
    public ResponseEntity<?> getDoctorDashboard(String reqHeader){
        try{
            String username =  jwtService.extractUsername(reqHeader.substring(7));
            Optional<Doctor> isDoctor = doctorRepository.findByEmail(username);
            if(isDoctor.isEmpty()){
                return ResponseEntity.badRequest().body("Invalid doctor");
            }
            Doctor doctor = isDoctor.get();
            List<Appointment> appointmentList = appointmentService.findAppointmentsByDoctor(doctor.getId());
            List<AppointmentRequest> appointmentRequestList = appointmentRequestRepository.findByDoctorId(doctor.getId()).get();

            DoctorDashboard doctorDashboard = new DoctorDashboard();
            doctorDashboard.setDoctorInfo(new DoctorResponse().returnResponse(doctor));
            doctorDashboard.setAppointmentList(appointmentList);
            doctorDashboard.setAppointmentRequestList(appointmentRequestList);
            return ResponseEntity.ok().body(doctorDashboard);
        }
        catch (Exception e){
            new PrintErrorMessage(e);
            return ResponseEntity.internalServerError().body("Got Some error while updating password");
        }
    }

    // ****************** findAllAppointment Where of a specific patient by doctor  ****************
    public ResponseEntity<?> getPatientProfileForDoctor(String patientId,String reqHeader){
        try{
            String username =  jwtService.extractUsername(reqHeader.substring(7));

            Optional<Doctor> isDoctor = doctorRepository.findByEmail(username);

            if(isDoctor.isEmpty()){
                return ResponseEntity.badRequest().body("Invalid doctor");
            }
            Doctor doctor = isDoctor.get();
            PatientProfileForDoctor patientProfileForDoctor = new PatientProfileForDoctor();
            Patient patient = patientRepository.findById(patientId).orElseThrow();
            List<Appointment> appointmentList = appointmentService.findAppointmetByPatientIdAndDoctor(patientId,doctor.getId());
            patientProfileForDoctor.setPatientInfo(new PatientResponse().returnResponse(patient));
            patientProfileForDoctor.setAppointmentList(appointmentList);
            return ResponseEntity.ok().body(patientProfileForDoctor);
        }
        catch (Exception e){
            new PrintErrorMessage(e);
            return ResponseEntity.internalServerError().body("Got Some error while updating password");
        }
    }

    // ***************** update appointment ************************
    public ResponseEntity<?> updateAppointment(String id,UpdateAppointmentRequest appointmentRequest){
        try{
            return ResponseEntity.ok().body(appointmentService.updateAppointment(id,appointmentRequest));
        }
        catch (Exception e){
            new PrintErrorMessage(e);
            return ResponseEntity.internalServerError().body("Got Some error while updating password");
        }
    }

    // ***************** findDoctorByCity ********************
    public ResponseEntity<?> getDoctorByCity(String city){
        try {

            BasicQuery query = new BasicQuery("{ 'address.city' : '" + city.toLowerCase() + "' }");
            List<Doctor> doctors = mongoTemplate.find(query, Doctor.class);
            return ResponseEntity.ok().body(new DoctorListResponse().returnResponse(doctors));
        }
        catch (Exception e){
            new PrintErrorMessage(e);
            return ResponseEntity.internalServerError().body("Got some problem");
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