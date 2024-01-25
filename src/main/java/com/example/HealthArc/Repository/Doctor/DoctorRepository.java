package com.example.HealthArc.Repository.Doctor;

import com.example.HealthArc.Models.Doctor.Doctor;
import org.springframework.data.mongodb.repository.MongoRepository;

import javax.print.Doc;
import java.util.Optional;


public interface DoctorRepository extends MongoRepository<Doctor,String> {
    Optional<Doctor> findByEmail(String email);
}
