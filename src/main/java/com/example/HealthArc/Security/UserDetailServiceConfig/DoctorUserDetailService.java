import com.example.HealthArc.Models.Doctor;
import com.example.HealthArc.Repository.Doctor.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Qualifier("doctor")
public class DoctoUserDetailService implements UserDetailsService {
    @Autowired
    private DoctorRepository doctorRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Loading doctor from db");
        Optional<Doctor> doctor = doctorRepository.findByEmail(username);


        if (doctor == null) {
            System.out.println("Doctor not found");
            throw new UsernameNotFoundException("No doctor found.");
        }
        if(doctor.isPresent()){
        }
        return new Doctor();

    }
}
