package com.library.user_service.AuthController;




import com.library.user_service.AuthenticationService.AuthService;
import com.library.user_service.Entity.User;
import com.library.user_service.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.library.user_service.Enum.Role;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String firstName,
            @RequestParam String lastName
    ) {
        try {
            User user = authService.register(email, password, firstName, lastName);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());  // ← Now shows "Email already in use"
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestParam String email,
            @RequestParam String password
    ) {
        String token = authService.login(email, password);
        return ResponseEntity.ok(token);
    }
    @PostMapping("/register-admin")
    public ResponseEntity<User> registerAdmin(
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String firstName,
            @RequestParam String lastName) {
        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .firstName(firstName)
                .lastName(lastName)
                .role(Role.ADMIN)  // ← ADMIN
                .build();
        userRepository.save(user);
        return ResponseEntity.ok(user);
    }
}
