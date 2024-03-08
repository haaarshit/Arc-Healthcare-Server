package com.example.HealthArc.Repository.Appointment;

import com.example.HealthArc.Models.Appointment;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface AppointmentRepository extends MongoRepository<Appointment,String> {
    Optional<List<Appointment>> findByPatientIdAndDoctorId(String patientId, String doctorId);
     Optional<List<Appointment>>  findByPatientId(String patientId);
     Optional<List<Appointment>> findByDoctorId(String doctorId);
}
