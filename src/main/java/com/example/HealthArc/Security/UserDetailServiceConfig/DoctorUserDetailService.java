//package com.example.HealthArc.Security.UserDetailServiceConfig;
//
//import com.example.HealthArc.Models.Doctor;
//import com.example.HealthArc.Repository.Doctor.DoctorRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Service
//@Qualifier("doctor")
//public class DoctorUserDetailService implements UserDetailsService {
//    @Autowired
//    private DoctorRepository doctorRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        System.out.println("Loading doctor from db");
//        Optional<Doctor> doctor = doctorRepository.findByEmail(username);
//
//
//        if (doctor.isEmpty()) {
//            System.out.println("Doctor not found");
//            throw new UsernameNotFoundException("No doctor found.");
//        }
//        return doctor.get();
//    }
//}
