package itu.auth.mg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import itu.auth.mg.args.ApiResponse;
import itu.auth.mg.args.LoginData;
import itu.auth.mg.args.Otp;
import itu.auth.mg.args.VerificationData;
import itu.auth.mg.model.Token;
import itu.auth.mg.model.User;
import itu.auth.mg.service.UserService;
import jakarta.mail.MessagingException;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

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
}
