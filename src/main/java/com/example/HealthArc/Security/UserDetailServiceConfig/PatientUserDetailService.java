//package com.example.HealthArc.Security.UserDetailServiceConfig;
//
//import com.example.HealthArc.Models.Patient;
//import com.example.HealthArc.Repository.Patient.PatientRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//
//import java.util.Optional;
//
//public class PatientUserDetailService implements UserDetailsService {
//
//    @Autowired
//    private PatientRepository patientRepository ;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        System.out.println("Loading doctor from db");
//        Optional<Patient> patient = patientRepository.findByEmail(username);
//
//
//        if (patient.isEmpty()) {
//            System.out.println("Doctor not found");
//            throw new UsernameNotFoundException("No doctor found.");
//        }
//        return patient.get();
//    }
//}
