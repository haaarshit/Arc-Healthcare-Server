package com.example.HealthArc.Services.Appointment;

import com.example.HealthArc.Models.Appointment;
import com.example.HealthArc.Repository.Appointment.AppointmentRepository;
import com.example.HealthArc.SupportClasses.Appointment.UpdateAppointmentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {
    @Autowired
    AppointmentRepository appointmentRepository;

    // ********************** Create appointment
    public Appointment createAppointment(Appointment appointment){
        Appointment appoinment = appointmentRepository.save(appointment);
        return appointment;
    }

    // ********************** find all the patient appointment
    public List<Appointment> findAppointmentsByPatient(String patientId){
        Optional<List<Appointment>> appointments = appointmentRepository.findByPatientId(patientId);
        return appointments.orElseGet(ArrayList::new);
    }
    // ********************** find all appointment of doctors
    public List<Appointment> findAppointmentsByDoctor(String doctorId){
        Optional<List<Appointment>> appointments = appointmentRepository.findByDoctorId(doctorId);
        return appointments.orElseGet(ArrayList::new);
    }
    // ********************** find all appointment between patient and doctor id
    public List<Appointment> findAppointmetByPatientIdAndDoctor(String patientId,String doctorId){
        Optional<List<Appointment>> appointments = appointmentRepository.findByPatientIdAndDoctorId(patientId,doctorId);
        return appointments.orElseGet(ArrayList::new);
    }
    // ********************** Update appointment ***************************
    public Appointment updateAppointment(String id,UpdateAppointmentRequest updateAppoinrmentRequest) throws NoSuchFieldException {
           Optional<Appointment> isAppointment = appointmentRepository.findById(id);
           if(isAppointment.isEmpty()) throw new NoSuchFieldException("No appointment found");
           Appointment appointment = isAppointment.get();
           appointment.setStartTime(updateAppoinrmentRequest.getStartTime());
           appointment.setEndTime(updateAppoinrmentRequest.getEndTime());
           appointment.setSymptoms(updateAppoinrmentRequest.getSymptoms());
           appointment.setDiagnosis(updateAppoinrmentRequest.getDiagnosis());
           appointment.setPrescription(updateAppoinrmentRequest.getPrescription());
           appointment.setStatus(updateAppoinrmentRequest.getStatus());
           appointment.setNotes(updateAppoinrmentRequest.getNotes());
           appointment.setFollowUpDate(updateAppoinrmentRequest.getFollowUpDate());
           appointment.setFollowUpInstructions(updateAppoinrmentRequest.getFollowUpInstructions());
           appointment.setAppointmentType(updateAppoinrmentRequest.getAppointmentType());
           appointment.setPrescribedMedications(updateAppoinrmentRequest.getPrescribedMedications());
           appointment.setLabTestRequests(updateAppoinrmentRequest.getLabTestRequests());
           appointment.setPatientConsent(updateAppoinrmentRequest.getPatientConsent());
           return appointmentRepository.save(appointment);

    }
}
