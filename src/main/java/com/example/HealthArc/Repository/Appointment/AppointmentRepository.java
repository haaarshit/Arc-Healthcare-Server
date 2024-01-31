package com.example.HealthArc.Repository.Appointment;

import com.example.HealthArc.Models.Appointment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AppointmentRepository extends MongoRepository<Appointment,String> {
}
