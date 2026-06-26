package wura.example.doctrack.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import wura.example.doctrack.dto.UserDTO;
import wura.example.doctrack.entity.EmploymentEntity;
import wura.example.doctrack.entity.EmploymentStatus;
import wura.example.doctrack.entity.UserEntity;
import wura.example.doctrack.exception.InvalidPasswordException;
import wura.example.doctrack.exception.UserAlreadyExistsException;
import wura.example.doctrack.repository.EmploymentRepository;
import wura.example.doctrack.repository.UserRepository;
import wura.example.doctrack.util.PasswordValidator;

@Service
@RequiredArgsConstructor
@Slf4j

public class UserService {

    private final UserRepository userRepository;
    private final EmploymentRepository employmentRepository;
    private final PasswordEncoder passwordEncoder;



    public ResponseEntity<UserDTO> registerProfile(UserDTO userDto) {
        try {

            if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
                throw new UserAlreadyExistsException ("User with email" + userDto.getEmail() + " already exists");
            }

//            if (!PasswordValidator.isValid(userDto.getPassword())) {
//                throw new InvalidPasswordException("Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, one number and one special character (!@#$%^&*()-+).");
//            }

//            userDto.setPassword((userDto.getPassword()));

            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
            UserEntity profile = toEntity(userDto);


            UserEntity savedProfile = userRepository.save(profile);

            return ResponseEntity.ok(toDTO(savedProfile));


        } catch (DataIntegrityViolationException e) {
            log.error("Database constraint violation during registration for email: {}", userDto.getEmail());
            throw new RuntimeException("User with email " + userDto.getEmail() + " already exists");
        } catch (MailException e) {
            log.error("Email service failed for registration: {}, Error: {}", userDto.getEmail(), e.getMessage());
            throw new RuntimeException("Registration completed but activation email could not be sent");
        }
    }



    public UserEntity toEntity(UserDTO userDTO){

        EmploymentEntity employment = null;
        if (userDTO.getEmploymentStatus() != null) {
            EmploymentStatus status = EmploymentStatus.valueOf(userDTO.getEmploymentStatus());
            employment = employmentRepository.findByName(status)
                    .orElseThrow(() -> new RuntimeException("Invalid employment status: " + userDTO.getEmploymentStatus()));
        }
        return UserEntity.builder()
                .id(userDTO.getId())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .createdAt(userDTO.getCreatedAt())
                .employmentStatus(employment)
                .build();
    }

    public UserDTO toDTO(UserEntity userEntity){
        return UserDTO.builder()
                .id(userEntity.getId())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .email(userEntity.getEmail())
                .createdAt(userEntity.getCreatedAt())
                .employmentStatus(
                        userEntity.getEmploymentStatus() != null
                                ? userEntity.getEmploymentStatus().getName().name()
                                : null
                )
                .build();
    }
}
