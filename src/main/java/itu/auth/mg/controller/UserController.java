package itu.auth.mg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import itu.auth.mg.args.ApiResponse;
import itu.auth.mg.model.User;
import itu.auth.mg.service.UserService;
import itu.auth.mg.util.LoginData;
import itu.auth.mg.util.VerificationData;
import jakarta.mail.MessagingException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@RequestBody User user) throws MessagingException {
        userService.registerUser(user);
        return ResponseEntity.ok(new ApiResponse<>(true, "Inscription réussie ! Vérifiez votre e-mail.", null));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> register(@RequestBody LoginData user) throws MessagingException {
        userService.registerUser(user);
        return ResponseEntity.ok(new ApiResponse<>(true, "Inscription réussie ! Vérifiez votre e-mail.", null));
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
