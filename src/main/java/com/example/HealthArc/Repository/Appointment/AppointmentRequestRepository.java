package com.example.HealthArc.Repository.Appointment;

import com.example.HealthArc.Models.Appointment;
import com.example.HealthArc.Models.AppointmentRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRequestRepository extends MongoRepository<AppointmentRequest,String> {
    Optional<List<AppointmentRequest>> findByPatientId(String patientId);
    Optional<List<AppointmentRequest>> findByDoctorId(String doctorId);
}
