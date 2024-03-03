package com.example.HealthArc.SupportClasses.Doctor;

import com.example.HealthArc.Models.Doctor;
import com.example.HealthArc.SupportClasses.Address;
import com.example.HealthArc.SupportClasses.Doctor.Availability;
import com.example.HealthArc.SupportClasses.Doctor.DoctorPersonalInfo;
import com.example.HealthArc.SupportClasses.Doctor.ProfessionalInfo;
import com.example.HealthArc.SupportClasses.Doctor.Review;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;
import java.util.List;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DoctorResponse {
    private  String id;
    private  String email;
    private  String phone;
    private  String avatar;
    private  DoctorPersonalInfo personalInfo;
    private  Address address;
    private String  clinicName;
    private  ProfessionalInfo professionalInfo;
    private  Availability availability;
    private  int consultationFees;
    private  List<String> languages;
    private  List<Review> reviews;
    private int totalRating = 0;
    private int peopleRated = 0;
    private Date createdAt ;
    private Date updatedAt ;
    public DoctorResponse returnResponse(Doctor doctor){
        return DoctorResponse.builder().id(doctor.getId())
                .email(doctor.getEmail())
                .phone(doctor.getPhone())
                .avatar(doctor.getAvatar())
                .personalInfo(doctor.getPersonalInfo())
                .address(doctor.getAddress())
                .professionalInfo(doctor.getProfessionalInfo())
                .availability(doctor.getAvailability())
                .consultationFees(doctor.getConsultationFees())
                .languages(doctor.getLanguages())
                .reviews(doctor.getReviews())
                .clinicName(doctor.getClinicName())
                .totalRating(doctor.getTotalRating())
                .peopleRated(doctor.getPeopleRated())
                .createdAt(doctor.getCreatedAt())
                .updatedAt(doctor.getUpdatedAt())
                .build();

    }
}
