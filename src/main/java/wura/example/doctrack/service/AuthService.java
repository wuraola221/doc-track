package wura.example.doctrack.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import wura.example.doctrack.dto.AuthDto;
import wura.example.doctrack.dto.UserDTO;
import wura.example.doctrack.entity.UserEntity;
import wura.example.doctrack.repository.UserRepository;
import wura.example.doctrack.util.JwtUtil;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j

public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public ResponseEntity<AuthDto> login (AuthDto authDto) {
        try{
            // Validate credentials
            if (authDto.getEmail() == null || authDto.getPassword() == null) {
                log.warn("Login attempt with missing credentials");
                return ResponseEntity.badRequest()
                        .body(AuthDto.error("Email and password are required"));
            }

            // Find user by email
            Optional<UserEntity> userOpt =findUserByEmail(authDto.getEmail());
            if (userOpt.isEmpty()) {
                log.warn("Login attempt with non-existent email: {}", authDto.getEmail());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(AuthDto.error("Invalid email or password"));
            }

            UserEntity user = userOpt.get();

            if (!passwordEncoder.matches(authDto.getPassword(), user.getPassword())) {
                log.warn("Failed login attempt for email: {}", authDto.getEmail());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(AuthDto.error("Invalid email or password"));
            }

            // Generate JWT token
            String token = jwtUtil.generateToken(
                    user.getEmail()
            );
            long expiresIn = jwtUtil.getExpirationTime();

            UserDTO userDto = new UserDTO();
            userDto.setId(user.getId());
            userDto.setFirstName(user.getFirstName());
            userDto.setLastName(user.getLastName());
            userDto.setEmail(user.getEmail());
//            userDto.setEmploymentStatus(user.getEmploymentStatus().getName().name());

            log.info("Successful login for user: {}", user.getEmail());

            // Return success response with token and profile
            return ResponseEntity.ok(AuthDto.success(token, expiresIn, userDto));
        } catch (Exception e) {
            log.error("Login failed for email: {}, Error: {}", authDto.getEmail(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(AuthDto.error("Login failed due to an unexpected error"));
        }
    }


    private Optional<UserEntity> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
