package itu.auth.mg.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import itu.auth.mg.args.ApiResponse;
import itu.auth.mg.args.LoginData;
import itu.auth.mg.args.Otp;
import itu.auth.mg.args.VerificationData;
import itu.auth.mg.model.Setting;
import itu.auth.mg.model.Token;
import itu.auth.mg.model.User;
import itu.auth.mg.repositories.UserRepository;
import itu.auth.mg.service.UserService;
import jakarta.mail.MessagingException;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository ur;

    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<User>>> getAllUser() {
        return ResponseEntity.ok(new ApiResponse<List<User>>(true, "list users !", ur.findAll()));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable Long id, @RequestBody User user) {
        try {
            Optional<User> updatedUser = userService.update(id, user);
            if (updatedUser.isPresent()) {
                return ResponseEntity.ok(new ApiResponse(true, "User mise à jour avec succès", updatedUser.get()));
            } else {
                return ResponseEntity.status(404).body(new ApiResponse(false, "User non trouvée pour mise à jour", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse(false, e.getMessage(), null));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@RequestBody User user) throws MessagingException {
        try {
            userService.registerUser(user);
            return ResponseEntity.ok(new ApiResponse<>(true, "Inscription réussie ! Vérifiez votre e-mail.", null));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse<>(false, "Inscription failed ! Email dupliquer.", null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody LoginData user) throws Exception {
        try {
            if (userService.loginUser(user)) {
                return ResponseEntity.ok(new ApiResponse<>(
                    true, "Connexion réussie !", null
                ));
            } else {
                return ResponseEntity.status(401).body(new ApiResponse<>(
                    false, "Identifiants incorrects ou compte non activé.", null
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse<>(
                    false, e.getMessage(), null
                ));
        }
    }

    @PostMapping("/login/otp")
    public ResponseEntity<ApiResponse<Token>> verifyLoginOtp(@RequestBody Otp otp) throws Exception {
        try {
            if (userService.verifyOtp(otp)) {
                Token token = userService.genererToken(otp.getPin());
                return ResponseEntity.ok(new ApiResponse<>(
                    true, "Pin validee !", token 
                ));
            } else {
                return ResponseEntity.status(401).body(new ApiResponse<>(
                    false, "Pin deja utilise ou a expierer.", null
                ));
            }
        } catch(Exception e) {
            return ResponseEntity.status(401).body(new ApiResponse<>(
                    false, e.getMessage(), null
                ));
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<ApiResponse<String>> verify(@RequestParam String token) {
        if (userService.verify(token)) {
            return ResponseEntity.ok(new ApiResponse<>(true, "Compte activé avec succès !", null));
        }
        return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Token invalide ou expiré.", null));
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<String>> verifyHash(@RequestBody VerificationData data) {
        if (userService.verify(data.getPin())) {
            return ResponseEntity.ok(new ApiResponse<>(true, "Compte activé avec succès !", null));
        }
        return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Token invalide ou expiré.", null));
    }

    @GetMapping("/pin/{email}")
    public ResponseEntity<ApiResponse<List<User>>> getPin(@PathVariable String email) throws MessagingException {
        if (email != null) {
            Optional<User> u = ur.findByEmail(email);
            if (u.isPresent()) {
                userService.sendVerifyOtp(u.get());
                return ResponseEntity.ok(new ApiResponse<List<User>>(true, "Verifier votre email !", null));      
            }
            return ResponseEntity.status(404).body(new ApiResponse<List<User>>(false, "Error user not found!", null));
        }
        return ResponseEntity.ok(new ApiResponse<List<User>>(true, "Email invalide !", null));      
    }
}
