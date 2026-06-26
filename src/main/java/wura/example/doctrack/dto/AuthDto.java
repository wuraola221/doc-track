package wura.example.doctrack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class AuthDto {
    private Long id;
    private String email;
    private String password;
    private String token;
    private String message;
    private Long expiresIn;

    public static AuthDto success(String token, Long expiresIn, UserDTO userDTO ) {
        AuthDto dto = new AuthDto();
        dto.setId(userDTO.getId());
        dto.setToken(token);
        dto.setEmail(userDTO.getEmail());
        dto.setExpiresIn(expiresIn);
        dto.setMessage("Login successful");
        return dto;
    }

    public static AuthDto error(String message) {
        AuthDto dto = new AuthDto();
        dto.setMessage(message);
        return dto;
    }


}
