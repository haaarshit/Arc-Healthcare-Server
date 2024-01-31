package com.example.HealthArc.Repository.Doctor;

import com.example.HealthArc.Models.Doctor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface DoctorRepository extends MongoRepository<Doctor,String> {
    Optional<Doctor> findByEmail(String email);

}
