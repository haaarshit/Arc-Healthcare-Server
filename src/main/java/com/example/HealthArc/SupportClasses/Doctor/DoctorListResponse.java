package com.example.HealthArc.SupportClasses.Doctor;

import com.example.HealthArc.Models.Doctor;
import com.example.HealthArc.SupportClasses.Address;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DoctorListResponse {
    //   fields = "{id:1,email:1,phone:1,avatar:1,address:1,professionalInfo:1,totalRating:1,peopleRated:1,createdAt:1}"

    private String id;
    private String email;
    private String phone;
    private String avatar;
    private Address address;
    private  ProfessionalInfo professionalInfo;
    private  int totalRating;
    private  int peopleRated;
    private Date createdAt;


    public List<DoctorListResponse> returnResponse(List<Doctor> doctors){
        List<DoctorListResponse> doctorListResponse = new ArrayList<>();
        for(Doctor doctor:doctors) {
            doctorListResponse.add(DoctorListResponse.builder().id(doctor.getId())
                    .email(doctor.getEmail())
                    .phone(doctor.getPhone())
                    .avatar(doctor.getAvatar())
                    .address(doctor.getAddress())
                    .professionalInfo(doctor.getProfessionalInfo())
                    .totalRating(doctor.getTotalRating())
                    .peopleRated(doctor.getPeopleRated())
                    .createdAt(doctor.getCreatedAt()).build());
        }
        return doctorListResponse;

    }


}
