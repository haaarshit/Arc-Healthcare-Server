package com.example.HealthArc.Repository.Patient;

import com.example.HealthArc.Models.Patient;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends MongoRepository<Patient,String> {
    Optional<Patient> findByEmail(String email);
}
