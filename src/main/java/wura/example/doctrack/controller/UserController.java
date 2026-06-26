package wura.example.doctrack.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
//import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wura.example.doctrack.dto.AuthDto;
import wura.example.doctrack.dto.UserDTO;
import wura.example.doctrack.service.AuthService;
import wura.example.doctrack.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")

public class UserController {
    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerProfile(@RequestBody @Valid UserDTO userDto) {
        return userService.registerProfile(userDto);

    }

    @PostMapping("/login")
    public ResponseEntity<AuthDto> login(@RequestBody AuthDto authDto) {
        return authService.login(authDto);
    }
}
