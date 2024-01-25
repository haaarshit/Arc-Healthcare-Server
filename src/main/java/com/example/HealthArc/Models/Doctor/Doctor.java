package com.example.HealthArc.Models.Doctor;

import com.example.HealthArc.SupportClasses.Availability;
import com.example.HealthArc.SupportClasses.PersonalInfo;
import com.example.HealthArc.SupportClasses.ProfessionalInfo;
import com.example.HealthArc.SupportClasses.Review;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "doctors")
@Getter
@Setter
public class Doctor {
    @Id
    private ObjectId id;
    @Indexed(unique = true)
    String email;
    private PersonalInfo personalInfo;
    private ProfessionalInfo professionalInfo;
    private Availability availability;
    private int consultationFees;
    private List<String> languages;
    private List<Review> reviews;

}

// dummy data

//{
//        "email":"email"
//        "personalInfo": {
//        "firstName": "John",
//        "lastName": "Doe",
//        "gender": "Male",
//        "dateOfBirth": "1990-05-15",
//        "nationality": "American",
//        "contact": {
//        "phone": "+1 123-456-7890",
//        "address": {
//        "street": "123 Main St",
//        "city": "Anytown",
//        "state": "CA",
//        "zipCode": "12345",
//        "country": "USA"
//        }
//        }
//        },
//        "professionalInfo": {
//        "registrationNumber": "MD123456",
//        "qualification": [
//        {
//        "degree": "Doctor of Medicine",
//        "university": "Medical University",
//        "year": 2015
//        }
//        ],
//        "specializations": ["Cardiology"],
//        "certifications": [
//        {
//        "name": "Board Certified Cardiologist",
//        "issuedBy": "Medical Board",
//        "issuedDate": "2020-02-01"
//        }
//        ],
//        "workExperience": [
//        {
//        "hospitalName": "City General Hospital",
//        "position": "Senior Cardiologist",
//        "startDate": "2015-06-01",
//        "endDate": "2022-01-15"
//        }
//        ],
//        "awards": ["Outstanding Cardiologist of the Year"]
//        },
//        "availability": {
//        "isAvailable": true,
//        "availableDays": ["Monday", "Wednesday", "Friday"],
//        "availableTime": {
//        "startTime": "08:00 AM",
//        "endTime": "05:00 PM"
//        }
//        },
//        "consultationFees": {
//        "currency": "USD",
//        "amount": 150.00
//        },
//        "languages": ["English", "Spanish"],
//        "reviews": [
//        {
//        "patientName": "Alice Johnson",
//        "rating": 5,
//        "comment": "Dr. Doe is very knowledgeable and caring.",
//        "date": "2022-03-10"
//        },
//        {
//        "patientName": "Bob Smith",
//        "rating": 4,
//        "comment": "Great experience with Dr. Doe. Highly recommended.",
//        "date": "2022-02-28"
//        }
//        ]
//        }
